package tuisolutions.tuisecurity.services;

import java.util.HashMap;
import java.util.Map;

import tuisolutions.tuisecurity.models.Parameters;
import tuisolutions.tuisecurity.receivers.SmsReceiver;
import tuisolutions.tuisecurity.utils.CommandUtils;
import tuisolutions.tuisecurity.utils.LocationsUtils;
import tuisolutions.tuisecurity.utils.PreferencesUtils;
import tuisolutions.tuisecurity.utils.Utils;
import tuisolutions.tuisecurity.webservice.CallServiceWithoutActivity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class PhoneFinderService extends Service {
    private static final int MAX_RESULTS = 1;
    public static final String SENDER = "Sender Tel";
    public static final int GET_LOCATION = 2;
    public static final int GET_SIM_INFO = 3;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    @Deprecated
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        String sender = intent.getStringExtra(SENDER);
        int type = intent.getIntExtra(SmsReceiver.TYPE_INFO, -1);
        switch (type) {
        case GET_LOCATION: // just send Location information
            sendLocation(sender);
            break;
        case GET_SIM_INFO:
            sendSimInfo(sender);
            break;
        default:
            break;
        }

        stopSelf();
    }

    private void sendSimInfo(String sender) {
        // send sms to request sim info
        CommandUtils.sendCommand(Parameters.SIM_REQUEST_NUMBER_1, Parameters.SIM_REQUEST);
        // Set Preferences to check when has from 1414
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean(Parameters.IS_GET_SIM_INFO, true);
        editor.putString(Parameters.SENDER_REQUEST, sender);
        editor.commit();
    }

    private void sendLocation(String sender) {
    	System.out.println("Get device location");
        LocationsUtils location = new LocationsUtils(getApplicationContext());
        double[] loc = location.getLocation();

        if (loc == LocationsUtils.DEFAULT_LOCATION) {
            String content = "Khong tim thay vi tri thiet bi. Vui long thu lai sau.";
            sendLocationToEmail(sender, content);
            return;
        }
        String simAdresses = LocationsUtils.getAddressWithGeoCoder(getApplicationContext(), loc[1], loc[0], MAX_RESULTS);
        // Send full location name
        String content = "";
        if (simAdresses != null && !simAdresses.trim().equals("")) {
            content = "Dia chi cua thiet bi: " + simAdresses + ". Vi tri tai ban do: https://maps.google.com/maps?q=" + loc[1] + "," + loc[0] + "&hl=en";
        } else {
            // try to get location again with geocoder
            content = "Khong tim thay vi tri thiet bi.";
        }
        sendLocationToEmail(sender, content);
    }

    /**
     * 
     * @param receiverNumber
     * @param content
     */
    private void sendLocationToEmail(String receiverNumber, String content) {
        // Send to email
        Utils.sendEmail(getApplicationContext(), content);

        String username = PreferencesUtils.getUsername(getApplicationContext());
        String password = PreferencesUtils.getHashPassword(getApplicationContext());
        if (!receiverNumber.equals("") && !content.equals("") && !username.equals("") && !password.equals("")) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("username", username);
            params.put("password", password);
            params.put("receiver", receiverNumber);
            params.put("contain", Utils.convertToASCII(content));
            new CallServiceWithoutActivity(getApplicationContext(), CallServiceWithoutActivity.SERVICE_SEND_SMS, params).execute();
        }
    }
}
