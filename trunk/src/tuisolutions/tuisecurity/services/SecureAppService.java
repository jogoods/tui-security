package tuisolutions.tuisecurity.services;

import tuisolutions.tuisecurity.R;
import tuisolutions.tuisecurity.controllers.SIMController;
import tuisolutions.tuisecurity.models.Parameters;
import tuisolutions.tuisecurity.models.SIM;
import tuisolutions.tuisecurity.ui.LockScreenActivity;
import tuisolutions.tuisecurity.ui.PinInputActivity;
import tuisolutions.tuisecurity.utils.PreferencesUtils;
import tuisolutions.tuisecurity.utils.SecureApp;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

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
			int typeSecure = bundle.getInt(SECURE_APP_TYPE);
			switch (typeSecure) {
			case SECURE_APP_TYPE_HIDE_ICON:
				Log.d(TAG, "in hide icon service");
				new SecureApp(getApplicationContext()).hideAppIcon();
				break;
			case SECURE_APP_TYPE_SHOW_ICON:
				new SecureApp(getApplicationContext()).showAppIcon();
				break;
			case SECURE_APP_TYPE_LOCK_SCREEN:
				/** 
				 * Future work
				 */
				/*
				Intent i = new Intent(getApplicationContext(),
						LockScreenActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplicationContext().startActivity(i);
				*/
				break;
			case SECURE_APP_TYPE_ALARM:
				boolean showLockScreen = bundle.getBoolean(ALARM_AND_LOCK);
				new PlayMedia(getApplicationContext(), showLockScreen).execute();	
				break;
			default:
				break;
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	class PlayMedia extends AsyncTask<Void, Void, Void>{
		Context context;
		boolean showLockScreen = false;
		public PlayMedia(Context context, boolean isShowLockScreen) {
			this.context = context;
			this.showLockScreen = isShowLockScreen;
		}
		@Override
		protected Void doInBackground(Void... params) {
			// Check condition to open lockScreen and alarm 
			if (PreferencesUtils.isFirstRun(getApplicationContext())) {
				// Do nothing
			}else{
				PinInputActivity.isLoggedIn = false;
				
				if(showLockScreen)
				{
					LockScreenActivity.unlocked = false;
					Intent intent = new Intent(context, LockScreenActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
				
				m_alarm = MediaPlayer.create(getApplicationContext(), R.drawable.alarm_sound_2);
				m_alarm.start();
				m_alarm.setLooping(true);
				
				// Condition to stop music
				// 1. Unlock with PIN.
				// 2. If click HOME button, user must re-login app with PIN to stoop music.
				// In case icon app was hide and click HOME. User must send SMS to show icon app and login in with PIN 
				
				boolean conditionToStop = false;
				
				while(!conditionToStop){
					// Continue alarm
					conditionToStop = PinInputActivity.isLoggedIn || ((showLockScreen) ? LockScreenActivity.unlocked : false);
				}
				m_alarm.stop();
				LockScreenActivity.unlocked = true;
				PinInputActivity.isLoggedIn = true;
				SIM  m_sim = new SIMController(context).getSIMInfo();
				SharedPreferences shared = PreferenceManager
						.getDefaultSharedPreferences(context);
				SharedPreferences.Editor editor = shared.edit();
				editor.putString(Parameters.OLD_SIM_SERIAL, m_sim.getSerial().toString());
				editor.commit();
				// Call web service to save new SIM serial
				// TODO
			}
			return null;
		}
		
	}
}
