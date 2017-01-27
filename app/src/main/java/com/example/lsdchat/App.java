package com.example.lsdchat;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.example.lsdchat.manager.ApiManager;
import com.example.lsdchat.manager.DataManager;
import com.facebook.drawee.backends.pipeline.Fresco;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application {
    private static DataManager sDataManager;
    private static ApiManager sApiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        Fabric.with(this, new Crashlytics());

        RealmConfiguration configuration = new RealmConfiguration
                .Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
        sDataManager = new DataManager();

        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/roboto_regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
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


}
