package tuisolutions.tuisecurity.ui;

import tuisolutions.tuisecurity.R;
import tuisolutions.tuisecurity.controllers.AntiThiefListener;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DialogConfirmActivity extends Activity implements OnClickListener {
	private Button btnConfirm, btnCancel;
	public static boolean isHide = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		isHide = false;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog_confirm_anti_thief);
		btnConfirm = (Button) findViewById(R.id.btnConfirmActive);
		btnCancel = (Button) findViewById(R.id.btnCancelActive);

		btnConfirm.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isHide = true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnConfirmActive:
			Toast.makeText(getApplicationContext(), "Confirm",
					Toast.LENGTH_SHORT).show();
			// Call function start listener after 5 seconds
			// This time is enough to put down device on the table
			// And the device in quiet
			// Sleep 5 seconds
			finish();
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(3000);
						startListener(getBaseContext());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
			break;
		case R.id.btnCancelActive:
			
			Toast.makeText(getApplicationContext(), "Cancel",
					Toast.LENGTH_SHORT).show();
			finish();
			break;
		default:
			break;
		}
		
	}

	protected void startListener(Context context) {
		if (!AntiThiefListener.isActivated) {
			new AntiThiefListener(context).startListener();
		}
	}

}
