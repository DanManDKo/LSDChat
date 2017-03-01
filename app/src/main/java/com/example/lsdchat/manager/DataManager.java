package com.example.lsdchat.manager;

import com.example.lsdchat.model.RealmMessage;
import com.example.lsdchat.model.User;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

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


    public User getUser() {
        return mRealm.where(User.class).findFirst();
    }

    private void clearDb() {
        RealmResults<User> realmResults = mRealm.where(User.class).findAll();
        if (!realmResults.isEmpty()) {
            mRealm.executeTransaction(realm -> realm.deleteAll());
        }
    }

    //handle messages
    public void insertRealmMessage(RealmMessage message) {
        mRealm.executeTransactionAsync(realm -> realm.copyToRealmOrUpdate(message));
    }

    public List<RealmMessage> findMessagesByDialogId(String chatDialogId) {
        return mRealm.where(RealmMessage.class).equalTo(RealmMessage.CHAT_DIALOG_ID, chatDialogId).findAllSorted("dateSent", Sort.ASCENDING);
    }

}
