package com.example.lsdchat.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by User on 22.01.2017.
 */

public class SharedPreferencesManager {
    public static final String APP_PREFERENCES = "preferences";
    public static final String IS_LOGGED = "is_logged";
    public static final boolean DEFAULT_VALUE = false;
    private boolean isLogged;
    private SharedPreferences mSharedPreferences;

    public SharedPreferencesManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        isLogged = mSharedPreferences.getBoolean(IS_LOGGED, DEFAULT_VALUE);
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setIsLogged(boolean logged) {
        isLogged = logged;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(IS_LOGGED, logged);
        editor.commit();
    }


}

