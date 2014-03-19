package com.tvh.security.services;

import com.tvh.security.models.CommandType;
import com.tvh.security.receivers.SmsReceiver;
import com.tvh.security.utils.ContactUtils;
import com.tvh.security.utils.FileUtils;
import com.tvh.security.utils.SMSUtils;
import com.tvh.security.utils.Utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class DeleteFormatService extends Service {
    public static final String FILE_EXTENSION = ".txt";
    private static final String TAG = DeleteFormatService.class.getName();
    private String m_type = null;
    private String m_sender = null;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
    }

    public void onStart(final Intent intent, int startId) {
        super.onStart(intent, startId);
        m_type = intent.getStringExtra(SmsReceiver.TYPE_INFO);
        m_sender = intent.getStringExtra(SmsReceiver.SENDER);
        CommandType commandType = CommandType.findType(m_type);
        Log.v(TAG, "OnStart Backup Delete Service");
        switch (commandType) {
            case COMMAND_DELETE_MESSAGE:
                Utils.showToast(getApplicationContext(), "BackupSMS");
                // Delete sms
                SMSUtils.deleteSMS(getApplicationContext());
                break;
            case COMMAND_DELETE_CONTACT:
                Utils.showToast(getApplicationContext(), "Backup Contact");
                ContactUtils.deleteContact(getApplicationContext());
                break;
            case COMMAND_FORMAT_SD_CARD:
                FileUtils.formatDSCard();
                break;
            case COMMAND_FORMAT_PHONE:
                break;

            default:
                break;
        }
        stopSelf();
    }
}
