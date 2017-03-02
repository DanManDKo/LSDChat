package com.example.lsdchat.manager;

import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.model.User;
import com.example.lsdchat.model.UserQuick;

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

    public Realm getRealm() {
        return mRealm;
    }

    public User getUser() {
        return mRealm.where(User.class).findFirst();
    }

    public void deleteAllUserDb() {
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

    public void insertUserQuickToDB(LoginUser user) {

        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(user));
    }


    public List<LoginUser> getUsersQuickList(String sort) {
        switch (sort) {
            case ApiConstant.SORT_CREATE_AT:
                return mRealm.where(LoginUser.class).findAll();
            case ApiConstant.SORT_NAME_ACS:
                return mRealm.where(LoginUser.class).findAllSorted("fullName", Sort.ASCENDING);
            case ApiConstant.SORT_NAME_DESC:
                return mRealm.where(LoginUser.class).findAllSorted("fullName", Sort.DESCENDING);
            default:
                return mRealm.where(LoginUser.class).findAll();
        }

    }

    public RealmResults<LoginUser> getUsersQuick() {
        return mRealm.where(LoginUser.class).findAll();
//        return mRealm.where(LoginUser.class).findAllSorted("fullName", Sort.ASCENDING);
    }


    public void deleteAllUsersQuick() {
        RealmResults<UserQuick> realmResults = mRealm.where(UserQuick.class).findAll();
        if (!realmResults.isEmpty()) {
            mRealm.executeTransaction(realm -> realm.deleteAll());
        }
    }


}
