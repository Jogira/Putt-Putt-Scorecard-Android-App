package com.example.minigolfapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import static android.content.Context.MODE_PRIVATE;

class UserPreferencesManager {

    private SharedPreferences userPrefs;
    private boolean usePars;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_PARS = "pars";


    UserPreferencesManager(Context c) {
        userPrefs = c.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        usePars = userPrefs.getBoolean(PREF_PARS, false);
    }

    public void updatePars(boolean usePars) {
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.putBoolean(PREF_PARS, usePars);
        editor.apply();
        usePars = userPrefs.getBoolean(PREF_PARS, false);
    }

    public boolean parsOn() {
        return usePars;
    }

}