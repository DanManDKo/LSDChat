package com.example.lsdchat.ui.conversation;

import android.content.Context;
import android.util.Log;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.example.lsdchat.model.RealmMessage;
import com.example.lsdchat.util.Network;

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
        String token = "e40a4e9201f10cb5a64634fc9b398b267100cc7e";
        mModel.getMessagesByDialogId(token, dialogId)
                .map(messagesResponse -> messagesResponse.getItemMessageList())
                .subscribe(itemMessages -> {
                            saveMessagesToDataBase(itemMessages);
                            List<RealmMessage> list = getMessagesFromDataBase(dialogId);


                            mView.fillListOfMessages(list);


                        },
                        throwable -> {
                            //handle errors
                            if (!Network.isOnline(mContext)) {
                                List<RealmMessage> list = getMessagesFromDataBase(dialogId);
                                mView.fillListOfMessages(list);
                            }
                        });
    }

    private void saveMessagesToDataBase(List<ItemMessage> items) {
        for (ItemMessage item : items) {
            RealmMessage object = new RealmMessage(true);

            object.setChatDialogId(item.getChatDialogId());
            object.setMessage(item.getMessage());
            object.setSenderId(item.getSender_id());
            object.setDateSent(Long.parseLong(item.getDateSent()));
            object.setMessageId(item.getId());
            object.setRead(item.getRead());

            App.getDataManager().insertRealmMessage(object);
        }
    }

    public List<RealmMessage> getMessagesFromDataBase(String dialogId) {
        return App.getDataManager().findMessagesByDialogId(dialogId);
    }
}
