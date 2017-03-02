package com.example.lsdchat.ui.main.dialogs;


import com.example.lsdchat.App;
import com.example.lsdchat.api.login.service.LoginService;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.User;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DialogsModel implements DialogsContract.Model {
    private DataManager mDataManager;
    private LoginService mLoginService;

    public DialogsModel() {
        mLoginService = App.getApiManager().getLoginService();
        mDataManager = App.getDataManager();
    }

    @Override
    public User getCurrentUser() {
        return mDataManager.getUser();
    }


    @Override
    public Observable<Void> destroySession(String token) {
        return mLoginService.destroySession(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void deleteUser() {
        mDataManager.deleteAllUserDb();
    }
}