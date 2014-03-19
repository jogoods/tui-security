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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener, TextWatcher {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    
    private TextView m_editUsername, m_editPassword, m_editPasswordAgain, m_editEmail;
    private Button m_btnRegister;
    private TextView m_tvValidateEmail;
    private SIM m_sim;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        m_btnRegister = (Button) findViewById(R.id.btnRegister);
        m_editUsername = (EditText) findViewById(R.id.etRegisterUsername);
        m_editPassword = (EditText) findViewById(R.id.etRegisterPassword);
        m_editPasswordAgain = (EditText) findViewById(R.id.etRegisterPasswordAgain);
        m_editEmail = (EditText) findViewById(R.id.etRegisterEmail);
        m_tvValidateEmail = (TextView) findViewById(R.id.tvValidateEmail);
        m_sim = new SIMController(getApplicationContext()).getSIMInfo();
                
        m_btnRegister.setOnClickListener(this);
        m_editEmail.addTextChangedListener(this);
    }
    
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                // View send info to server for register
                String username = (!m_editUsername.getText().equals(null)) ? m_editUsername.getText().toString().trim() : "";
                String password = (!m_editPassword.getText().equals(null)) ? m_editPassword.getText().toString().trim() : "";
                String confirmPassword = (!m_editPasswordAgain.getText().equals(null)) ? m_editPasswordAgain.getText().toString().trim() : "";
                
                String email = (!m_editEmail.getText().equals(null)) ? m_editEmail.getText().toString().trim() : "";
                
                if (!username.equalsIgnoreCase("") && !password.equalsIgnoreCase("") && !email.equalsIgnoreCase("")) {
                    if (!m_tvValidateEmail.getText().toString().equalsIgnoreCase("Invalid email")) {
                        if (!password.equals(confirmPassword)) {
                            Toast.makeText(getApplicationContext(), "Password and confirm password do not match.", Toast.LENGTH_LONG).show();
                        } else {
                        	PreferencesUtils.setUsernamePassword(getApplicationContext(), username, Utils.md5(password));
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("username", username);
                            params.put("password", Utils.md5(password));
                            params.put("email", email);
                            params.put("serialSIM", m_sim.getSerial().toString());
                            new CallServiceInActivity(this, CallServiceInActivity.SERVICE_REGISTER, params).execute();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_LONG).show();
                    }
                    
                } else {
                    // Show Toast message
                    Toast.makeText(getApplicationContext(), "You must fill out the form above.", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
    
    public void afterTextChanged(Editable s) {
        if (m_editEmail.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && s.length() > 0) {
            m_tvValidateEmail.setText("");
        } else if (!m_editEmail.getText().toString().equals("")) {
            m_tvValidateEmail.setText("Invalid email");
        } else {
            m_tvValidateEmail.setText("");
        }
    }
    
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub
        
    }
    
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub
        
    }
}
