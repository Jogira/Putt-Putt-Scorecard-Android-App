package com.example.minigolfapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                toHome();
            }
        };
        handler.postDelayed(r, 1200);
    }

    public void toHome(){
        Intent homeScreen = new Intent(this, MainActivity.class);
        startActivity(homeScreen);
        this.overridePendingTransition(R.anim.activity_fade_out, R.anim.activity_fade_in);
    }

}