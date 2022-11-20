package com.yoonicode.catjam_wearos;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yoonicode.catjam_wearos.databinding.ActivityMainBinding;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private static Context context; // https://stackoverflow.com/questions/4391720/how-can-i-get-a-resource-content-from-a-static-context/4391811#4391811

    public FirebaseManager firebase;

    private TextView hrDisplay;
    private ActivityMainBinding binding;
    private HeartService service;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.d("s", "Service connected");
            HeartService.HeartBinder heartBinder = (HeartService.HeartBinder) binder;
            service = heartBinder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        hrDisplay = binding.hrDisplay;

        Context context = getApplicationContext();
        Intent intent = new Intent(context, HeartService.class);
        context.startService(intent);

        firebase = new FirebaseManager(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.BODY_SENSORS }, 1);
        } else {
            onReadyToTrack();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onReadyToTrack();
        }
        else {
            Toast.makeText(MainActivity.this, "App will not function without Body Sensors permission", Toast.LENGTH_LONG) .show();
        }
    }

    private void onReadyToTrack() {
        Intent intent = new Intent(getApplicationContext(), HeartService.class);
        Log.d("bind", "Bind service called");
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                if(service != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int accuracy = service.getCurrentAccuracy();
                            if(accuracy == SensorManager.SENSOR_STATUS_NO_CONTACT) {
                                hrDisplay.setText(getString(R.string.accuracy_no_contact));
                            } else if(accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
                                hrDisplay.setText(getString(R.string.accuracy_inaccurate));
                            } else {
                                hrDisplay.setText(service.getCurrentHeartRate() + " bpm");
                            }
                        }
                    });
                }
            }
        }, 0, 500);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
    }

    public static Context getContext() {
        return context;
    }
}