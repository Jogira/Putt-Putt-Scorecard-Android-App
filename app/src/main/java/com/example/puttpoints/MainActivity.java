package com.example.puttpoints;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    //Takes a given view and animation ID and starts an animation
    private void playAnimation(View v, int animationId) {
        if(v != null) {
            Animation animation = AnimationUtils.loadAnimation(this, animationId);
            animation.setDuration(animation.getDuration());
            animation.setFillAfter(true);
            v.startAnimation(animation);
        }
    }
}