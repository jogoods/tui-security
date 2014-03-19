package com.tvh.security.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import tuisolutions.tuisecurity.R;

public class SetupHelperActivity extends Activity {
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setup_helper);
        btnContinue = (Button) findViewById(R.id.btn_setup_continue);

        btnContinue.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        SetupStepActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
