package com.applauncher.application;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Scott on 11/7/2015.
 */
public class UserPreferences {

    public static final String FIRSTRUN = "firstrun";

    public static boolean isFirstRun(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).getBoolean(FIRSTRUN,true);
    }

    public static void setFirstRun(Context context,boolean state){
        PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).edit().putBoolean(FIRSTRUN,state).apply();
    }
}
