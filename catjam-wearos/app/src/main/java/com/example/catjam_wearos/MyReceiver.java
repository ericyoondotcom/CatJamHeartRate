package com.example.catjam_wearos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Resources rsc = context.getResources();
        Log.d("c", "on receive, " + intent.getAction());
        if(intent.getAction().equals(rsc.getString(R.string.stop_service_action))) {
            Log.d("c", "Received a stop service action");
            Intent serviceIntent = new Intent(context, HeartService.class);
            context.stopService(serviceIntent);
        }
    }
}
