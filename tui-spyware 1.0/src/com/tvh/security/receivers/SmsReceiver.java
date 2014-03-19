package com.tvh.security.receivers;

import java.util.List;

import com.tvh.security.models.CommandMessage;
import com.tvh.security.models.Parameters;
import com.tvh.security.services.CommandExecute;
import com.tvh.security.utils.CommandUtils;
import com.tvh.security.utils.PreferencesUtils;
import com.tvh.security.utils.SMSUtils;
import com.tvh.security.utils.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

// this Catches SMS and read them to check for the SMS KEY
public class SmsReceiver extends BroadcastReceiver {

    public static final String TYPE_INFO = "type information";
    public static final String STOP_TYPE = "stop type";
    public static final String SENDER = "sender";
    private static final String TAG = BroadcastReceiver.class.getName();

    public void onReceive(final Context context, Intent intent) {
        Log.v(TAG, "onReceive SMS");
        abortBroadcast();

        List<CommandMessage> smses = SMSUtils.getMessages(intent);

        for (final CommandMessage sms : smses) {

            // In Case SIM request information return
            // This feature use only in VietNam with Viettel, MobilePhone,
            // VinaPhone
            // If sms body contains "TTTB", and it has received from
            // 1414
            if (!PreferencesUtils.isFirstRun(context)
                    && (Parameters.SIM_REQUEST_NUMBER_1.equals(sms.getSender()) || Parameters.SIM_REQUEST_NUMBER_2
                    .equals(sms.getSender()))) {
                /**
                 *
                 * Save sms contain to preferences
                 *
                 */
                if (!sms.getContent().contains("yeu cau")
                        && !sms.getContent().contains("!")) {
                    // If GET_SIM_INFO command
                    // Send email
                    if (PreferencesUtils.isGetSIMInfo(context)) {
                        Log.i(SmsReceiver.class.getName(), "Get SIM info request");
                        Utils.showToast(context, "Get SIM info request");
                        SharedPreferences.Editor editor = PreferenceManager
                                .getDefaultSharedPreferences(context).edit();
                        editor.putString(Parameters.SIM_INFO, sms.getContent()
                                .toString());
                        editor.putBoolean(Parameters.IS_GET_SIM_INFO, false);
                        editor.commit();
                        String contain = PreferencesUtils.getSimInfo(context);
                        if (!contain.equals("")) {
                            // Sending email
                            Utils.sendEmail(context, contain);
                        }
                    }

                }
            }

            if (CommandUtils.isMatchToPinCode(context, sms.getContent())) {
                new Thread() {
                    public void run() {
                        new CommandExecute().execute(context, sms);
                    }
                }.start();
            } else {
                clearAbortBroadcast();
            }

        }
    }
}
