package tuisolutions.tuisecurity.ui;

import java.util.HashMap;
import java.util.Map;

import tuisolutions.tuisecurity.R;
import tuisolutions.tuisecurity.controllers.SIMController;
import tuisolutions.tuisecurity.models.SIM;
import tuisolutions.tuisecurity.utils.PreferencesUtils;
import tuisolutions.tuisecurity.utils.Utils;
import tuisolutions.tuisecurity.webservice.CallServiceInActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements View.OnClickListener {
	private static final String TAG = LoginActivity.class.getSimpleName();

	private TextView m_editUsername, m_editPassword;
	private Button m_btnLogin;
	private TextView m_tvForgotPassword, m_tvRegister;
	private SIM m_sim;
	public static String TOKEN = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		m_btnLogin = (Button) findViewById(R.id.btnLogin);
		m_editUsername = (EditText) findViewById(R.id.etLoginUsername);
		m_editPassword = (EditText) findViewById(R.id.etLoginPassword);
		m_tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
		m_tvRegister = (TextView) findViewById(R.id.tvRegister);
		m_sim = new SIMController(getApplicationContext()).getSIMInfo();
		Log.v(TAG, "Login Page");

		m_btnLogin.setOnClickListener(this);
		m_tvForgotPassword.setOnClickListener(this);
		m_tvRegister.setOnClickListener(this);
		
	}

	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
		case R.id.btnLogin:
			// Send request to login
			// View send info to server for register
			String username = (!m_editUsername.getText().equals(null)) ? m_editUsername
					.getText().toString().trim()
					: "";
			String password = (!m_editPassword.getText().equals(null)) ? m_editPassword
					.getText().toString().trim()
					: "";
			if (!username.equalsIgnoreCase("")
					&& !password.equalsIgnoreCase("")) {
				PreferencesUtils.setUsernamePassword(getApplicationContext(), username, Utils.md5(password));
				Map<String, String> params = new HashMap<String, String>();
				params.put("username", username);
				params.put("password", Utils.md5(password));
				params.put("serialSIM", m_sim.getSerial());
				new CallServiceInActivity(this, CallServiceInActivity.SERVICE_LOGIN, params)
						.execute();
			} else {
				Toast.makeText(getApplicationContext(),
						"You must enter username and password",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tvRegister:
			// Redirect to register page
			i = new Intent(getApplicationContext(),
					RegisterActivity.class);
			startActivity(i);
			finish();
			break;
		case R.id.tvForgotPassword:
			// Redirect to forgot password page
			i = new Intent(getApplicationContext(),
					ForgotPasswordActivity.class);
			startActivity(i);
			break;
		default:
			break;
		}
	}
}
