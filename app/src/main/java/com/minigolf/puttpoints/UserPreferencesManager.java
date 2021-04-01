package com.minigolf.puttpoints;

import android.content.Context;
import android.content.SharedPreferences;


import static android.content.Context.MODE_PRIVATE;

class UserPreferencesManager {

    private SharedPreferences userPrefs;
    private boolean usePars;
    private boolean firstLaunch;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_PARS = "pars";
    private static final String PREFS_FIRST_LAUNCH = "first_launch";


    UserPreferencesManager(Context c) {
        userPrefs = c.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        usePars = userPrefs.getBoolean(PREF_PARS, false);
        firstLaunch = userPrefs.getBoolean(PREFS_FIRST_LAUNCH, true);
    }

    public void updatePars(boolean usePars) {
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.putBoolean(PREF_PARS, usePars);
        editor.apply();
        usePars = userPrefs.getBoolean(PREF_PARS, false);
    }


    public void updateFirstLaunch(boolean firstLaunch) {
        SharedPreferences.Editor editor = userPrefs.edit();
        editor.putBoolean(PREFS_FIRST_LAUNCH, firstLaunch);
        editor.apply();
        firstLaunch = userPrefs.getBoolean(PREFS_FIRST_LAUNCH, true);
    }


    public boolean isFirstLaunch() { return firstLaunch;}

    public boolean parsOn() {
        return usePars;
    }

}