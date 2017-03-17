package com.example.lsdchat.ui.main.users;


import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.DialogService;
import com.example.lsdchat.api.dialog.response.UserListResponse;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.util.DialogUtil;

import java.util.List;
import java.util.Objects;

import io.realm.RealmResults;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UsersModel implements UsersContract.Model {
    private DialogService mDialogService;
    private DataManager mDataManager;
    private SharedPreferencesManager mSharedPreferencesManager;

    public UsersModel(SharedPreferencesManager sharedPreferencesManager,DataManager mDataManager,DialogService mDialogService) {
        this.mSharedPreferencesManager = sharedPreferencesManager;
        this.mDataManager = mDataManager;
        this.mDialogService = mDialogService;

    }
    @Override
    public String getToken() {
        return mSharedPreferencesManager.getToken();
    }

    @Override
    public Observable<UserListResponse> getUserList(String token) {
        return mDialogService.getUserList(token, 100)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    @Override
    public void insetUsersQuick(LoginUser userQuick) {
        if (!Objects.equals(userQuick.getLogin(), "eroy"))
            mDataManager.insertUserQuickToDB(userQuick);
    }


    @Override
    public RealmResults<LoginUser> getUsersQuick() {
        return mDataManager.getUsersQuick();
    }

    @Override
    public Observable<List<LoginUser>> getUserObservable() {
        return mDataManager.getUserObservable();
    }

    @Override
    public Observable<List<LoginUser>> getUserObservable(String sort) {
        return mDataManager.getUserObservable(sort);
    }

    @Override
    public Observable<List<ContentModel>> getObservableUserAvatar() {
        return mDataManager.getObservableUserAvatar();
    }

    @Override
    public void deleteAllUSerQuick() {
        mDataManager.deleteAllUsersQuick();
    }

}
