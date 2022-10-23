package com.yoonicode.catjam_wearos;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class HeartService extends Service {
    HeartBinder binder = new HeartBinder();
    SensorListener listener;
    SensorManager sensorManager;
    Sensor sensor;

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return;
        CharSequence name = getString(R.string.notification_channel_name);
        String description = getString(R.string.notification_channel_description);
        int importance = NotificationManager.IMPORTANCE_MIN;
        NotificationChannel channel = new NotificationChannel("status", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Intent cancelServiceIntent = new Intent(this, MyReceiver.class);
        cancelServiceIntent.setAction(getString(R.string.stop_service_action));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, cancelServiceIntent, 0);
        Notification notif = new NotificationCompat.Builder(this, "status")
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("CatJamHeartRate is running")
                .setContentText("View the companion website to configure")
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.icon, getString(R.string.cancel_button), pendingIntent)
                .build();
        startForeground(1, notif);

        listener = new SensorListener();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }

    public int getCurrentHeartRate() {
        return listener.rate;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class HeartBinder extends Binder {
        HeartService getService() {
            return HeartService.this;
        }
    }
}

class SensorListener implements SensorEventListener {
    public int rate;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d("Sensor type", "Sensor type: " + sensorEvent.sensor.getType());
        if (sensorEvent.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            Log.d("s", "Sensor changed to: " + sensorEvent.values[0]);
            rate = (int) sensorEvent.values[0];

            FirebaseManager.instance.updateHeartRate(rate);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}