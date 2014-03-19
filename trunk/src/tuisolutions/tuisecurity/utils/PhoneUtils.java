package tuisolutions.tuisecurity.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;

public class PhoneUtils {
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void setMobileDataEnabled(Context context, boolean enabled) {
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class conmanClass;
        try {
            conmanClass = Class.forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        } catch (ClassNotFoundException e) {
            AppUtils.log("ClassNotFoundException", e);
        } catch (NoSuchFieldException e) {
            AppUtils.log("NoSuchFieldException", e);
        } catch (IllegalArgumentException e) {
            AppUtils.log("IllegalArgumentException", e);
        } catch (IllegalAccessException e) {
            AppUtils.log("IllegalAccessException", e);
        } catch (InvocationTargetException e) {
            AppUtils.log("InvocationTargetException", e);
        } catch (NoSuchMethodException e) {
            AppUtils.log("NoSuchMethodException", e);
        }
    }
    
    public static boolean isDataEnable(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return (telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED);
    }
}
