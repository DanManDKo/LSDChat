package com.example.lsdchat.ui.main.users;


import android.util.Log;

import com.example.lsdchat.api.dialog.model.ItemUser;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.model.UserQuick;
import com.example.lsdchat.util.Utils;

import java.util.List;

import io.realm.RealmResults;

public class UsersPresenter implements UsersContract.Presenter {
    private UsersContract.View mView;
    private SharedPreferencesManager mSharedPreferencesManager;
    private UsersContract.Model mModel;


    public UsersPresenter(UsersContract.View mView, SharedPreferencesManager sharedPreferencesManager) {
        this.mView = mView;
        this.mSharedPreferencesManager = sharedPreferencesManager;
        mModel = new UsersModel();

    }


    @Override
    public void downloadImage(long blobId, String token, ItemUser user) {
        Utils.downloadImage(blobId, token)
                .subscribe(file -> {
                    mModel.insertUsersQuick(new UserQuick(user, file.getPath()));
                }, throwable -> {
                    Log.e("TETS", throwable.getMessage());
                });
    }

    @Override
    public String getToken() {
        return mSharedPreferencesManager.getToken();
    }

    @Override
    public void getUserList() {
        mModel.getUserList(getToken())
                .subscribe(userListResponse -> {
                    Log.e("TETS", String.valueOf(userListResponse.getTotalEntries()));
                    List<ItemUser> itemUserList = userListResponse.getItemUserList();
                    for (ItemUser user : itemUserList) {
                        int blobId = user.getUser().getBlobId();
                        if (blobId != 0) {
                            downloadImage(blobId, getToken(), user);
                        } else {
                            mModel.insertUsersQuick(new UserQuick(user));
                        }


                    }
                    Log.e("TETS", itemUserList.toString());
                }, throwable -> {
                    Log.e("TETS", throwable.getMessage());
                });
    }


    @Override
    public RealmResults<UserQuick> getUsersQuick() {
        return mModel.getUsersQuick();
    }
}
