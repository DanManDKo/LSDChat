package com.example.lsdchat.ui.main.users;


import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.DialogService;
import com.example.lsdchat.api.dialog.response.UserListResponse;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.UserQuick;

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
        return mDialogService.getUserList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    @Override
    public void insertUsersQuick(UserQuick user) {
        mDataManager.insertUsersQuick(user);
    }

    @Override
    public RealmResults<UserQuick> getUsersQuick() {
        return mDataManager.getUsersQuick();
    }
}
