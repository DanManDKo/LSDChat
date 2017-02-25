package com.example.lsdchat.manager;

import com.example.lsdchat.model.User;
import com.example.lsdchat.model.UserQuick;

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
            deleteAllUserDb();
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

    public void deleteAllUserDb() {
        RealmResults<User> realmResults = mRealm.where(User.class).findAll();
        if (!realmResults.isEmpty()) {
            mRealm.executeTransaction(realm -> mRealm.clear(User.class));
        }
    }

    //handle messages
    public void insertRealmMessage(RealmMessage message) {
        mRealm.executeTransactionAsync(realm -> realm.copyToRealmOrUpdate(message));
    }

    public List<RealmMessage> findMessagesByDialogId(String chatDialogId) {
        return mRealm.where(RealmMessage.class).equalTo(RealmMessage.CHAT_DIALOG_ID, chatDialogId).findAllSorted("dateSent", Sort.ASCENDING);
    public void insertCM(ContactsModel contactsModel) {
        mRealm.executeTransaction(realm -> realm.insertOrUpdate(contactsModel));
    }

    public List<ContactsModel> getContactsModel() {
        return mRealm.where(ContactsModel.class).findAll();
    }

    public void clearContactsModel() {
        RealmResults<ContactsModel> realmResults = mRealm.where(ContactsModel.class).findAll();
        if (!realmResults.isEmpty()) {
            mRealm.executeTransaction(realm -> realm.deleteAll());
        }
    }

    public void insertUsersQuick(UserQuick userQuick) {
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(userQuick));
    }

    public RealmResults<UserQuick> getUsersQuick() {
        return mRealm.where(UserQuick.class).findAll();
    }

}
