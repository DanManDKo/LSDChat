package com.example.lsdchat.manager;

import com.example.lsdchat.api.dialog.model.ItemMessage;
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
    public void insertRealmMessage(ItemMessage message) {
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(message));
    }

    public List<ItemMessage> retrieveMessagesByDialogId(String chatDialogId) {
        return mRealm.where(ItemMessage.class).equalTo(ItemMessage.CHAT_DIALOG_ID, chatDialogId).findAllSorted(ItemMessage.DATE_SENT, Sort.DESCENDING);
    }

    public ItemMessage retrieveMessageById(String messageId) {
        return mRealm.where(ItemMessage.class).equalTo(ItemMessage.MESSAGE_ID, messageId).findFirst();
    }

}
