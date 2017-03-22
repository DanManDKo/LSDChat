package com.example.lsdchat.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by User on 22.01.2017.
 */

public class SharedPreferencesManager {
    public static final String APP_PREFERENCES = "preferences";
    public static final String IS_LOGGED = "is_logged";

    public static final String TOKEN = "token";
    public static final String USER_ID = "user_id";

    public static final boolean DEFAULT_VALUE = false;
    // TODO: 28.01.2017 [Code Review] rename to 'logged', do not start boolean values with 'is'.
    // Setter should be 'setLogged'
    private boolean isLogged;
    private SharedPreferences mSharedPreferences;
    private String token;
    private int userID;

    public SharedPreferencesManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
//        isLogged = mSharedPreferences.getBoolean(IS_LOGGED, DEFAULT_VALUE);
        token = mSharedPreferences.getString(TOKEN, "");

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

    public String getToken() {
        return token;
    }

    public int getUserID() {
        return userID;
    }

    public void saveToken(String token) {
        this.token = token;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(TOKEN, token);
        editor.apply();
    }
}

