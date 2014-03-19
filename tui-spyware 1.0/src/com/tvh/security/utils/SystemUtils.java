package com.tvh.security.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

final public class SystemUtils {
    public static final String TabLine = "\t";
    public static final String EndLine = "\r\n";
    public static final SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy.MM.dd");
    public static final SimpleDateFormat DateTimeFormat = new SimpleDateFormat("yyyy.MM.dd HH-mm-ss");

    private static final int getTotalForFile(String path) {
        final StatFs sf = new StatFs(path);
        final int blocks = sf.getBlockCount();
        final int blockSize = sf.getBlockSize();
        final int totalBytes = blocks * blockSize;
        return totalBytes;
    }

    private static final int getSpaceForFile(String path) {
        final StatFs sf = new StatFs(path);
        final int blocks = sf.getAvailableBlocks();
        final int blockSize = sf.getBlockSize();
        final int totalBytes = blocks * blockSize;
        return totalBytes;
    }

    public static final int getExternalTotalSpace() {
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        final int mbytes = getTotalForFile(path);
        return mbytes;
    }

    public static final int getInternalTotalSpace() {
        final String path = Environment.getDataDirectory().getAbsolutePath();
        final int mbytes = getTotalForFile(path);
        return mbytes;
    }

    public static final int getExternalFreeSpace() {
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        final int mbytes = getSpaceForFile(path);
        return mbytes;
    }

    public static final int getInternalFreeSpace() {
        final String path = Environment.getDataDirectory().getAbsolutePath();
        final int mbytes = getSpaceForFile(path);
        return mbytes;
    }

    public static final boolean isSDCardMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static final boolean isExternal(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        final int flags = applicationInfo.flags;
        final boolean isExternal = (flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE;
        return isExternal;
    }

//	public static final String getAppName(Context context)
//	{	
//		final Resources resources = context.getResources();
//		return resources.getString(R.string.app_name);
//	}

    public static final String getDeviceModel() {
        return Build.DEVICE;
    }

    public static final String getRussianNumber(String number) {
        if ((number != null) && (number.length() >= 2)) {
            return number.replace("+7", "8");
        }
        return number;
    }

    public static final String getRoundDouble(double value, int scale) {
        BigDecimal decimal = new BigDecimal(value);
        return String.valueOf(decimal.setScale(scale, BigDecimal.ROUND_HALF_EVEN));
    }

    public static final File safeCreateFile(Context context, String path, String fileName) throws IOException {
        File folder = new File(path);
        if (!folder.exists() && !folder.mkdirs()) {
            Log.e(SystemUtils.class.getName(), "Thuong Le: " + folder.getAbsolutePath());
            throw new IOException("Thuong LE: " + path);
        }

        final float total = getTotalForFile(folder.getAbsolutePath());
        final float free = getSpaceForFile(folder.getAbsolutePath());
        final float used = (free / total) * 100;

        if (used > 85) {
            Log.e(SystemUtils.class.getName(), "THoung Le");
            throw new IOException("THuong LE");
        }

        File file = new File(path, fileName);
        if (!file.exists() && !file.createNewFile()) {
            Log.e(SystemUtils.class.getName(), "THoung Le: " + file.getAbsolutePath());
            throw new IOException("Tuong LE: " + file.getAbsolutePath());
        }
        return file;
    }
}
