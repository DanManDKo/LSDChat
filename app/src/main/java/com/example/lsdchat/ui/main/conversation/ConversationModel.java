package com.example.lsdchat.ui.main.conversation;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.DialogService;
import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.example.lsdchat.api.dialog.request.CreateMessageRequest;
import com.example.lsdchat.api.dialog.response.MessagesResponse;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.model.User;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ConversationModel implements ConversationContract.Model {
    private DialogService mDialogService;
    private DataManager mDataManager;

    public ConversationModel() {
        mDialogService = App.getApiManager().getDialogService();
        mDataManager = App.getDataManager();
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

    @Override
    public Observable<RealmDialogModel> getDialogFromDatabase(String dialogID) {
        return mDataManager.getDialogByID(dialogID)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<User> getCurrentUserFromDatabase() {
        return mDataManager.getCurrentUser()
                .observeOn(AndroidSchedulers.mainThread());
    }
}
