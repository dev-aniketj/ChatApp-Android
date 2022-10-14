package com.aniketjain.chatappjava.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.aniketjain.chatappjava.R;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // setUp the handler for the another activity
        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashScreenActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }, 1000);

    }
}