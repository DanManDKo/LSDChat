package com.example.lsdchat.ui.conversation;

import android.content.Context;

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
        String token = "";
        mModel.getMessagesByDialogId(token, dialogId)
                .subscribe(messagesResponse -> {
                    List<ItemMessage> listOfMessages = messagesResponse.getItemMessageList();
                    mView.fillListOfMessages(listOfMessages);
                });
    }
}
