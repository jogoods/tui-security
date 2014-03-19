package tuisolutions.tuisecurity.preferences;

import tuisolutions.tuisecurity.R;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

public class AccountSettingPreference extends DialogPreference {
    private EditText m_txtUsername, m_txtPassword;
    private String m_username, m_password;
    
    public AccountSettingPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.account_settings_preference);
    }
    
    public AccountSettingPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDialogLayoutResource(R.layout.account_settings_preference);
    }
    
    @Override
    protected View onCreateDialogView() {
        View root = super.onCreateDialogView();
        m_txtUsername = (EditText) root.findViewById(R.id.txt_account_user);
        m_txtPassword = (EditText) root.findViewById(R.id.txt_account_password);
        return root;
    }
    
    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        m_txtUsername.setText(m_username);
        m_txtPassword.setText(m_password);
    }
    
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                m_username = m_txtUsername.getText().toString();
                m_password = m_txtPassword.getText().toString();
                callChangeListener(m_username);
                callChangeListener(m_password);
                break;
        }
        super.onClick(dialog, which);
    }
    
    public String getUsername() {
        return m_username;
    }
    
    public void setUsername(String username) {
        this.m_username = username;
    }
    
    public String getPassword() {
        return m_password;
    }
    
    public void setPassword(String password) {
        this.m_password = password;
    }
}
