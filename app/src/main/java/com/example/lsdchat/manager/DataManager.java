package com.example.lsdchat.manager;

import io.realm.Realm;

public class DataManager {
    private Realm realm;

    public DataManager() {
        realm = Realm.getDefaultInstance();
    }
    //TODO write query methods here
}
