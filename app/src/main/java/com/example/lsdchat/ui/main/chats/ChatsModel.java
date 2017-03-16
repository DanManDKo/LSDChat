package com.example.lsdchat.ui.main.chats;


import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.DialogService;
import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.api.login.service.LoginService;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.model.User;
import com.example.lsdchat.util.DialogUtil;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChatsModel implements ChatsContract.Model {
    private DataManager mDataManager;
    private LoginService mLoginService;
    private DialogService mDialogService;
    private SharedPreferencesManager mSharedPreferencesManager;

    public ChatsModel(SharedPreferencesManager sharedPreferencesManager) {
        mSharedPreferencesManager = sharedPreferencesManager;
        mLoginService = App.getApiManager().getLoginService();
        mDataManager = App.getDataManager();
        mDialogService = App.getApiManager().getDialogService();
    }


    @Override
    public String getToken() {
        return mSharedPreferencesManager.getToken();
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


    @Override
    public Observable<DialogsResponse> getAllDialogs(String token) {
        return mDialogService.getDialog(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public void saveDialog(List<RealmDialogModel> dialogList) {
        Observable.from(dialogList)
                .subscribe(itemDialog -> mDataManager.insertDialogToDB(itemDialog));
        DialogUtil.saveImageDialog(dialogList,getToken());
    }



    @Override
    public Observable<List<ContentModel>> getObservableUserAvatar() {
        return mDataManager.getObservableUserAvatar();
    }
}