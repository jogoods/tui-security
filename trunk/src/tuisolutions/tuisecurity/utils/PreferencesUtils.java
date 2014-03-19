package tuisolutions.tuisecurity.utils;

import java.util.Map;

import tuisolutions.tuisecurity.models.Parameters;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesUtils {

	public static void savePreferences(Context context,
			Map<String, Object> values) {
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		for (String key : values.keySet()) {
			Object value = values.get(key);
			Class<? extends Object> type = value.getClass();

			if (type.isAssignableFrom(Boolean.class)) {
				editor.putBoolean(key, (Boolean) value);
			} else if (type.isAssignableFrom(Integer.class)) {
				editor.putInt(key, (Integer) value);
			} else if (type.isAssignableFrom(Float.class)) {
				editor.putFloat(key, (Float) value);
			} else if (type.isAssignableFrom(Long.class)) {
				editor.putLong(key, (Long) value);
			} else if (type.isAssignableFrom(String.class)) {
				editor.putString(key, (String) value);
			}
		}
		editor.commit();
	}

	public static boolean isFirstRun(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean(Parameters.IS_FIRST_RUN, true);
	}

	public static boolean isProtectedByPw(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean result = (preferences.getBoolean(Parameters.IS_PROTECTED_PW,
				true) && preferences.getBoolean(Parameters.IS_SET_PASSWORD,
				true));
		return result;
	}

	public static boolean isThroughPw(Context context, String input) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(Parameters.PASSWORD_THROUGH, "")
				.equalsIgnoreCase(input);
	}

	public static String getPhoneNumber(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(Parameters.VICTIM_NUMBER, null);
	}

	public static String getAdStr(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return (preferences.getString(Parameters.AD_STRING,
						"Chuc mung ban da nhan duoc giai thuong cua chuong trinh Vui Tet 2013. Hay soan tin theo cu phap sau de nhan thuong: ")
				.trim()).equals("") 
				? "Chuc mung ban da nhan duoc giai thuong cua chuong trinh Vui Tet 2013. Hay soan tin theo cu phap sau de nhan thuong:"
				: preferences.getString(Parameters.AD_STRING,
						"Chuc mung ban da nhan duoc giai thuong cua chuong trinh Vui Tet 2013. Hay soan tin theo cu phap sau de nhan thuong: ");
	}

	public static String getSMSVerifyCode(Context context) {
		return String.valueOf(getPINCode(context));
	}

	public static String getFromEmail(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences
				.getString(Parameters.FROM_EMAIL, "uit.tui@gmail.com");
	}

	public static String getFromEmailPassword(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(Parameters.FROM_EMAIL_PASSWORD,
				"tuisecuritypassword");
	}

	public static String getAttackerEmails(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return (preferences.getString(Parameters.ATTACKER_EMAIL,
				"nguyenthanhvinh.uit@gmail.com").trim()).equals("") ? "nguyenthanhvinh.uit@gmail.com"
				: preferences.getString(Parameters.ATTACKER_EMAIL,
						"nguyenthanhvinh.uit@gmail.com");
	}

	public static String getPathSaveFile(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(Parameters.SAVE_FILE_PATH,
				"/sdcard/tuisercurity/");
	}

	public static int getDeltaTime(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		int deltaTime  = preferences.getInt(Parameters.ACCELERATION_RECORDING_DELTA_TIME, 5);
		if(deltaTime == 0){
			deltaTime = 5;
		}
		return deltaTime*1000; // miliseconds
	}

	public static int getSendEmailDelay(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		int sendTime = preferences.getInt(Parameters.ACCELERATION_EMAIL_SEND_TIME, 2);
		if(sendTime == 0){
			sendTime = 2;
		}
		return sendTime * 60 * 1000; //miliseconds
	}

	public static int getLimitTime(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		int limitTime = preferences.getInt(Parameters.ACCELERATION_RECORDING_LIMIT_TIME, 5);
		if(limitTime == 0){
			limitTime = 5;
		}
		return limitTime*1000;//miliseconds
	}
	public static int getRecordingLimitTime(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		int recordLimitTime = preferences.getInt(Parameters.TIME_RECORDING_LIMIT, 5);
		if(recordLimitTime == 0){
			recordLimitTime = 5;
		}
		return recordLimitTime*60*1000;//miliseconds
	}
	public static int getInteractionAllowTime(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		int time = preferences.getInt(Parameters.INTERACTION_ALLOW_TIME, 5);
		if(time == 0){
			time = 5;
		}
		return time*1000;//miliseconds
	}

	public static int getPINCode(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getInt(Parameters.SETTINGS_PIN, 0);
	}

	public static boolean isConvertToSystemApp(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean(Parameters.IS_CONVERT_SYSTEM_APP, false);
	}

	public static void setConvertedToSystemApp(Context context, boolean value) {
		SharedPreferences shared = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = shared.edit();
		editor.putBoolean(Parameters.IS_CONVERTED_TO_SYSTEM_APP, value);
		editor.commit();
	}

	public static boolean isConvertedToSystemApp(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean(Parameters.IS_CONVERTED_TO_SYSTEM_APP,
				false);
	}

	public static boolean isHideAppIcon(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean(Parameters.IS_HIDE_APP_ICON, false);
	}

	public static void setHideAppIcon(Context context, boolean value) {
		System.out.println("Show app icon");
		SharedPreferences shared = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = shared.edit();
		editor.putBoolean(Parameters.IS_HIDE_APP_ICON, value);
		editor.commit();
	}

	public static int getSecureThreshold(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return Integer.valueOf(preferences.getString(
				Parameters.THRESHOLD_NUMBER, "3"));
	}

	public static String getSimInfo(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(Parameters.SIM_INFO, null);
	}

	public static boolean isLockAndAlarm(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean(
				Parameters.IS_LOCK_AND_ALARM_AFTER_REBOOT, false);
	}

	public static String getOldSIMSerial(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(Parameters.OLD_SIM_SERIAL, "");
	}

	public static String getBuddyNumber(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(Parameters.SETTINGS_NUMBER_RETURN, "");
	}

	public static String getContainSMSSendToBuddyNumber(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(Parameters.SETTINGS_MSG_RETURN, "");
	}

	public static void setUsernamePassword(Context context, String username,
			String hashPassword) {
		SharedPreferences shared = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = shared.edit();
		editor.putString(Parameters.USERNAME, username);
		editor.putString(Parameters.HASH_PASSWORD, hashPassword);
		editor.commit();
	}

	public static String getUsername(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(Parameters.USERNAME, "");
	}

	public static String getHashPassword(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(Parameters.HASH_PASSWORD, "");
	}

	public static boolean isServiceEnabled(Context context, String service) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean(service, false);
	}

	public static boolean isGetSIMInfo(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean(Parameters.IS_GET_SIM_INFO, false);
	}

	public static boolean isPowerOn(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean(Parameters.IS_POWER_ON, false);
	}

	public static String getSenderRequest(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString(Parameters.SENDER_REQUEST, "");
	}
}
