package com.example.lsdchat.ui.main.users;


import android.util.Log;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.model.ItemUser;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.model.UserAvatar;
import com.example.lsdchat.ui.main.usersinfo.UserInfoFragment;
import com.example.lsdchat.util.Utils;

import java.util.List;

import rx.Observable;

public class UsersPresenter implements UsersContract.Presenter {
    private UsersContract.View mView;
    private String mToken;
    private UsersContract.Model mModel;


    public UsersPresenter(UsersContract.View mView, UsersContract.Model mModel) {
        this.mView = mView;
        this.mModel = mModel;
        this.mToken = mModel.getToken();
//        getUserList();

    }



    private void getUserList() {
        mModel.getUserList(mToken)
                .subscribe(userListResponse -> {
                    List<ItemUser> itemUsers = userListResponse.getItemUserList();
                    if (userListResponse.getTotalEntries() < (mModel.getUsersQuick().size() + 2)) {
                        mModel.deleteAllUSerQuick();
                        saveUserToDb(itemUsers);
                    } else {
                        saveUserToDb(itemUsers);
                    }
//                    getImage(itemUsers);
                }, throwable -> mView.showMessageError(throwable));

    }


    private void saveUserToDb(List<ItemUser> itemUsers) {
        Observable.from(itemUsers)
                .flatMap(user -> Observable.just(user.getUser()))
                .subscribe(loginUser -> {
                    mModel.insetUsersQuick(loginUser);
                });
    }

    private void getImage(List<ItemUser> itemUsers) {
        Observable.from(itemUsers)
                .flatMap(user -> Observable.just(user.getUser()))
                .filter(loginUser -> loginUser.getBlobId() != 0)
                .filter(loginUser -> loginUser.getImagePath() == null)
                .subscribe(loginUser -> {
                    Utils.downloadImage(loginUser.getBlobId(), mToken)
                            .flatMap(file -> Observable.just(file.getAbsolutePath()))
                            .subscribe(path -> {
                                LoginUser loginUser1 = new LoginUser(loginUser, path);
                                Log.e("getImage", loginUser1.getImagePath());
                                mModel.insetUsersQuick(loginUser1);
                            }, throwable -> {
                                Log.e("getImage", throwable.getMessage());
                            });

                });

    }


    @Override
    public Observable<String> getImageUrl(long blobId) {
        return Utils.getUrlImage(blobId, mToken);
    }


    @Override
    public void setClickUser(LoginUser loginUser) {
        mView.navigateToInfoUser(new UserInfoFragment().newInstance(loginUser));
    }

    @Override
    public List<LoginUser> getUsersQuickList(String sort) {
        return mModel.getUsersQuickList(sort);
    }

    @Override
    public List<LoginUser> getUsersQuickList() {
        return mModel.getUsersQuickList();
    }



}
