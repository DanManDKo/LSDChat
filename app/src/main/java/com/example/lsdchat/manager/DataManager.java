package com.example.lsdchat.manager;

import com.example.lsdchat.api.model.SessionToken;
import com.example.lsdchat.model.User;


import io.realm.Realm;

public class DataManager {
    private Realm mRealm;

    public DataManager() {
        mRealm = Realm.getDefaultInstance();
    }

    public boolean insertUser(User user) {
        try {
            mRealm.beginTransaction();
            mRealm.copyToRealmOrUpdate(user);
            mRealm.commitTransaction();
        } catch (Exception ex) {
            mRealm.cancelTransaction();
            return false;
        }
        return true;
    }


    public User getUser() {
        return mRealm.where(User.class).findFirst();
    }


}
