package com.example.lsdchat.ui.main.editchat;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.DialogService;
import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.api.dialog.response.MessagesResponse;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.model.RealmDialogModel;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditchatModel implements EditchatContract.Model {
    private DialogService mDialogService;
    private DataManager mDataManager;

    public EditchatModel() {
        mDialogService = App.getApiManager().getDialogService();
        mDataManager = App.getDataManager();
    }

    @Override
    public Observable<DialogsResponse> getDialogByID(String token, String dialogID) {
        return mDialogService.getDialog(token, dialogID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ItemDialog> updateDialog(String dialogID, String token, CreateDialogRequest body) {
        return mDialogService.updateDialog(dialogID, token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<RealmDialogModel> getDialogFromDatabase(String dialogID) {
        return mDataManager.getDialogByID(dialogID)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ContentModel> getDialogAvatarFromDatabase(String dialogID) {
        return mDataManager.getObservableUserAvatar(dialogID)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<ContentModel>> getAllAvatarsFromDatabase() {
        return mDataManager.getObservableUserAvatar()
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<LoginUser>> getAppUsersFromDatabase() {
        return mDataManager.getUserObservable()
                .observeOn(AndroidSchedulers.mainThread());
    }
}
