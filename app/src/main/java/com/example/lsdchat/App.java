package com.example.lsdchat;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.example.lsdchat.manager.DataManager;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {
    private static DataManager sDataManager;

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


}
