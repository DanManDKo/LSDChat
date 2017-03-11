package com.example.lsdchat.ui.main.conversation;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.DialogService;
import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.example.lsdchat.api.dialog.request.CreateMessageRequest;
import com.example.lsdchat.api.dialog.response.MessagesResponse;
import com.example.lsdchat.constant.ApiConstant;

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
        return mDialogService.getMessages(token, dialogId, ApiConstant.MessageRequestParams.MESSAGE_LIMIT, ApiConstant.MessageRequestParams.DATE_SENT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<MessagesResponse> getMessageByMessageID(String token, String dialogID, String messageID) {
        return mDialogService.getMessageById(token, dialogID, messageID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ItemMessage> createDialogMessage(String token, String dialogId, String message) {
        CreateMessageRequest body = new CreateMessageRequest(dialogId, message);

        return mDialogService.createMessages(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
