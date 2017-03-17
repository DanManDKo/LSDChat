package com.example.lsdchat.ui.main.users;


import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.ui.main.usersinfo.UserInfoFragment;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class UsersPresenter implements UsersContract.Presenter {
    private UsersContract.View mView;
    private UsersContract.Model mModel;


    public UsersPresenter(UsersContract.View mView, UsersContract.Model mModel) {
        this.mView = mView;
        this.mModel = mModel;


    }

    @Override
    public void setClickUser(LoginUser loginUser) {
        mView.navigateToInfoUser(new UserInfoFragment().newInstance(loginUser));
    }


    @Override
    public Observable<List<LoginUser>> getUserObservable() {
        return mModel.getUserObservable()
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void setSortedList(String sort) {
        mModel.getUserObservable(sort)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginUsers -> mView.setListUsers(loginUsers));

    }

    @Override
    public Observable<List<ContentModel>> getObservableUserAvatar() {
        return mModel.getObservableUserAvatar();
    }
}
