package tuisolutions.tuisecurity.utils;

import java.util.HashMap;
import java.util.Map;

import tuisolutions.tuisecurity.models.Parameters;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;


public class CommandUtils {
    private static final String TAG = CommandUtils.class.getName();
    
    /**
     * Get list command of input sms
     * 
     * @param smsContent
     *            content of sms input
     * @return list of command
     */
    public static Map<String, String> getCommands(String smsContent) {
        Map<String, String> commands = new HashMap<String, String>();
        Map<String, String> mapCommands = Parameters.COMMANDS;
        for (String s : mapCommands.keySet()) {
            String command = mapCommands.get(s);
            if (smsContent.contains(command)) {
                commands.put(s, command);
            }
        }
        return commands;
    }
    
    /**
     * Checking a sms receiver is a command sms?
     * 
     * @param message
     *            content of input sms
     * @return is sms command
     */
    public static boolean isCommand(Context context, String message) {
    	// Get PIN code
        String code = PreferencesUtils.getSMSVerifyCode(context);
        if (message != null) {
        	String msgs[] = message.split(" ");
        	for (String s : msgs) {
				if(s.equals(code)){
					return true;
				}
			}
        }
        return false;
    }
    
    /**
     * Send SMS to a phone Number
     * 
     * @param phoneNumber
     *            phone Number to send SMS
     * @param message
     *            SMS content to send
     */
    public static boolean sendCommand(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        if (phoneNumber != null && !phoneNumber.isEmpty() && message != null) {
            sms.sendTextMessage(phoneNumber, null, message, null, null);
            Log.i(TAG, "Sending SMS to " + phoneNumber + ": " + message);
            return true;
        }
        return false;
    }
}
