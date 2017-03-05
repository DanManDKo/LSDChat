package com.example.lsdchat.manager;

import com.example.lsdchat.manager.model.RealmItemDialog;
import com.example.lsdchat.model.User;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class DataManager {
    private Realm mRealm;

    public DataManager() {
        mRealm = Realm.getDefaultInstance();
    }

    public boolean insertUser(User user) {
        try {
            clearDb();
            mRealm.beginTransaction();
            mRealm.copyToRealmOrUpdate(user);
            mRealm.commitTransaction();
        } catch (Exception ex) {
            mRealm.cancelTransaction();
            return false;
        }
        return true;
    }

    public boolean saveDialogs(List<RealmItemDialog> dialogs) {
        try {
            mRealm.beginTransaction();
            mRealm.copyToRealmOrUpdate(dialogs);
            mRealm.commitTransaction();
        } catch (Exception ex) {
            mRealm.cancelTransaction();
            return false;
        }
        return true;
    }

    public List<RealmItemDialog> getDialogs(int type) {
        return mRealm.where(RealmItemDialog.class).equalTo("type", type).findAll();
    }

    public User getUser() {
        return mRealm.where(User.class).findFirst();
    }

    private void clearDb() {
        RealmResults<User> realmResults = mRealm.where(User.class).findAll();
        if (!realmResults.isEmpty()) {
            mRealm.executeTransaction(realm -> realm.deleteAll());
        }
    }

}
