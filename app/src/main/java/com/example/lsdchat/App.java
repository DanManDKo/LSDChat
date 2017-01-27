package com.example.lsdchat;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.example.lsdchat.api.ApiManager;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.manager.SharedPreferencesManager;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {
    private static DataManager sDataManager;
    private static ApiManager sApiManager;
    private static SharedPreferencesManager sSharedPreferencesManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        RealmConfiguration configuration = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
        sDataManager = new DataManager();
    }

    public static DataManager getDataManager() {
        return sDataManager;
    }

    public static ApiManager getApiManager() {
        if (sApiManager == null) {
            sApiManager = new ApiManager();
            sApiManager.init();
        }
        return sApiManager;
    }

    public static SharedPreferencesManager getSharedPreferencesManager(Context context) {
        if (sSharedPreferencesManager == null) {
            sSharedPreferencesManager = new SharedPreferencesManager(context);
        }
        return sSharedPreferencesManager;
    }

}
