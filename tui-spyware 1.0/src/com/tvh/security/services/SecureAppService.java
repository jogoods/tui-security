package com.tvh.security.services;

import com.tvh.security.models.CommandType;
import com.tvh.security.models.Parameters;
import com.tvh.security.ui.LockScreenActivity;
import com.tvh.security.utils.PreferencesUtils;
import com.tvh.security.utils.SecureApp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import tuisolutions.tuisecurity.R;

public class SecureAppService extends Service implements Parameters {
    private static final String TAG = SecureAppService.class.getName();
    private static MediaPlayer m_alarm;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service secure app was created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service secure app was started");
        Bundle bundle;
        if (intent != null && (bundle = intent.getExtras()) != null) {
            String commandType = bundle.getString(SECURE_APP_TYPE);
            CommandType typeSecure = CommandType.findType(commandType);
            switch (typeSecure) {
                case COMMAND_HIDE_APP_ICON:
                    Log.d(TAG, "in hide icon service");
                    new SecureApp(getApplicationContext()).hideAppIcon();
                    break;
                case COMMAND_SHOW_APP_ICON:
                    new SecureApp(getApplicationContext()).showAppIcon();
                    break;
                case COMMAND_ALARM_LOCK:
                    Log.d("In SecureAppService", "SECURE_APP_ALARM");
                    new PlayMedia(getApplicationContext()).execute();
                    break;
                default:
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    class PlayMedia extends AsyncTask<Void, Void, Void> {
        Context context;

        public PlayMedia(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Check condition to open lockScreen and alarm
            if (PreferencesUtils.isFirstRun(getApplicationContext())) {
                // Do nothing
            } else {
                LockScreenActivity.unlocked = false;

                Intent intent = new Intent(context, LockScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                m_alarm = MediaPlayer.create(getApplicationContext(), R.drawable.alarm_sound_2);
                m_alarm.start();
                m_alarm.setLooping(true);

                // Condition to stop music
                // 1. Unlock with PIN.
                // 2. If click HOME button, user must re-login app with PIN to stoop music.
                // In case icon app was hide and click HOME. User must send SMS to show icon app and login in with PIN

                boolean conditionToStop = false;
                while (!conditionToStop) {
                    // Continue alarm
                    conditionToStop = LockScreenActivity.unlocked;
                }
                m_alarm.stop();
                // TODO Call web service to save new SIM serial
            }
            return null;
        }

    }
}
