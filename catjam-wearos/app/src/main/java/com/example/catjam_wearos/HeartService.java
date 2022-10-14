package com.example.catjam_wearos;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class HeartService extends Service {
    IBinder binder;

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
        Log.d("svc", "onStartCommand");
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
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
