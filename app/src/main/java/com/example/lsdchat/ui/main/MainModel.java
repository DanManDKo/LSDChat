package com.example.lsdchat.ui.main;


import com.example.lsdchat.App;
import com.example.lsdchat.api.login.service.LoginService;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.User;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainModel implements MainContract.Model {
    private DataManager mDataManager;
    private LoginService mLoginService;

    public MainModel() {
        mLoginService = App.getApiManager().getmLoginService();
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
        mDataManager.clearDb();
    }
}
