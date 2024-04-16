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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseUser;
import com.yoonicode.catjam_wearos.databinding.ActivityMainBinding;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private static Context context; // https://stackoverflow.com/questions/4391720/how-can-i-get-a-resource-content-from-a-static-context/4391811#4391811
    public FirebaseManager firebase;

    private ActivityMainBinding binding;
    private HeartService service;
    private boolean isBound;

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
        binding.signOutButton.setOnClickListener(view -> { signOut(); });
        binding.grantPermissionButton.setOnClickListener(view -> { requestPermission(); });
        binding.signInButton.setOnClickListener(view -> { tryAuthentication(); });

        if(FirebaseManager.instance == null) {
            firebase = new FirebaseManager(this);
        } else {
            firebase = FirebaseManager.instance;
        }
    }

    private void updateUI() {
        boolean hasPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED;
        FirebaseUser user = FirebaseManager.instance.auth.getCurrentUser();
        if(user != null) Log.d("username", user.getDisplayName());
        else Log.d("username", "User is null!");
        if(!hasPermission) {
            binding.noPermission.setVisibility(View.VISIBLE);
            binding.unauthenticated.setVisibility(View.GONE);
            binding.main.setVisibility(View.GONE);
        } else if(user == null) {
            binding.noPermission.setVisibility(View.GONE);
            binding.unauthenticated.setVisibility(View.VISIBLE);
            binding.main.setVisibility(View.GONE);
        } else {
            binding.noPermission.setVisibility(View.GONE);
            binding.unauthenticated.setVisibility(View.GONE);
            binding.main.setVisibility(View.VISIBLE);
            onReadyToTrack();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.BODY_SENSORS }, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            updateUI();
        }
        else {
            Toast.makeText(MainActivity.this, "App will not function without Body Sensors permission", Toast.LENGTH_LONG).show();
        }
    }

    private void tryAuthentication() {
        Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
        startActivity(intent);
    }

    private void onReadyToTrack() {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, HeartService.class);
        context.startService(intent);

        Log.d("bind", "Bind service called");
        isBound = bindService(intent, connection, Context.BIND_AUTO_CREATE);

        new Timer().scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                if(service != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int accuracy = service.getCurrentAccuracy();
                            if(accuracy == SensorManager.SENSOR_STATUS_NO_CONTACT) {
                                binding.hrDisplay.setText(getString(R.string.accuracy_no_contact));
                            } else if(accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
                                binding.hrDisplay.setText(getString(R.string.accuracy_inaccurate));
                            } else {
                                binding.hrDisplay.setText(getString(R.string.heart_rate_display, service.getCurrentHeartRate()));
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
        if(isBound) unbindService(connection);
    }

    private void signOut() {
        FirebaseManager.instance.getAuth().signOut();
        updateUI();
    }

    public static Context getContext() {
        return context;
    }
}