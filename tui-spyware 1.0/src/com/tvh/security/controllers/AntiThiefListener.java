package com.tvh.security.controllers;

import java.util.Date;

import com.tvh.security.controllers.AccelerationManager.AccelerationCallback;
import com.tvh.security.models.CommandType;
import com.tvh.security.services.SecureAppService;
import com.tvh.security.utils.Fusion;
import com.tvh.security.utils.PreferencesUtils;
import com.tvh.security.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.util.FloatMath;

public class AntiThiefListener implements AccelerationCallback {
    public static boolean isActivated = false;
    private static AccelerationManager accelerationManagger = null;
    private static Context context;
    long tempTime, startRecordingTime;
    private Fusion lastFusion, nowFusion;
    private long lastTime, currentTime;

    public AntiThiefListener(Context context) {
        AntiThiefListener.context = context;
    }

    public static void stopListener(boolean isAlarm) {
        if (accelerationManagger != null) {
            accelerationManagger.stopListening();
            accelerationManagger = null;
            isActivated = false;
            if (isAlarm)
                startAlarming(context);
            else {
                System.out.println("Deactive");
            }
        }

    }

    private static void startAlarming(Context context) {
        System.out.println("start alarm");
        Intent intent = new Intent(context, SecureAppService.class);
        intent.putExtra(SecureAppService.SECURE_APP_TYPE,
                CommandType.COMMAND_ALARM_LOCK.getCommand());
        intent.putExtra(SecureAppService.ALARM_AND_LOCK, true);
        context.startService(intent);
    }

    public void startListener() {
        lastFusion = new Fusion();
        nowFusion = new Fusion();
        if (accelerationManagger == null) {
            System.out.println("Create listener");
            accelerationManagger = new AccelerationManager(context);
            accelerationManagger.setAccelerationCallback(this);
        }
        accelerationManagger.startListening();
    }

    public void onAccelerationUpdate(Fusion fusion, SensorEvent event) {
        float x = fusion.getX();
        float y = fusion.getY();
        float z = fusion.getZ();
        nowFusion.setX(FloatMath.floor(x));
        nowFusion.setY(FloatMath.floor(y));
        nowFusion.setZ(FloatMath.floor(z));
        currentTime = new Date().getTime();
        // Lastime, which two fusion is same
        if (!isActivated && !Utils.compareFusion(lastFusion, nowFusion)) {
            lastFusion.setX(FloatMath.floor(x));
            lastFusion.setY(FloatMath.floor(y));
            lastFusion.setZ(FloatMath.floor(z));
            isActivated = true;
            lastTime = currentTime;
            System.out.println("Start count!");
        }
        if (Utils.compareFusion(lastFusion, nowFusion)) {
            lastTime = currentTime;
        }
        //
        // Stop when has interaction on device
        // Check if delta delay time (between current time and last time) >
        // limit time ==> has interaction on device ==> stop
        //
        if (isActivated
                && (currentTime - lastTime) > PreferencesUtils
                .getInteractionAllowTime(context)) {
            stopListener(true);
        }
    }
}