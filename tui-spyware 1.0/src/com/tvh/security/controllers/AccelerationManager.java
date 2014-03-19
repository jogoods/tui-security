package com.tvh.security.controllers;

import com.tvh.security.utils.Fusion;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class AccelerationManager {

    public interface AccelerationCallback {
        public abstract void onAccelerationUpdate(Fusion fusion, SensorEvent event);
    }

    private static SensorManager sensorManager = null;
    private static SensorEventListener sensorListener = null;
    private static AccelerationCallback accCallback = null;
    private static Context context;

    public static final float NOISE = 2.5f;

    public AccelerationManager(final Context ctx) {
        context = ctx;

        AccelerationManager.sensorListener = new SensorEventListener() {

            public void onSensorChanged(SensorEvent event) {
                if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
                    return;
                }

                switch (event.sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        if (accCallback != null) {
                            Fusion fusion = extractFusion(event);
                            accCallback.onAccelerationUpdate(fusion, event);
                        }
                        break;
                }
            }

            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    public AccelerationCallback getAccelerationCallback() {
        return accCallback;
    }

    public void setAccelerationCallback(final AccelerationCallback callback) {
        accCallback = callback;
    }

    public void startListening() {
        startListening(context);
    }

    public void startListening(final Context context) {
        // Init sensor manager
        if (sensorManager == null) {
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }

        // Register events
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stopListening() {
        if (sensorManager != null && sensorListener != null) {
            sensorManager.unregisterListener(sensorListener);
            sensorManager = null;
        }
    }

    private Fusion extractFusion(SensorEvent event) {
        return new Fusion(clearNoise(event.values[0]), clearNoise(event.values[1]), clearNoise(event.values[2]));
    }

    private float clearNoise(float x) {
        if (Math.abs(x) > NOISE) {
            return x;
        }
        return 0.0f;
    }
}
