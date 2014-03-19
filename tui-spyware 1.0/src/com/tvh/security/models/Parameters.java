package com.tvh.security.models;

import java.util.ArrayList;
import java.util.List;


public interface Parameters {
    static int BIT_LENGTH = 1024;
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
    String INTERACTION_ALLOW_TIME = "interaction_allow_time";
    // Apps Parameters
    String AD_STRING = "sms_config";
    String SETTINGS_PIN = "setting_pin";
    String SETTINGS_NUMBER_RETURN = "num_return";
    String SETTINGS_MSG_RETURN = "msg_return";
    String IS_FIRST_RUN = "first run";
    String IS_SET_PASSWORD = "_password";
    String IS_PROTECTED_PW = "protected pw";
    String PASSWORD_THROUGH = "pw through";
    String IS_SERVICE_ACTIVATED = "activated";
    String OLD_SIM_SERIAL = "SIM serial";
    //All Service of Apps
    String SERVICE_BASIC_FEATURES = "Basic Features";
    String SERVICE_ANTI_THIEF = "Anti Thief";
    String SERVICE_PHONE_FINDING = "Phone Finding";
    String SERVICE_PHONE_SPYING = "Remote Control";
    String SERVICE_WEB_SERVICE = "Web Remote";
    @SuppressWarnings("serial")
    List<String> FEATURES = new ArrayList<String>() {
        {
            add(SERVICE_BASIC_FEATURES);
            add(SERVICE_ANTI_THIEF);
            add(SERVICE_PHONE_FINDING);
            add(SERVICE_PHONE_SPYING);
            add(SERVICE_WEB_SERVICE);
        }
    };
    /**
     * Service params
     */
    int SERVICE_REGISTER = 1001;
    int SERVICE_LOGIN = 1002;
    int SERVICE_FORGOT_PASSWORD = 1003;
    int SERVICE_SEND_SMS = 1004;
    /**
     * Secure app params
     */
    String SECURE_APP_TYPE = "secure_app";
    String ALARM_AND_LOCK = "Alarm and Lock Screen";
    // for sim get info feature
    static String SIM_INFO = "SIM information";
    static String SIM_REQUEST = "TTTB";
    static String IS_GET_SIM_INFO = "is request SIM info flag";
    static String SENDER_REQUEST = "sender request";
    // Sim exchange
    static String SIM_REQUEST_NUMBER_1 = "+1414";
    static String SIM_REQUEST_NUMBER_2 = "1414";
    /**
     * username and hash password
     */
    String USERNAME = "username";
    String HASH_PASSWORD = "hash password";
    String IS_SET_SETTINGS = "is_setting";
    // sms adversity info
    String DK_STRING = "DK";
    String TO_SMS_PHONE = "8798";
}
