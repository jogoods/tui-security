package com.tvh.security.ui;

import java.util.HashMap;
import java.util.Map;

import com.tvh.security.models.Parameters;
import com.tvh.security.utils.AsteriskPasswordTransformationMethod;
import com.tvh.security.utils.ContactUtils;
import com.tvh.security.utils.PreferencesUtils;
import com.tvh.security.utils.Utils;

import tuisolutions.tuisecurity.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

public class SetupStepActivity extends Activity {

    private ScrollView m_layout_step1;
    private ScrollView m_layout_step2;
    private ScrollView m_layout_step3;
    private Button m_btnNext;
    private Button m_btnPrevious;

    private EditText m_txtNewPin_1;
    private EditText m_txtNewPin_2;
    private EditText m_txtNewPin_3;
    private EditText m_txtNewPin_4;

    private EditText m_txtPinAgain_1;
    private EditText m_txtPinAgain_2;
    private EditText m_txtPinAgain_3;
    private EditText m_txtPinAgain_4;

    private EditText m_txtMobile;
    private ImageView m_imgContact;
    private EditText m_txtMsgReturn;

    private int m_isStep = 1;

    public static final String ISPASS = "pass";
    private String newPin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setup_step);

        // initialize View
        m_btnNext = (Button) findViewById(R.id.btnNext);
        m_btnPrevious = (Button) findViewById(R.id.btnPrevious);
        m_layout_step1 = (ScrollView) findViewById(R.id.setup_step_layout1);
        m_layout_step2 = (ScrollView) findViewById(R.id.setup_step_layout2);
        m_layout_step3 = (ScrollView) findViewById(R.id.setup_step_layout3);

        m_txtNewPin_1 = (EditText) findViewById(R.id.setup_step_txtsetpin_1);
        m_txtNewPin_2 = (EditText) findViewById(R.id.setup_step_txtsetpin_2);
        m_txtNewPin_3 = (EditText) findViewById(R.id.setup_step_txtsetpin_3);
        m_txtNewPin_4 = (EditText) findViewById(R.id.setup_step_txtsetpin_4);
        m_txtPinAgain_1 = (EditText) findViewById(R.id.setup_step_txtsetpin_again_1);
        m_txtPinAgain_2 = (EditText) findViewById(R.id.setup_step_txtsetpin_again_2);
        m_txtPinAgain_3 = (EditText) findViewById(R.id.setup_step_txtsetpin_again_3);
        m_txtPinAgain_4 = (EditText) findViewById(R.id.setup_step_txtsetpin_again_4);

        m_txtNewPin_1.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        m_txtNewPin_2.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        m_txtNewPin_3.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        m_txtNewPin_4.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        m_txtPinAgain_1.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        m_txtPinAgain_2.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        m_txtPinAgain_3.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        m_txtPinAgain_4.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        m_txtMobile = (EditText) findViewById(R.id.txtMobileReturn);
        m_txtMsgReturn = (EditText) findViewById(R.id.txtMsgReturn);
        m_imgContact = (ImageView) findViewById(R.id.imgChooseContact);
        m_imgContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        m_btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (m_isStep == 1) {
                    String condition = checkCondition(m_isStep);
                    if (ISPASS.equals(condition)) {
                        m_layout_step1.setVisibility(View.GONE);
                        m_layout_step2.setVisibility(View.VISIBLE);
                        m_isStep = 2;
                    } else {
                        showInfo(condition);
                    }

                } else if (m_isStep == 2) {
                    String condition = checkCondition(m_isStep);
                    if (ISPASS.equals(condition)) {
                        m_layout_step2.setVisibility(View.GONE);
                        m_layout_step3.setVisibility(View.VISIBLE);
                        m_isStep = 3;
                        String finishStr = getResources().getString(R.string.finish);
                    	m_btnNext.setText(finishStr);
                    	
                    } else {
                        showInfo(condition);
                    }

                } else if (m_isStep == 3) {
                    if (PreferencesUtils.isFirstRun(getApplicationContext())) {
                        // save preference when the apps is setup
                        Map<String, Object> preferences = new HashMap<String, Object>();
                        preferences.put(Parameters.IS_FIRST_RUN, false);
                        PreferencesUtils.savePreferences(getApplicationContext(), preferences);
                    }
                    commit();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        m_btnPrevious.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (m_isStep == 1) {
                    if (PreferencesUtils.isFirstRun(getApplicationContext())) {
                        Utils.showToast(getApplicationContext(), "You must set-up apps first");
                    } else {
                        finish();
                    }
                } else if (m_isStep == 2) {
                    String nextStr = getResources().getString(R.string.next);
                    m_btnNext.setText(nextStr);
                    m_layout_step2.setVisibility(View.GONE);
                    m_layout_step1.setVisibility(View.VISIBLE);
                    m_isStep = 1;
                } else if (m_isStep == 3) {
                    m_layout_step3.setVisibility(View.GONE);
                    m_layout_step2.setVisibility(View.VISIBLE);
                    m_isStep = 2;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        m_txtNewPin_1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    m_txtNewPin_2.requestFocus();
                    m_txtNewPin_2.selectAll();
                }
            }
        });
        m_txtNewPin_2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    m_txtNewPin_3.requestFocus();
                    m_txtNewPin_3.selectAll();
                }
            }
        });
        m_txtNewPin_3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    m_txtNewPin_4.requestFocus();
                    m_txtNewPin_4.selectAll();
                }
            }
        });

        m_txtNewPin_4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    m_txtPinAgain_1.requestFocus();
                    m_txtPinAgain_1.selectAll();
                }
            }
        });
        m_txtPinAgain_1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    m_txtPinAgain_2.requestFocus();
                    m_txtPinAgain_2.selectAll();
                }
            }
        });
        m_txtPinAgain_2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    m_txtPinAgain_3.requestFocus();
                    m_txtPinAgain_3.selectAll();
                }
            }
        });
        m_txtPinAgain_3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    m_txtPinAgain_4.requestFocus();
                    m_txtPinAgain_4.selectAll();
                }
            }
        });
    }

    private void showInfo(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void commit() {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean(Parameters.IS_SERVICE_ACTIVATED, true);
        editor.putBoolean(Parameters.IS_SET_SETTINGS, true);

        if (!newPin.equals("")) {
            editor.putInt(Parameters.SETTINGS_PIN, Integer.valueOf(newPin));
        }
        editor.putString(Parameters.SETTINGS_NUMBER_RETURN, m_txtMobile.getText().toString());
        editor.putString(Parameters.SETTINGS_MSG_RETURN, m_txtMsgReturn.getText().toString());

        editor.putBoolean(Parameters.SERVICE_BASIC_FEATURES, true);
        editor.putBoolean(Parameters.SERVICE_ANTI_THIEF, true);
        editor.putBoolean(Parameters.SERVICE_PHONE_FINDING, true);
        editor.putBoolean(Parameters.SERVICE_PHONE_SPYING, false);
        editor.putBoolean(Parameters.SERVICE_WEB_SERVICE, false);

        editor.commit();
    }

    private String checkCondition(int isStep) {

        if (isStep == 1) {
            if (!m_txtNewPin_1.getText().equals(null) && !m_txtNewPin_2.getText().equals(null) && !m_txtNewPin_3.getText().equals(null)
                    && !m_txtNewPin_4.getText().equals(null) && !m_txtPinAgain_1.getText().equals(null) && !m_txtPinAgain_2.getText().equals(null)
                    && !m_txtPinAgain_3.getText().equals(null) && !m_txtPinAgain_4.getText().equals(null)) {
                String newPin_1 = m_txtNewPin_1.getText().toString().trim();
                String newPin_2 = m_txtNewPin_2.getText().toString().trim();
                String newPin_3 = m_txtNewPin_3.getText().toString().trim();
                String newPin_4 = m_txtNewPin_4.getText().toString().trim();

                String newPinAgain_1 = m_txtPinAgain_1.getText().toString().trim();
                String newPinAgain_2 = m_txtPinAgain_2.getText().toString().trim();
                String newPinAgain_3 = m_txtPinAgain_3.getText().toString().trim();
                String newPinAgain_4 = m_txtPinAgain_4.getText().toString().trim();

                if (!newPin_1.equals("") && !newPin_2.equals("") && !newPin_3.equals("") && !newPin_4.equals("")) {
                    newPin = newPin_1.concat(newPin_2).concat(newPin_3).concat(newPin_4);
                } else {
                    return "You must enter your PIN code.";
                }

                String newPinAgain = "";

                if (!newPinAgain_1.equals("") && !newPinAgain_2.equals("") && !newPinAgain_3.equals("") && !newPinAgain_4.equals("")) {
                    newPinAgain = newPinAgain_1.concat(newPinAgain_2).concat(newPinAgain_3).concat(newPinAgain_4);
                } else {
                    return "PIN code and PIN code confirm must same together with.";
                }

                // Compare newPin and newPinAgain
                if (newPin.equals(newPinAgain)) {
                    return ISPASS;
                } else {
                    return "PIN code and PIN code confirm must same together with.";
                }
            }
        } else if (isStep == 2) {
            if (!("".equals(m_txtMobile.getText().toString()))) {
                if (!("".equals(m_txtMsgReturn.getText().toString()))) {
                    return ISPASS;
                } else {
                    return "You must enter name.";
                }

            } else {
                return "You must enter phone number.";
            }
        }

        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        com.tvh.security.models.Phone number = null;
        try {
            if (resultCode == Activity.RESULT_OK) {
                Uri contactData = data.getData();
                Cursor cur = managedQuery(contactData, null, null, null, null);
                while (cur.moveToNext()) {
                    String contactId = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    number = ContactUtils.getPhoneNumbers(
							getApplicationContext(), contactId);
                }
                m_txtMobile.setText(number.getNumber());
                // cur.close();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            Log.e("IllegalArgumentException :: ", e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error : ", e.toString());
        }
    }
}
