package com.tvh.security.ui;

import com.tvh.security.models.CommandType;
import com.tvh.security.services.SecureAppService;
import com.tvh.security.utils.AsteriskPasswordTransformationMethod;
import com.tvh.security.utils.PreferencesUtils;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import tuisolutions.tuisecurity.R;

public class PinInputActivity extends Activity implements OnClickListener {
    private Button btnSubmit;
    private EditText edPin_1, edPin_2, edPin_3, edPin_4;
    private TextView tvForgotPin;

    public static boolean isLoggedIn = false;

    private static int countLoginFailed = 0;
    private static boolean isAlarming = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_input);
        edPin_1 = (EditText) findViewById(R.id.pin_input_edpin_1);
        edPin_2 = (EditText) findViewById(R.id.pin_input_edpin_2);
        edPin_3 = (EditText) findViewById(R.id.pin_input_edpin_3);
        edPin_4 = (EditText) findViewById(R.id.pin_input_edpin_4);

        edPin_1.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        edPin_2.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        edPin_3.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        edPin_4.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        btnSubmit = (Button) findViewById(R.id.pin_input_btn_submit);
        tvForgotPin = (TextView) findViewById(R.id.pin_input_forgot_pin);

        btnSubmit.setOnClickListener(this);
        tvForgotPin.setOnClickListener(this);

        isLoggedIn = false;
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        edPin_1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    edPin_2.requestFocus();
                    edPin_2.selectAll();
                }
            }
        });
        edPin_2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    edPin_3.requestFocus();
                    edPin_3.selectAll();
                }
            }
        });
        edPin_3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    edPin_4.requestFocus();
                    edPin_4.selectAll();
                }
            }
        });
    }

    public void onClick(View v) {
        Intent i = null;
        switch (v.getId()) {
            case R.id.pin_input_btn_submit:
                // Check in preferences
                if (PreferencesUtils.isFirstRun(getApplicationContext())) {
                	// TODO change activity in firstime
                	 i = new Intent(this, MainActivity.class);
                } else {
                    // If not first run, check pin saved
                    int pinSaved = PreferencesUtils
                            .getPINCode(getApplicationContext());
                    if (!edPin_1.getText().equals(null)
                            && !edPin_2.getText().equals(null)
                            && !edPin_3.getText().equals(null)
                            && !edPin_4.getText().equals(null)) {
                        String pin_1 = edPin_1.getText().toString().trim();
                        String pin_2 = edPin_2.getText().toString().trim();
                        String pin_3 = edPin_3.getText().toString().trim();
                        String pin_4 = edPin_4.getText().toString().trim();
                        if (!pin_1.equals("") && !pin_2.equals("")
                                && !pin_3.equals("") && !pin_4.equals("")) {
                            String pin = pin_1.concat(pin_2).concat(pin_3)
                                    .concat(pin_4);
                            if (pin.equals(String.valueOf(pinSaved))) {
                                isLoggedIn = true;
                                i = new Intent(this, MainActivity.class);

                                // Reset countLoginFailed = 0
                                countLoginFailed = 0;
                                isAlarming = false;
                            } else {
                                // Make Toast
                                Toast.makeText(getApplicationContext(),
                                        "PIN incorrect", Toast.LENGTH_SHORT).show();
                                // Count up countLoginFailed parameter
                                countLoginFailed++;
                            }
                            /**
                             * If countLoginFailed > limit threshold setting
                             */
                            //System.out.println(PreferencesUtils.getSecureThreshold(getApplicationContext()));
                            if (countLoginFailed > PreferencesUtils.getSecureThreshold(getApplicationContext())) {
                                if (!isAlarming) {
                                    System.out.println("start alarm");
                                    Intent intent = new Intent(getApplicationContext(), SecureAppService.class);
                                    intent.putExtra(SecureAppService.SECURE_APP_TYPE, CommandType.COMMAND_ALARM_LOCK.getCommand());
                                    intent.putExtra(SecureAppService.ALARM_AND_LOCK, false);
                                    getApplicationContext().startService(intent);
                                }
                                isAlarming = true;
                            }
                        }
                    }
                }

                break;
            case R.id.pin_input_forgot_pin:
            	 i = new Intent(this, SetupStepActivity.class);
                break;
        }
        if (i != null) {
            startActivity(i);
            finish();
        }
    }
}
