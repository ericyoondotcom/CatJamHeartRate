package com.example.catjam_wearos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.catjam_wearos.databinding.ActivityMainBinding;

public class MainActivity extends Activity {

    private TextView hrDisplay;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        hrDisplay = binding.hrDisplay;

        Context context = getApplicationContext();
        Intent intent = new Intent(getApplicationContext(), HeartService.class);
        context.startService(intent);

    }
}