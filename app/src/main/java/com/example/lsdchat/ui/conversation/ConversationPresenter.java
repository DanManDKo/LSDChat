package com.example.lsdchat.ui.conversation;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.util.Network;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ConversationPresenter implements ConversationContract.Presenter {
    private ConversationContract.View mView;
    private ConversationContract.Model mModel;
    private Context mContext;
    private SharedPreferencesManager mPreferencesManager;
    private String token = "ace15ab9286353da23ca6a8103ce4fd8ee00cc7e";

    public ConversationPresenter(ConversationContract.View view, SharedPreferencesManager manager) {
        mView = view;
        mContext = view.getContext();
        mModel = new ConversationModel();
        mPreferencesManager = manager;
    }

    @Override
    public void onDestroy() {
        mView = null;
        mModel = null;
    }

    @Override
    public void getMessages(String dialogId) {
        //Get token from DataBase
        //mPreferencesManager.getToken();
        mModel.getMessagesByDialogId(token, dialogId)
                .doOnRequest(aLong -> mView.showLoadProgressBar(true))
                .doOnUnsubscribe(() -> mView.showLoadProgressBar(false))
                .map(messagesResponse -> messagesResponse.getItemMessageList())
                .doOnNext(itemMessages -> saveMessagesToDataBase(itemMessages))
                .subscribe(itemMessages -> {
                    fillAdapterListWithMessages(dialogId);

                }, throwable -> {
                });
    }

    @Override
    public void onAdapterItemClicked(String id, int position) {
        Toast.makeText(mContext, "item " + id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean isOnline() {
        return Network.isOnline(mContext);
    }

    @Override
    public void sendMessage(String dialogId, String message, String sendTo) {
        if (ConversationService.getState().equals(ConversationConnection.ConnectionState.CONNECTED)) {

            if (!message.equalsIgnoreCase("")) {
                Log.d("AAA", "The client is connected to the server, sending Message");

                //mPreferencesManager.getToken();
                mModel.createDialogMessage(token, dialogId, message)
                        .subscribe(itemMessage -> {
                            saveMessagesToDataBase(itemMessage);
                            addNewMessageToAdapterList(itemMessage.getId());
                        }, throwable -> {

                        });

                //Send the message to the server
                Intent intent = new Intent(ConversationService.SEND_MESSAGE);
                intent.putExtra(ConversationService.BUNDLE_MESSAGE_BODY, message);
                intent.putExtra(ConversationService.BUNDLE_TO, sendTo);
                getApplicationContext().sendBroadcast(intent);
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Client not connected to server, message not sent!",
                    Toast.LENGTH_LONG).show();
        }
        mView.clearEditableField();
    }

    @Override
    public void fillAdapterListWithMessages(String dialogId) {
        List<ItemMessage> list = App.getDataManager().retrieveMessagesByDialogId(dialogId);
        mView.fillConversationAdapter(list);
    }

    @Override
    public void addNewMessageToAdapterList(String messageId) {
        ItemMessage item = App.getDataManager().retrieveMessageById(messageId);
        mView.fillConversationAdapter(item);
    }

    private void saveMessagesToDataBase(List<ItemMessage> items) {
        for (ItemMessage item : items) {
            App.getDataManager().insertRealmMessage(item);
        }
    }

    private void saveMessagesToDataBase(ItemMessage item) {
        App.getDataManager().insertRealmMessage(item);
    }

    @Override
    public void loadMoreFromDataBase(String dialogId, int page) {
        List<ItemMessage> list = App.getDataManager().retrieveMessagesByDialogId(dialogId);
        if (page + 20 <= list.size()) {
            mView.loadMoreData(list.subList(page, page + 20));
        } else {
            Toast.makeText(mContext, "no more messages", Toast.LENGTH_SHORT).show();
        }
    }
}
