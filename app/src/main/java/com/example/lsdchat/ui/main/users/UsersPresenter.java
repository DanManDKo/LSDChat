package com.example.lsdchat.ui.main.users;


import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.ui.main.usersinfo.UserInfoFragment;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;

public class UsersPresenter implements UsersContract.Presenter {
    private UsersContract.View mView;
    private UsersContract.Model mModel;


    public UsersPresenter(UsersContract.View mView, UsersContract.Model mModel) {
        this.mView = mView;
        this.mModel = mModel;


    }

    @Override
    public void onDestroy() {
        mView = null;
        mModel = null;
    }

    @Override
    public void setClickUser(LoginUser loginUser) {
        mView.navigateToInfoUser(new UserInfoFragment().newInstance(loginUser));
    }

    @Override
    public void getUserFilterList(String query) {

        mModel.getUserObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginUserList -> {
                    List<LoginUser> filterList = new ArrayList<>();
                    for (LoginUser user : loginUserList) {
                        String name = user.getFullName().toLowerCase();
                        if (name.contains(query.toLowerCase())) {
                            filterList.add(user);
                        }
                    }

                    mView.setListUsers(filterList);

                });
    }

    @Override
    public void getUserList() {
        mModel.getUserObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginUserList -> mView.setListUsers(loginUserList));
    }

    @Override
    public void setSortedList(String sort) {
        mModel.getUserObservable(sort)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginUsers -> mView.setListUsers(loginUsers));

    }

    @Override
    public void getContentModelList() {
        mModel.getObservableUserAvatar()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contentModels -> mView.setContentModelList(contentModels));
    }
}
