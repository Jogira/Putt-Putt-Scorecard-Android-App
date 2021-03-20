package com.example.minigolfapp;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

abstract class AnimationController {


    public static void playAnimation(final Context context, final View v, final int animationId, int delayMS) {
        if(v != null) {
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(context, animationId);
                    animation.setDuration(animation.getDuration());
                    animation.setFillAfter(true);
                    v.startAnimation(animation);
                }
            }, delayMS);
        }
    }


    public static void playAnimation(final Context context, final View v, final int animationId) {
        if(v != null) {
            Animation animation = AnimationUtils.loadAnimation(context, animationId);
            animation.setDuration(animation.getDuration());
            animation.setFillAfter(true);
            v.startAnimation(animation);
        }
    }


    public static void buttonPress(final Context context, final View v){

        if(v != null) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.button_press_in);
            animation.setDuration(animation.getDuration());
            animation.setFillAfter(true);
            v.startAnimation(animation);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.button_press_out);
                    animation.setDuration(animation.getDuration());
                    animation.setFillAfter(true);
                    v.startAnimation(animation);
                }
            }, 100);
        }
    }


    public static void buttonPressSubtle(final Context context, final View v){

        if(v != null) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.button_press_in_subtle);
            animation.setDuration(animation.getDuration());
            animation.setFillAfter(true);
            v.startAnimation(animation);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.button_press_out_subtle);
                    animation.setDuration(animation.getDuration());
                    animation.setFillAfter(true);
                    v.startAnimation(animation);
                }
            }, 100);
        }
    }

}
