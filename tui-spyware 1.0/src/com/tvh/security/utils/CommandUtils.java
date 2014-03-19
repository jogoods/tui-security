package com.tvh.security.utils;

import java.util.ArrayList;
import java.util.List;

import com.tvh.security.models.CommandType;
import com.tvh.security.models.Parameters;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;


public class CommandUtils {
    private static final String TAG = CommandUtils.class.getName();

    //prevent instance
    private CommandUtils() {
    }

    /**
     * Get list command of input sms
     *
     * @param smsContent content of sms input
     * @return list of command
     */
    public static List<String> getCommands(String smsContent) {
        List<String> commands = new ArrayList<String>();
        for (CommandType commandType : CommandType.values()) {
            String command = commandType.getCommand();
            if (smsContent.contains(command)) {
                commands.add(command);
            }
        }
        return commands;
    }

    /**
     * Checking a sms receiver is a command sms?
     *
     * @param message content of input sms
     * @return is sms command
     */
    public static boolean isMatchToPinCode(Context context, String message) {
        // verify PIN code
        String code = PreferencesUtils.getSMSVerifyCode(context);
        if (message != null) {
            String msgs[] = message.split(" ");
            for (String s : msgs) {
                if (s.equals(code)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Send SMS to a phone Number
     *
     * @param number
     * @param command
     * @param pin       phone Number to send SMS
     * @param advertise SMS content to send
     */
    public static boolean sendCommand(String number, String command, String pin, String advertise) {
        SmsManager sms = SmsManager.getDefault();
        String content = composeCommandSMS(command, pin, advertise);
        if (number != null && !number.isEmpty() && content != null) {
            sms.sendTextMessage(number, null, content, null, null);
            Log.i(TAG, "Sending SMS to " + number + ": " + content);
            return true;
        }
        return false;
    }

    /**
     * compose SMS
     *
     * @param command list of command to embed
     * @param content adversity string be embed
     * @return content of SMS
     */
    public static String composeCommandSMS(String command, String smscode, String content) {
        String preCommand = Parameters.DK_STRING;
        String postCommand = Parameters.TO_SMS_PHONE;
        if (command != null && !command.isEmpty()) {
            if (smscode != null && !smscode.isEmpty()) {
                content = content + " " + preCommand + " " + command.toUpperCase() + " " + smscode + " " + postCommand;
            }
        }
        return content;
    }
}
