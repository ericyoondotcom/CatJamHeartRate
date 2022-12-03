package com.yoonicode.catjam_wearos;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.SensorManager;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {
    public static FirebaseManager instance;

    FirebaseDatabase database;
    FirebaseAuth auth;

    public FirebaseManager(Context context) {
        if(instance == null) {
            instance = this;
        } else {
            return;
        }
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        updateHeartRate(0);
        updateAccuracy(-420);
    }

    public void updateHeartRate(int heartRate) {
        UserInfo currentUser = auth.getCurrentUser();
        if(currentUser == null) return;

        DatabaseReference ref = database.getReference().child("users").child(currentUser.getUid()).child("heart_rate");
        ref.setValue(heartRate);
    }

    public void updateAccuracy(int accuracy) {
        UserInfo currentUser = auth.getCurrentUser();
        if(currentUser == null) return;

        DatabaseReference ref = database.getReference().child("users").child(currentUser.getUid()).child("accuracy");
        String value;
        switch(accuracy) {
            case -420:
                value = "Measuring...";
                break;
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

    public FirebaseDatabase getDatabase() { return database; }
    public FirebaseAuth getAuth() { return auth; }
}
