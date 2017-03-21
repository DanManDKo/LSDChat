package com.example.lsdchat.manager;


import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.model.User;
import com.example.lsdchat.model.UserQuick;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;


public class DataManager {
    private static final String ID = "id";

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


    public List<LoginUser> getUsersQuickList() {
        return mRealm.where(LoginUser.class).findAll();
    }


    public Observable<List<LoginUser>> getUserObservable() {

        return Observable.fromCallable(() -> mRealm.where(LoginUser.class).findAll());
    }

    public Observable<List<LoginUser>> getUserObservable(String sort) {
        return Observable.fromCallable(() -> {
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
        });
    }


    public LoginUser getUserById(int id) {
        return mRealm.where(LoginUser.class).equalTo(ID, id).findFirst();
    }

    public Observable<RealmDialogModel> getDialogByID(String dialogID) {
        return mRealm.where(RealmDialogModel.class).equalTo(ID, dialogID).findFirst().asObservable();
    }

    public Observable<User> getCurrentUser() {
        return mRealm.where(User.class).findFirst().asObservable();
    }

    public RealmResults<LoginUser> getUsersQuick() {
        return mRealm.where(LoginUser.class).findAll();
    }


    public void deleteAllUsersQuick() {
        RealmResults<UserQuick> realmResults = mRealm.where(UserQuick.class).findAll();
        if (!realmResults.isEmpty()) {
            mRealm.executeTransaction(realm -> realm.deleteAll());
        }
    }


    public void insertDialogToDB(RealmDialogModel dialog) {
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(dialog));
    }

    public void deleteItemDialog(String idDialog) {
        mRealm.executeTransaction(realm -> {
            RealmResults<RealmDialogModel> realmResults = mRealm.where(RealmDialogModel.class).equalTo("id",idDialog).findAll();
            realmResults.deleteAllFromRealm();
        });
    }


    public Observable<List<RealmDialogModel>> getAllDialog() {
        return Observable.fromCallable(() -> mRealm.where(RealmDialogModel.class).findAll());
    }

    public Observable<List<RealmDialogModel>> getObservableDialogsByType(int type) {
        return Observable.fromCallable(() ->
                mRealm.where(RealmDialogModel.class).equalTo("type", type).findAllSorted("lastMessageDateSent", Sort.DESCENDING));
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

    public void saveUserAvatar(ContentModel contentModel) {
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(contentModel));
    }

    public Observable<List<ContentModel>> getObservableUserAvatar() {
        return Observable.fromCallable(() -> mRealm.where(ContentModel.class).findAll());
    }

    public Observable<ContentModel> getObservableUserAvatar(String dialogID) {
        return Observable.fromCallable(() -> mRealm.where(ContentModel.class).equalTo(ID, dialogID).findFirst());
    }

    public List<ContentModel> getUserAvatars() {
        return mRealm.where(ContentModel.class).findAll();
    }
}
