package com.facebook.fragment_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivty extends AppCompatActivity {

    // Set the duration for which the splash screen will be displayed
    private static final long SPLASH_DURATION = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activty);

        // Use a Handler to delay the execution of code for the specified duration
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent to start the main activity
                Intent intent = new Intent(SplashActivty.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close the splash screen activity
            }
        }, SPLASH_DURATION);
    }
}