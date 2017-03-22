package com.example.lsdchat.util;


import android.content.Context;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.model.ItemUser;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.ui.main.users.UsersContract;
import com.example.lsdchat.ui.main.users.UsersModel;
import com.example.lsdchat.util.error.ErrorInterface;
import com.example.lsdchat.util.error.NetworkConnect;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class UsersUtil {


    public static void getUserListAndSave(String token, Context context) {
        NetworkConnect networkConnect = ((NetworkConnect) context);
        ErrorInterface errorInterface = ((ErrorInterface) context);
        UsersContract.Model mModel = new UsersModel(App.getSharedPreferencesManager(context),
                App.getDataManager(), App.getApiManager().getDialogService());
        if (networkConnect.isNetworkConnect()) {
            mModel.getUserList(token)
                    .subscribe(userListResponse -> {
                        List<ItemUser> itemUsers = userListResponse.getItemUserList();
                        if (userListResponse.getTotalEntries() < (mModel.getUsersQuick().size() + 2)) {
                            mModel.deleteAllUSerQuick();
                            saveUserToDb(itemUsers, mModel);
                        } else {
                            saveUserToDb(itemUsers, mModel);
                        }
                        getImage(itemUsers, token, context);

                    }, errorInterface::showErrorDialog);
        }
    }

    private static void getImage(List<ItemUser> itemUsers, String token, Context context) {
        DataManager dataManager = App.getDataManager();
        ErrorInterface errorInterface = ((ErrorInterface) context);
        Observable.from(itemUsers)
                .flatMap(user -> Observable.just(user.getUser()))
                .filter(user -> user.getBlobId() != 0)
                .doOnError(errorInterface::showErrorDialog)
                .subscribe(user ->
                        Utils.downloadImage(user.getBlobId(), token)
                                .flatMap(file -> Observable.just(file.getAbsolutePath()))
                                .subscribe(path -> dataManager.saveUserAvatar(new ContentModel(String.valueOf(user.getId()), path)),
                                        errorInterface::showErrorDialog));


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
