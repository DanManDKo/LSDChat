package com.example.lsdchat.ui.main.conversation;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.DialogService;
import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.example.lsdchat.api.dialog.request.CreateMessageRequest;
import com.example.lsdchat.api.dialog.request.UpdateMessageRequest;
import com.example.lsdchat.api.dialog.response.MessagesResponse;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.model.User;

import java.util.List;

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
    public Observable<MessagesResponse> getMessagesByDialogId(String token, String dialogId, int readMark, int pageLimit, int skip) {
        return mDialogService.getMessages(token, dialogId, pageLimit, skip, ApiConstant.MessageRequestParams.DATE_SENT, readMark)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<MessagesResponse> getMessageByMessageID(String token, String dialogID, String messageID, int readMark) {
        return mDialogService.getMessageById(token, dialogID, messageID, readMark)
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

    @Override
    public Observable<List<LoginUser>> getUsersFromDatabase() {
        return mDataManager.getUserObservable()
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<ContentModel>> getUserAvatarFromDatabase() {
        return mDataManager.getObservableUserAvatar()
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Void> deleteMessage(String token, String dialogID) {
        return mDialogService.deleteMessageRequest(token, dialogID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Void> updateMessage(String token, String messageID, String message, String dialogID) {
        UpdateMessageRequest body = new UpdateMessageRequest();
        body.setDialogID(dialogID);
        body.setMessage(message);

        return mDialogService.updateMessageRequest(token, messageID, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
