package com.tvh.security.ui;

import com.tvh.security.utils.PreferencesUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import tuisolutions.tuisecurity.R;

public class SplashActivity extends Activity {
    private String TAG = SplashActivity.class.getName();

    int splashTime = 2500;
    private Thread splashThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        String[] commands = new String[]{"sysrw", "sysro"};
        splashThread = new Thread() {
            public void run() {
                try {
                    synchronized (this) {
                        wait(splashTime);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    setupFirstRun();
                }
            }
        };
        splashThread.start();
    }

    /**
     * Setup the app when it is first run.
     */
    private void setupFirstRun() {
        Log.v(TAG, "SplashActivity.setupFirstRun()");
        try {
            Intent i;
            if (PreferencesUtils.isFirstRun(getApplicationContext())) {
            	// TODO change activity in first timme
            	i = new Intent(this, SetupHelperActivity.class);
            } else {
                // If not first run, go to PIN input activity
                i = new Intent(this, PinInputActivity.class);
            }
            startActivity(i);
            finish();
        } catch (Exception ex) {
            Log.e(TAG, "SplashActivity.setupFirstRun() ERROR: " + ex.toString());
        }
    }

}
