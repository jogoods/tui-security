package tuisolutions.tuisecurity.ui;

import java.util.HashMap;
import java.util.Map;

import tuisolutions.tuisecurity.R;
import tuisolutions.tuisecurity.webservice.CallServiceInActivity;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotPasswordActivity extends Activity implements
		OnClickListener, TextWatcher {

	private static final String TAG = ForgotPasswordActivity.class
			.getSimpleName();

	private TextView m_editEmail;
	private Button m_btnSend;
	private TextView m_tvValidateEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);

		m_btnSend = (Button) findViewById(R.id.btnRetrievePassword);
		m_editEmail = (EditText) findViewById(R.id.etForgotEmail);
		m_tvValidateEmail = (TextView) findViewById(R.id.tvForgotValidateEmail);
		Log.v(TAG, "Forgot Password Page");

		m_btnSend.setOnClickListener(this);
		m_editEmail.addTextChangedListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRetrievePassword:
			// Send request to get new password
			String email = (!m_editEmail.getText().equals(null)) ? m_editEmail
					.getText().toString().trim() : "";
			if (!m_tvValidateEmail.getText().toString()
					.equalsIgnoreCase("Invalid email")) {
				Map<String, String> params = new HashMap<String, String>();
				params.put("email", email);
				new CallServiceInActivity(this, CallServiceInActivity.SERVICE_FORGOT_PASSWORD,
						params).execute();
			} else {
				Toast.makeText(getApplicationContext(), "Invalid email",
						Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
	}

	public void afterTextChanged(Editable s) {
		if (m_editEmail.getText().toString()
				.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
				&& s.length() > 0) {
			m_tvValidateEmail.setText("");
		} else if (!m_editEmail.getText().toString().equals("")) {
			m_tvValidateEmail.setText("Invalid email");
		} else{
			m_tvValidateEmail.setText("");
		}
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}
}
