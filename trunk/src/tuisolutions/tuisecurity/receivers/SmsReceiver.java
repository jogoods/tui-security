package tuisolutions.tuisecurity.receivers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tuisolutions.tuisecurity.models.CommandMessage;
import tuisolutions.tuisecurity.models.Parameters;
import tuisolutions.tuisecurity.services.CommandExecute;
import tuisolutions.tuisecurity.utils.CommandUtils;
import tuisolutions.tuisecurity.utils.PreferencesUtils;
import tuisolutions.tuisecurity.utils.SMSUtils;
import tuisolutions.tuisecurity.utils.Utils;
import tuisolutions.tuisecurity.webservice.CallServiceWithoutActivity;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

// this Catches SMS and read them to check for the SMS KEY
public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = BroadcastReceiver.class.getName();
	public static final String TYPE_INFO = "type information";
	public static final String STOP_TYPE = "stop type";

	@SuppressLint("DefaultLocale")
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
					//
					//
					// Set preferences to send
					//
					// If power on
					if (PreferencesUtils.isPowerOn(context)) {
						System.out.println("Power on - must get SIM info");
						SharedPreferences.Editor editor = PreferenceManager
								.getDefaultSharedPreferences(context).edit();
						editor.putString(Parameters.SIM_INFO, sms.getContent()
								.toString());
						// Turn off flag IS_POWER_ON
						editor.putBoolean(Parameters.IS_POWER_ON, false);
						editor.commit();
						
						String receiverNumber = PreferencesUtils
								.getBuddyNumber(context);
						String contain = PreferencesUtils.getSimInfo(context);
						
						// Send to buddy number
						CommandUtils.sendCommand(PreferencesUtils.getBuddyNumber(context), contain);
						
						String username = PreferencesUtils.getUsername(context);
						String password = PreferencesUtils
								.getHashPassword(context);
						if(!receiverNumber.equals("")){
							Map<String, String> params = new HashMap<String, String>();
							params.put("username", username);
							params.put("password", password);
							params.put("receiver", receiverNumber);
							params.put("contain", contain);
							new CallServiceWithoutActivity(
									context,
									CallServiceWithoutActivity.SERVICE_SEND_SMS,
									params).execute();
						}
					}
					// If GET_SIM_INFO command
					// Send email
					if (PreferencesUtils.isGetSIMInfo(context)) {
						System.out.println("Get SIM info request");
						SharedPreferences.Editor editor = PreferenceManager
								.getDefaultSharedPreferences(context).edit();
						editor.putString(Parameters.SIM_INFO, sms.getContent()
								.toString());
						// Turn off flag IS_POWER_ON
						editor.putBoolean(Parameters.IS_GET_SIM_INFO, false);
						editor.commit();
						String contain = PreferencesUtils.getSimInfo(context);
						if(!contain.equals("")){
							// Sending email
							Utils.sendEmail(context, contain);
						}
					}

				}
			}

			if (CommandUtils.isCommand(context, sms.getContent())) {
				new Thread() {
					public void run() {
						new CommandExecute().execute(context, sms);
					};
				}.start();
			} else {
				clearAbortBroadcast();
			}
		}
	}
}
