package com.example.lsdchat.ui.main.chats.dialogs;


import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.DialogService;
import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.util.DialogUtil;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DialogsModel implements DialogsContract.Model {
    private DataManager mDataManager;
    private DialogService mDialogService;
    private SharedPreferencesManager mSharedPreferencesManager;

    public DialogsModel(SharedPreferencesManager mSharedPreferencesManager) {
        mDataManager = App.getDataManager();
        mDialogService = App.getApiManager().getDialogService();
        this.mSharedPreferencesManager = mSharedPreferencesManager;
    }

    @Override
    public String getToken() {
        return mSharedPreferencesManager.getToken();
    }



    @Override
    public Observable<DialogsResponse> getAllDialogs(String token) {
        return mDialogService.getDialog(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<RealmDialogModel>> getObservableDialogsByType(int type) {
        return mDataManager.getObservableDialogsByType(type);

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
