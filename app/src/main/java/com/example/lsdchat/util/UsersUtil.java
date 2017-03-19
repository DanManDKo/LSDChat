package com.example.lsdchat.util;


import android.content.Context;
import android.util.Log;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.model.ItemUser;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.ui.main.fragment.BaseFragment;
import com.example.lsdchat.ui.main.users.UsersContract;
import com.example.lsdchat.ui.main.users.UsersModel;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class UsersUtil {

    static ErrorInterface errorInterface = new BaseFragment();

    public static void getUserListAndSave(String token, Context context) {
        UsersContract.Model mModel = new UsersModel(App.getSharedPreferencesManager(context),
                App.getDataManager(),App.getApiManager().getDialogService());

        mModel.getUserList(token)
                .subscribe(userListResponse -> {
                    List<ItemUser> itemUsers = userListResponse.getItemUserList();
                    if (userListResponse.getTotalEntries() < (mModel.getUsersQuick().size() + 2)) {
                        mModel.deleteAllUSerQuick();
                        saveUserToDb(itemUsers, mModel);
                    } else {
                        saveUserToDb(itemUsers, mModel);
                    }
                    getImage(itemUsers, token);

                }, throwable -> Log.e("FIX", throwable.getMessage()));
    }

    private static void getImage(List<ItemUser> itemUsers, String token) {
        DataManager dataManager = App.getDataManager();

        Observable.from(itemUsers)
                .flatMap(user -> Observable.just(user.getUser()))
                .filter(user -> user.getBlobId() != 0)
                .doOnError(throwable -> errorInterface.showErrorDialog(throwable))
                .subscribe(user ->
                        Utils.downloadImage(user.getBlobId(), token)
                        .flatMap(file -> Observable.just(file.getAbsolutePath()))
                        .subscribe(path -> dataManager.saveUserAvatar(new ContentModel(String.valueOf(user.getId()),path)),
                                throwable -> Log.e("FIX-user", throwable.getMessage())));


    }


    private static void saveUserToDb(List<ItemUser> itemUsers, UsersContract.Model mModel) {
        Observable.from(itemUsers)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(user -> Observable.just(user.getUser()))
                .subscribe(mModel::insetUsersQuick);
    }


    public static LoginUser getUserById(int id) {
        return App.getDataManager().getUserById(id);
    }
}
