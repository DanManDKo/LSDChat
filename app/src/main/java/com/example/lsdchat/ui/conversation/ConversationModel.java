package com.example.lsdchat.ui.conversation;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.DialogService;
import com.example.lsdchat.api.dialog.response.MessagesResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ConversationModel implements ConversationContract.Model {
    private DialogService mDialogService;

    public ConversationModel() {
        mDialogService = App.getApiManager().getDialogService();
    }

    @Override
    public Observable<MessagesResponse> getMessagesByDialogId(String token, String dialogId) {
        return mDialogService.getMessages(token, dialogId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
