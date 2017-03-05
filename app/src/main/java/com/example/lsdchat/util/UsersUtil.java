package com.example.lsdchat.util;


import android.util.Log;

import com.example.lsdchat.api.dialog.model.ItemUser;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.ui.main.users.UsersContract;
import com.example.lsdchat.ui.main.users.UsersModel;

import java.util.List;

import rx.Observable;

public class UsersUtil {



    public static void getUserListAndSave(String token) {
        UsersContract.Model mModel = new UsersModel();

        mModel.getUserList(token)
                .subscribe(userListResponse -> {
                    List<ItemUser> itemUsers = userListResponse.getItemUserList();
                    if (userListResponse.getTotalEntries() < (mModel.getUsersQuick().size() + 2)) {
                        mModel.deleteAllUSerQiuck();
                        saveUserToDb(itemUsers,mModel);
                    } else {
                        saveUserToDb(itemUsers,mModel);
                    }

                }, throwable -> {

                    Log.e("getUserList-error", throwable.getMessage());
                });

    }


    private static void saveUserToDb(List<ItemUser> itemUsers,UsersContract.Model mModel) {
        Observable.from(itemUsers)
                .flatMap(user -> Observable.just(user.getUser()))
                .subscribe(mModel::insetUsersQuick);
    }


    public static List<LoginUser> getAllUser() {
        UsersContract.Model mModel = new UsersModel();
        return mModel.getUsersQuickList();
    }
}
