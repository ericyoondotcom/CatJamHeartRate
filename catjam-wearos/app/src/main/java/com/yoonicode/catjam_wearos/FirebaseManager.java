package com.yoonicode.catjam_wearos;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.SensorManager;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {
    public static FirebaseManager instance;

    FirebaseDatabase database;

    public FirebaseManager(Context context) {
        if(instance == null) {
            instance = this;
        } else {
            return;
        }
        database = FirebaseDatabase.getInstance();
        updateHeartRate(0);
        DatabaseReference accuracyRef = database.getReference().child("users").child("test").child("accuracy");
        accuracyRef.setValue(MainActivity.getContext().getString(R.string.accuracy_measuring));
    }

    public void updateHeartRate(int heartRate) {
        DatabaseReference ref = database.getReference().child("users").child("test").child("heart_rate");
        ref.setValue(heartRate);
    }

    public void updateAccuracy(int accuracy) {
        DatabaseReference ref = database.getReference().child("users").child("test").child("accuracy");
        String value;
        switch(accuracy) {
            case SensorManager.SENSOR_STATUS_UNRELIABLE:
                value = MainActivity.getContext().getString(R.string.accuracy_inaccurate);
                break;
            case SensorManager.SENSOR_STATUS_NO_CONTACT:
                value = MainActivity.getContext().getString(R.string.accuracy_no_contact);
                break;
            default:
                value = "";
                break;
        }
        ref.setValue(value);
    }
}
