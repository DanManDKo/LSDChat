package com.example.lsdchat.ui.main.chats.dialogs;


import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.DialogService;
import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.DialogModel;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DialogsModel implements DialogsContract.Model {
    private DataManager mDataManager;
    private DialogService mDialogService;

    public DialogsModel() {
        mDataManager = App.getDataManager();
        mDialogService = App.getApiManager().getDialogService();
    }

    @Override
    public List<DialogModel> getDialogsByType(int type) {
        return mDataManager.getDialogsByType(type);
    }

    @Override
    public Observable<DialogsResponse> getAllDialogs(String token) {
        return mDialogService.getDialog(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void saveDialog(List<DialogModel> dialogList) {
        Observable.from(dialogList)
                .subscribe(itemDialog -> mDataManager.insertDialogToDB(itemDialog));
    }

}
