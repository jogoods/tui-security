package com.tvh.security.preferences;

import java.io.File;
import java.io.IOException;

import com.tvh.security.models.CommandType;
import com.tvh.security.services.SecureAppService;
import com.tvh.security.utils.FileUtils;
import com.tvh.security.utils.PreferencesUtils;
import com.tvh.security.utils.Utils;

import tuisolutions.tuisecurity.R;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class BasicPreferenceActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.addPreferencesFromResource(R.xml.preferences);
        this.setContentView(R.layout.basic_preferences);
        final CheckBoxPreference isRootedCheck = (CheckBoxPreference) getPreferenceManager().findPreference(getResources().getString(R.string.run_command_as_root));
        isRootedCheck.setSelectable(Utils.isRootedPhone());
        isRootedCheck.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			public boolean onPreferenceClick(Preference preference) {
				if(isRootedCheck.isChecked()){
					try {
						Runtime.getRuntime().exec("su");
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
				}
				return false;
			}
		});
        final CheckBoxPreference isConvertToSystemApp = (CheckBoxPreference) getPreferenceManager().findPreference(getResources().getString(R.string.convert_to_system_app));
        isConvertToSystemApp.setSelectable(Utils.isRootedPhone());
        isConvertToSystemApp.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			public boolean onPreferenceClick(Preference preference) {
				if(isConvertToSystemApp.isChecked()){
					try {
						Runtime.getRuntime().exec("su");
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
				}
				return false;
			}
		});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String pathToSave = PreferencesUtils.getPathSaveFile(getApplicationContext());
        File file = new File(pathToSave);
        if (!file.exists()) {
            if (FileUtils.mkdir(file)) {
                Utils.showToast(getApplicationContext(), "Create path successful");
            } else {
                Utils.showToast(getApplicationContext(), "Create path unsuccessful");
            }
        }

        // Setting if has hide app icon setting checked.
        if (PreferencesUtils.isHideAppIcon(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), SecureAppService.class);
            intent.putExtra(SecureAppService.SECURE_APP_TYPE, CommandType.COMMAND_HIDE_APP_ICON.getCommand());
            getApplicationContext().startService(intent);
        }

        // TODO
        // Show alert dialog if has convert to system app setting checked.
    }
}
