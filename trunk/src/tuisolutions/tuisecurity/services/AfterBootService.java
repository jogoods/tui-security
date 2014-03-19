package tuisolutions.tuisecurity.services;

import tuisolutions.tuisecurity.ui.LockScreenActivity;
import tuisolutions.tuisecurity.ui.PinInputActivity;
import tuisolutions.tuisecurity.utils.PreferencesUtils;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;


public class AfterBootService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new LockPhone(getApplicationContext()).execute();
		return super.onStartCommand(intent, flags, startId);
	}
	class LockPhone extends AsyncTask<Void, Void, Void>{
		Context context;
		public LockPhone(Context context) {
			this.context = context;
		}
		@Override
		protected Void doInBackground(Void... params) {
			// Check condition to open lockScreen and alarm 
			if (PreferencesUtils.isFirstRun(getApplicationContext())) {
				// Do nothing
			}else{
				//LockScreenActivity.unlocked = false;
				PinInputActivity.isLoggedIn = false;
				Intent intent = new Intent(context, LockScreenActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				
				
				// Condition to stop music
				// 1. Unlock with PIN.
				// 2. If click HOME button, user must re-login app with PIN to stoop music.
				// In case icon app was hide and click HOME. User must send SMS to show icon app and login in with PIN 
				while(!LockScreenActivity.unlocked && !PinInputActivity.isLoggedIn){
					// Continue alarm
				}
				LockScreenActivity.unlocked = true;
				PinInputActivity.isLoggedIn = true;
			}
			return null;
		}
		
	}
}
