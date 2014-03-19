package tuisolutions.tuisecurity.preferences;

import java.io.File;

import tuisolutions.tuisecurity.R;
import tuisolutions.tuisecurity.services.SecureAppService;
import tuisolutions.tuisecurity.utils.FileUtils;
import tuisolutions.tuisecurity.utils.PreferencesUtils;
import tuisolutions.tuisecurity.utils.Utils;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class BasicPreferenceActivity extends PreferenceActivity {
    
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.addPreferencesFromResource(R.xml.preferences);
        this.setContentView(R.layout.basic_preferences);
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
        if(PreferencesUtils.isHideAppIcon(getApplicationContext())){
        	Intent intent = new Intent(getApplicationContext(), SecureAppService.class);
        	intent.putExtra(SecureAppService.SECURE_APP_TYPE, SecureAppService.SECURE_APP_TYPE_HIDE_ICON);
            getApplicationContext().startService(intent);
        }
        
        // TODO
        // Show alert dialog if has convert to system app setting checked.
    }
}
