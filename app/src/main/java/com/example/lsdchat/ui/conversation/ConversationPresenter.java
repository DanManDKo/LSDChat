package com.example.lsdchat.ui.conversation;

import android.content.Context;
import android.util.Log;

import com.example.lsdchat.api.dialog.model.ItemMessage;

import java.util.List;

public class ConversationPresenter implements ConversationContract.Presenter {
    private ConversationContract.View mView;
    private ConversationContract.Model mModel;
    private Context mContext;

    public ConversationPresenter(ConversationContract.View view) {
        mView = view;
        mContext = view.getContext();
        mModel = new ConversationModel();
    }

    @Override
    public void getMessages(String dialogId) {
        //Get token from DataBase
        String token = "e971e2f4acd8f5f2b5f546f7599c803e1c00cc7e";
        mModel.getMessagesByDialogId(token, dialogId)
                .subscribe(messagesResponse -> {
                    List<ItemMessage> listOfMessages = messagesResponse.getItemMessageList();
                    mView.fillListOfMessages(listOfMessages);
                }, throwable -> {
                    Log.d("++++", throwable.getMessage());
                });
    }
}
