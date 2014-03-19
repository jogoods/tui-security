package tuisolutions.tuisecurity.receivers;

import java.util.Date;

import tuisolutions.tuisecurity.controllers.AntiThiefListener;
import tuisolutions.tuisecurity.services.InteractionAlarmService;
import tuisolutions.tuisecurity.ui.DialogConfirmActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PowerButtonReceiver extends BroadcastReceiver {

	boolean active = false;
	private int LIMIT_TIME_ACTIVE = 1000; // miliseconds
	private int LIMIT_TIME_DEACTIVE = 1000; // miliseconds
	private long startTime = 0, endTime = 0;
	private long startTimeForDeactive = 0, endTimeForDeactive = 0;

	@Override
	public void onReceive(final Context context, Intent intent) {
		if (!InteractionAlarmService.ABORT_RECEIVER) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				if(!AntiThiefListener.isActivated){
					startTime = new Date().getTime();
					startTimeForDeactive = 0;
				}else{
					startTime = 0;
					startTimeForDeactive = new Date().getTime();
				}
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				if(!AntiThiefListener.isActivated)
				{
					endTime = new Date().getTime();
					endTimeForDeactive = 0;
				}else{
					endTime = 0;
					endTimeForDeactive = new Date().getTime();
				}
			}

			if (!AntiThiefListener.isActivated && (endTime > startTime)
					&& (endTime - startTime) <= LIMIT_TIME_ACTIVE) {
				if(DialogConfirmActivity.isHide){
					Intent i = new Intent(context, DialogConfirmActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(i);
				}
			}
			else if (AntiThiefListener.isActivated
					&& (endTimeForDeactive > startTimeForDeactive)
					&& (endTimeForDeactive - startTimeForDeactive) <= LIMIT_TIME_DEACTIVE) {
				stopListerner();
			}
		}
	}

	private void stopListerner() {
		if (AntiThiefListener.isActivated) {
			AntiThiefListener.stopListener(false);
		}
	}

}
