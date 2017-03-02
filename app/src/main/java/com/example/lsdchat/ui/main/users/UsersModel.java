package com.example.lsdchat.ui.main.users;


import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.DialogService;
import com.example.lsdchat.api.dialog.response.UserListResponse;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.manager.DataManager;

import java.util.List;
import java.util.Objects;

import io.realm.RealmResults;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UsersModel implements UsersContract.Model {
    private DialogService mDialogService;
    private DataManager mDataManager;

    public UsersModel() {
        mDataManager = App.getDataManager();
        mDialogService = App.getApiManager().getDialogService();
    }

    @Override
    public Observable<UserListResponse> getUserList(String token) {
        return mDialogService.getUserList(token, 100)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    @Override
    public void insetUsersQuick(LoginUser userQuick) {
        if (!Objects.equals(userQuick.getLogin(), "eroy") && !Objects.equals(userQuick.getEmail(), mDataManager.getUser().getEmail()))
            mDataManager.insertUserQuickToDB(userQuick);
    }


    @Override
    public RealmResults<LoginUser> getUsersQuick() {
        return mDataManager.getUsersQuick();
    }


    @Override
    public List<LoginUser> getUsersQuickList(String sort) {
        return mDataManager.getUsersQuickList(sort);
    }


    @Override
    public void deleteAllUSerQiuck() {
        mDataManager.deleteAllUsersQuick();
    }


}
