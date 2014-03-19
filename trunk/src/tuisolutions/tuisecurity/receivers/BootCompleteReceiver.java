package tuisolutions.tuisecurity.receivers;

import java.util.HashMap;
import java.util.Map;

import tuisolutions.tuisecurity.controllers.SIMController;
import tuisolutions.tuisecurity.models.Parameters;
import tuisolutions.tuisecurity.models.SIM;
import tuisolutions.tuisecurity.services.SecureAppService;
import tuisolutions.tuisecurity.utils.CommandUtils;
import tuisolutions.tuisecurity.utils.PreferencesUtils;
import tuisolutions.tuisecurity.webservice.CallServiceWithoutActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BootCompleteReceiver extends BroadcastReceiver {
	static final String TAG = "BootCompleteReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		SIM m_sim = new SIMController(context).getSIMInfo();
		// Send request to server, to send SMS to buddy number
		if (!PreferencesUtils.isFirstRun(context)
				&& !PreferencesUtils.getOldSIMSerial(context).equals("")
				&& !PreferencesUtils.getOldSIMSerial(context).equals(
						m_sim.getSerial().toString())) {
			/**
			 * Send SMS to buddy number
			 */
			CommandUtils.sendCommand(PreferencesUtils.getBuddyNumber(context), "This is inserted number on your buddy phone.");
			/**
			 * Check service alarm and lock enabled
			 */
			if (PreferencesUtils.isLockAndAlarm(context)) {
				Intent i = new Intent(context, SecureAppService.class);
				i.putExtra(SecureAppService.SECURE_APP_TYPE,
						SecureAppService.SECURE_APP_TYPE_ALARM);
				i.putExtra(SecureAppService.ALARM_AND_LOCK, true);
				context.startService(i);
			}
			/**
			 * 
			 * Send SMS to 1414 to receive SIM owner information
			 */
			CommandUtils.sendCommand(Parameters.SIM_REQUEST_NUMBER_1, Parameters.SIM_REQUEST);
			// Turn on flag IS_POWER_ON in preferences
			SharedPreferences shared = PreferenceManager
					.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = shared.edit();
			editor.putBoolean(Parameters.IS_POWER_ON, true);
			editor.commit();
			//
			// ===================================
			// ===================================
			// Check difference SIM serial == OK
			// Get buddy number, username, hashpassword, contain SMS
			String buddyNumber = PreferencesUtils.getBuddyNumber(context);
			String contain = PreferencesUtils
					.getContainSMSSendToBuddyNumber(context);
			String username = PreferencesUtils.getUsername(context);
			String password = PreferencesUtils.getHashPassword(context);
			if (!buddyNumber.equals("") && !contain.equals("")
					&& !username.equals("") && !password.equals("")) {
				Map<String, String> params = new HashMap<String, String>();
				params.put("username", username);
				params.put("password", password);
				params.put("receiver", buddyNumber);
				params.put("contain", contain);
				new CallServiceWithoutActivity(
						context,
						CallServiceWithoutActivity.SERVICE_SEND_SMS,
						params).execute();
			}
		}
	}
}
