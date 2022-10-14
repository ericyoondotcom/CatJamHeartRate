package com.yoonicode.catjam_wearos;

import android.content.Context;

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
    }

    public void updateHeartRate(int heartRate) {
        DatabaseReference ref = database.getReference().child("users").child("test").child("heart_rate");
        ref.setValue(heartRate);
    }
}
