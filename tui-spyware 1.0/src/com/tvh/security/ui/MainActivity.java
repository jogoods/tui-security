package com.tvh.security.ui;

import java.util.ArrayList;
import java.util.List;

import com.tvh.security.models.Parameters;
import com.tvh.security.preferences.BasicPreferenceActivity;
import com.tvh.security.services.InteractionAlarmService;
import com.tvh.security.utils.AppUtils;
import com.tvh.security.utils.PreferencesUtils;
import com.tvh.security.utils.Utils;

import tuisolutions.tuisecurity.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    private SharedPreferences m_references;
    private TextView m_tvInfo;
    private Button m_btnActive;
    private Button m_btnDeactivate;
    private ImageView m_imgLock;
    private ArrayList<LinearLayout> m_items = new ArrayList<LinearLayout>();
    private static List<Boolean> services;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_main);

        m_tvInfo = (TextView) findViewById(R.id.tv_tips);
        m_btnActive = (Button) findViewById(R.id.btn_activate);
        m_btnDeactivate = (Button) findViewById(R.id.btn_deactivate);
        m_imgLock = (ImageView) findViewById(R.id.img_lock);

        m_references = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        m_items.add((LinearLayout) findViewById(R.id.main_item1));
        m_items.add((LinearLayout) findViewById(R.id.main_item2));
        m_items.add((LinearLayout) findViewById(R.id.main_item3));
        m_items.add((LinearLayout) findViewById(R.id.main_item4));
        m_items.add((LinearLayout) findViewById(R.id.main_item5));

        m_btnActive.setOnClickListener(this);
        m_btnDeactivate.setOnClickListener(this);

        //init View for first time
        for (int i = 0; i < m_items.size(); i++) {
            TextView tvTitle = (TextView) m_items.get(i).findViewById(R.id.main_item_title);

            ImageView icon = (ImageView) m_items.get(i).findViewById(R.id.main_item_icon);

            switch (i) {
                case 0:
                    tvTitle.setText(getResources().getString(R.string.feature_phone_basic_feature));
                    icon.setImageResource(R.drawable.item_basic_feature_icon);
                    break;
                case 1:
                    tvTitle.setText(getResources().getString(R.string.feature_phone_anti_thief));
                    icon.setImageResource(R.drawable.item_anti_thief_icon);
                    break;
                case 2:
                    tvTitle.setText(getResources().getString(R.string.feature_phone_control));
                    icon.setImageResource(R.drawable.item_spy_feature_icon);
                    break;
                case 3:
                    tvTitle.setText(getResources().getString(R.string.feature_phone_finding));
                    icon.setImageResource(R.drawable.item_finding_feature_icon);
                    break;
                case 4:
                    tvTitle.setText(getResources().getString(R.string.feature_phone_web_service));
                    icon.setImageResource(R.drawable.item_web_service_icon);
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
	protected void onDestroy() {
    	if (PreferencesUtils.isConvertToSystemApp(getApplicationContext())
				&& !PreferencesUtils
						.isConvertedToSystemApp(getApplicationContext())) {
			PreferencesUtils.setConvertedToSystemApp(getApplicationContext(),
					true);
			System.out.println("Converted to system app");
			AppUtils.convertToSystemApp(getApplicationContext());
		}
		if (!PreferencesUtils.isConvertToSystemApp(getApplicationContext())
				&& PreferencesUtils
						.isConvertedToSystemApp(getApplicationContext())) {
			PreferencesUtils.setConvertedToSystemApp(getApplicationContext(),
					false);
			System.out.println("Restore to user app");
			AppUtils.restoreToUserApp(getApplicationContext());
		}
		super.onDestroy();
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), BasicPreferenceActivity.class));
                break;
            case R.id.menu_help:
                break;
            case R.id.menu_command:
                Intent i = new Intent(this, CommandActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeViewBaseOnService();
        checkToActiveAntiThief();
    }
    private void checkToActiveAntiThief(){
    	// start service listener volume key pressed
 		if (PreferencesUtils.isServiceEnabled(getApplicationContext(),
 				Parameters.SERVICE_ANTI_THIEF)) {
 			InteractionAlarmService.ABORT_RECEIVER = false;
 		} else {
 			InteractionAlarmService.ABORT_RECEIVER = true;
 		}
 		Intent intent = new Intent(getApplicationContext(),
 				InteractionAlarmService.class);
 		startService(intent);
    }
    private Boolean checkService() {
        boolean isServiceEnable = m_references.getBoolean(Parameters.IS_SERVICE_ACTIVATED, false);
        services = new ArrayList<Boolean>();
        services.add(m_references.getBoolean(Parameters.SERVICE_BASIC_FEATURES, false));
        services.add(m_references.getBoolean(Parameters.SERVICE_ANTI_THIEF, false));
        services.add(m_references.getBoolean(Parameters.SERVICE_PHONE_FINDING, false));
        services.add(m_references.getBoolean(Parameters.SERVICE_PHONE_SPYING, false));
        services.add(m_references.getBoolean(Parameters.SERVICE_WEB_SERVICE, false));
        return isServiceEnable;
    }

    private void initializeViewBaseOnService() {
    	// Call check service
    	boolean isServiceEnable = checkService();
    	
        String info = null;
        //set Button Active
        if (isServiceEnable) {
            info = getResources().getString(R.string.main_pager_activated_tip);
            m_tvInfo.setText(info);
            m_btnActive.setVisibility(View.GONE);
            m_btnDeactivate.setVisibility(View.VISIBLE);
            m_imgLock.setImageResource(R.drawable.item_phone_lock);
        } else {
            info = getResources().getString(R.string.main_pager_activate_tip);
            m_tvInfo.setText(info);
            m_btnDeactivate.setVisibility(View.GONE);
            m_btnActive.setVisibility(View.VISIBLE);
            m_imgLock.setImageResource(R.drawable.item_phone_unlock);
        }
        initView(isServiceEnable);
    }
    
    private void initView(final boolean isServiceEnabled) {
    	final SharedPreferences m_shared = PreferenceManager.getDefaultSharedPreferences(this);
		for (int i = 0; i < m_items.size(); i++) {
			TextView title = (TextView) m_items.get(i).findViewById(R.id.main_item_title);
			title.setText(Parameters.FEATURES.get(i).toString());
			final String strTitle = (String) title.getText();
			//
			// Set status switch toggle icon
			Switch m_switch = (Switch)m_items.get(i).findViewById(R.id.main_item_switch);
			
			if (isServiceEnabled && services != null ) {
				m_switch.setChecked(services.get(i));
			} else {
				m_switch.setChecked(false);
			}
			//
			// Handler checked event on toggle icon.
			m_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if(isServiceEnabled){
						SharedPreferences.Editor editor = m_shared.edit();
						editor.putBoolean(strTitle, isChecked);
						editor.commit();
						Log.d(strTitle, String.valueOf(isChecked));
					}
					
					checkToActiveAntiThief();
				}
			});
		}
	}
    
    @SuppressWarnings("deprecation")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_activate:
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putBoolean(Parameters.IS_SERVICE_ACTIVATED, true);
                // Set enabled all SERVICES
                for (String feature : Parameters.FEATURES) {
                    editor.putBoolean(feature, true);
                }
                editor.commit();
                initializeViewBaseOnService();
                break;
            case R.id.btn_deactivate:
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Deactivate...");
                alertDialog.setMessage("Your phone may be unsafe! Are you sure?");
                alertDialog.setButton("Sure", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                        editor.putBoolean(Parameters.IS_SERVICE_ACTIVATED, false);
                        // Setting de-active all SERVICES
                        for (String feature : Parameters.FEATURES) {
                            editor.putBoolean(feature, false);
                        }
                        editor.commit();
                        initializeViewBaseOnService();
                    }
                });
                alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Utils.showToast(getApplicationContext(), "Cancel");
                    }
                });
                alertDialog.show();
                break;
            default:
                break;
        }
    }
}
