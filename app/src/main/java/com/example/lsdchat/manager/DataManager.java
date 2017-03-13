package com.example.lsdchat.manager;


import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.model.DialogModel;
import com.example.lsdchat.model.User;
import com.example.lsdchat.model.UserAvatar;
import com.example.lsdchat.model.UserQuick;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Emitter;
import rx.Observable;

// TODO: 3/9/17 [Code Review] make sure all your code related to work with db runs on working thread
// (create Observable wrappers maybe)
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

    public void insertUserQuickToDB(LoginUser user) {
        mRealm.executeTransaction(realm -> {
            realm.copyToRealmOrUpdate(user);
        });
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

    public List<LoginUser> getUsersQuickList() {
        return mRealm.where(LoginUser.class).findAll();
    }

    public Observable<List<LoginUser>> getUserObservable() {
        return Observable.fromEmitter(loginUserEmitter -> {
            loginUserEmitter.onNext(mRealm.where(LoginUser.class).findAll());
            loginUserEmitter.onCompleted();
        }, Emitter.BackpressureMode.NONE);
    }


    public LoginUser getUserById(int id) {
        return mRealm.where(LoginUser.class).equalTo("id", id).findFirst();
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


    public void insertDialogToDB(DialogModel dialog) {
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(dialog));
    }

    public List<DialogModel> getDialogsByType(int type) {
        return mRealm.where(DialogModel.class).equalTo("type", type).findAllSorted("updatedAt", Sort.DESCENDING);
    }

    //handle messages
    public ItemMessage retrieveMessageById(String messageId) {
        return mRealm.where(ItemMessage.class).equalTo(ItemMessage.MESSAGE_ID, messageId).findFirst();
    }

    public void insertRealmMessage(ItemMessage message) {
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(message));
    }

    public List<ItemMessage> retrieveMessagesByDialogId(String chatDialogId) {
        return mRealm.where(ItemMessage.class).equalTo(ItemMessage.CHAT_DIALOG_ID, chatDialogId).findAllSorted(ItemMessage.DATE_SENT, Sort.DESCENDING);
    }

    public void saveUserToRealm(User user) {
        deleteAllUserDb();
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(user));
    }


    public void saveUserAvatar(UserAvatar userAvatar) {
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(userAvatar));
    }

    public List<UserAvatar> getListUserAvatar() {
        return mRealm.where(UserAvatar.class).findAll();
    }

    public Observable<List<UserAvatar>> getObservableUserAvatar() {
        return Observable.fromEmitter(userAvatarEmitter -> {
            userAvatarEmitter.onNext(mRealm.where(UserAvatar.class).findAll());
            userAvatarEmitter.onCompleted();
        }, Emitter.BackpressureMode.NONE);
    }
}
