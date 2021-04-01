package com.minigolf.puttpoints;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();

        UserPreferencesManager manager = new UserPreferencesManager(this);

        final Handler handler = new Handler();
        final Runnable r;

        if(manager.isFirstLaunch()) {
            manager.updateFirstLaunch(false);
            r = new Runnable() {
                public void run() {
                    toFirstLaunch();
                }
            };
        }
        else {
            r = new Runnable() {
                public void run() {
                    toHome();
                }
            };
        }
        handler.postDelayed(r, 1200);
    }

    public void toHome(){
        Intent homeScreen = new Intent(this, MainActivity.class);
        startActivity(homeScreen);
        this.overridePendingTransition(R.anim.activity_fade_out, R.anim.activity_fade_in);
    }

    public void toFirstLaunch(){
        Intent firstLaunch = new Intent(this, FirstLaunchActivity.class);
        startActivity(firstLaunch);
        this.overridePendingTransition(R.anim.activity_fade_out, R.anim.activity_fade_in);
    }

}