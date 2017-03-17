package com.example.lsdchat.ui.main.usersinfo;


import com.example.lsdchat.App;
import com.example.lsdchat.model.ContentModel;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class UserInfoPresenter implements UserInfoContract.Presenter {

    private UserInfoContract.View mView;
    private UserInfoContract.Model mModel;

    public UserInfoPresenter(UserInfoContract.View mView,UserInfoContract.Model mModel) {
        this.mView = mView;
        this.mModel = mModel;

    }


    @Override
    public void getUserAvatar(int userId) {
        mModel.getImagePath(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> mView.setImagePath(s));

    }
}
