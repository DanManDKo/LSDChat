package com.example.lsdchat.ui.dialog;


import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.DialogService;
import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateChatModel implements CreateChatContract.Model {
    private DialogService mDialogService;

    public CreateChatModel() {
        mDialogService = App.getApiManager().getDialogService();
    }


    @Override
    public Observable<ItemDialog> createDialog(String token, CreateDialogRequest createDialogRequest) {

        return mDialogService.createDialog(token, createDialogRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
