package com.tvh.security.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

public class SecureApp {
    Context context;

    public SecureApp(Context context) {
        this.context = context;
    }

    /**
     * This method allow hide app icon, but app is not kill
     */
    public void hideAppIcon() {
        ComponentName componentToDisable = new ComponentName(
                "tuisolutions.tuisecurity", "tuisolutions.tuisecurity.ui.SplashActivity");
        /**
         * getPackageManager().setApplicationEnabledSetting(packageName,
         * newState, flags) When we use this method, all function of app will
         * kill (service, receiver, thread, activity, ...).
         *
         * So, we should use getPackageManager().setComponentEnabledSetting
         */
        context.getPackageManager().setComponentEnabledSetting(
                componentToDisable,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
        Log.d("hide icon", "OK");
    }

    /**
     * This method allow show app icon
     */
    public void showAppIcon() {
        ComponentName componentToDisable = new ComponentName(
                "tuisolutions.tuisecurity", "tuisolutions.tuisecurity.ui.SplashActivity");
        context.getPackageManager().setComponentEnabledSetting(
                componentToDisable,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        Log.d("show icon", "OK");
        /**
         * Re-setting preferences hide app icon in secure mode
         */
        PreferencesUtils.setHideAppIcon(context, false);
    }

}
