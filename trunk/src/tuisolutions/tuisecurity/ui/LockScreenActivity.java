package tuisolutions.tuisecurity.ui;

import tuisolutions.tuisecurity.R;
import tuisolutions.tuisecurity.utils.AsteriskPasswordTransformationMethod;
import tuisolutions.tuisecurity.utils.PreferencesUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LockScreenActivity extends Activity implements OnClickListener {
	private static final String TAG = LockScreenActivity.class.getSimpleName();

	private EditText edt_pin_1, edt_pin_2, edt_pin_3, edt_pin_4;
	private Button btn_unlock;
	private int pinSaved;

	public static boolean unlocked = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_screen);

		edt_pin_1 = (EditText) findViewById(R.id.lock_screen_edpin_1);
		edt_pin_2 = (EditText) findViewById(R.id.lock_screen_edpin_2);
		edt_pin_3 = (EditText) findViewById(R.id.lock_screen_edpin_3);
		edt_pin_4 = (EditText) findViewById(R.id.lock_screen_edpin_4);

		edt_pin_1
				.setTransformationMethod(new AsteriskPasswordTransformationMethod());
		edt_pin_2
				.setTransformationMethod(new AsteriskPasswordTransformationMethod());
		edt_pin_3
				.setTransformationMethod(new AsteriskPasswordTransformationMethod());
		edt_pin_4
				.setTransformationMethod(new AsteriskPasswordTransformationMethod());

		btn_unlock = (Button) findViewById(R.id.lock_screen_btn_unlock);
		btn_unlock.setOnClickListener(this);

		// Check in preferences
		if (PreferencesUtils.isFirstRun(getApplicationContext())) {
			// If no PIN, can not lock
			finish();
		} else {
			unlocked = false;
			// If not first run, check pin saved
			pinSaved = PreferencesUtils.getPINCode(getApplicationContext());
		}
	}

	@Override
	protected void onPause() {
		Intent i = new Intent(getApplicationContext(), LockScreenActivity.class);
		this.finish();
		if (!unlocked)
			startActivity(i);
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		edt_pin_1.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					edt_pin_2.requestFocus();
					edt_pin_2.selectAll();
				}
			}
		});
		edt_pin_2.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					edt_pin_3.requestFocus();
					edt_pin_3.selectAll();
				}
			}
		});
		edt_pin_3.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					edt_pin_4.requestFocus();
					edt_pin_4.selectAll();
				}
			}
		});
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lock_screen_btn_unlock:
			// Get value from PIN edit text
			if (!edt_pin_1.getText().equals(null)
					&& !edt_pin_2.getText().equals(null)
					&& !edt_pin_3.getText().equals(null)
					&& !edt_pin_4.getText().equals(null)) {
				String pin_1 = edt_pin_1.getText().toString().trim();
				String pin_2 = edt_pin_2.getText().toString().trim();
				String pin_3 = edt_pin_3.getText().toString().trim();
				String pin_4 = edt_pin_4.getText().toString().trim();

				if (!pin_1.equals("") && !pin_2.equals("") && !pin_3.equals("")
						&& !pin_4.equals("")) {
					String pin = pin_1.concat(pin_2).concat(pin_3)
							.concat(pin_4);
					if (pin.equals(String.valueOf(pinSaved))) {
						Toast.makeText(getApplicationContext(),
								"Your screen unlocked", Toast.LENGTH_LONG)
								.show();
						unlocked = true;
						finish();
					} else {
						Toast.makeText(getApplicationContext(), "Wrong PIN",
								Toast.LENGTH_LONG).show();
					}
				}
			}
			break;

		default:
			break;
		}
	}
}
