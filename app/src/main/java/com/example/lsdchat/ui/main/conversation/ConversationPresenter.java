package com.example.lsdchat.ui.main.conversation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.util.Network;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ConversationPresenter implements ConversationContract.Presenter {
    private ConversationContract.View mView;
    private ConversationContract.Model mModel;
    private Context mContext;
    private SharedPreferencesManager mPreferencesManager;
    private BroadcastReceiver mBroadcastReceiver;
    private int userID;

    public ConversationPresenter(ConversationContract.View view, SharedPreferencesManager manager) {
        mView = view;
        mContext = view.getContext();
        mModel = new ConversationModel();
        mPreferencesManager = manager;

        userID = mPreferencesManager.getUserID();
    }

    @Override
    public void onDestroy() {
        mView = null;
        mModel = null;
    }

    @Override
    public void onUnregisterBroadcastReceiver() {
        mContext.unregisterReceiver(mBroadcastReceiver);
        Log.e("AAA", "unregister receiver");
    }

    @Override
    public void onRegisterBroadcastReceiver() {
        Log.e("AAA", "register receiver");
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action) {
                    case XMPPService.NEW_MESSAGE:
                        String fromJID = intent.getStringExtra(XMPPService.BUNDLE_FROM_JID);
                        String body = intent.getStringExtra(XMPPService.BUNDLE_MESSAGE_BODY);
                        String messageID = intent.getStringExtra(XMPPService.MESSAGE_ID);
//                        String from = fromJID.split("-")[0];

                        retrieveNewMessage(mView.getCurrentDialogID(), messageID);

//                        if (fromJID.equals(ownerJID)) {
//                            Log.e("AAA", "Got a message from myself");
////                            mAdapter.addFirst(item);
////                            mRecyclerView.scrollToPosition(0);
//                        } else {
//                            Log.e("AAA", "Got a message from friend");
//
////                            mAdapter.addFirst(item);
////                            mRecyclerView.scrollToPosition(0);
//                        }
                        return;
                }
            }
        };
        IntentFilter filter = new IntentFilter(XMPPService.NEW_MESSAGE);
        mContext.registerReceiver(mBroadcastReceiver, filter);
    }

    private void retrieveNewMessage(String dialogID, String messageID) {
        mModel.getMessageByMessageID(mPreferencesManager.getToken(), dialogID, messageID)
                .subscribe(messagesResponse -> {
                    ItemMessage im = messagesResponse.getItemMessageList().get(0);
                    saveMessagesToDataBase(im);
                    addNewMessageToAdapterList(messageID);
                    Log.e("retrieveNewMessage", im.getMessage());
                }, throwable -> {
                    Log.e("retrieveNewMessage", throwable.getMessage().toString());
                });
    }

    @Override
    public void getMessages(String dialogId) {
        mModel.getMessagesByDialogId(mPreferencesManager.getToken(), dialogId)
                .doOnRequest(aLong -> mView.showLoadProgressBar(true))
                .doOnUnsubscribe(() -> mView.showLoadProgressBar(false))
                .map(messagesResponse -> messagesResponse.getItemMessageList())
                .doOnNext(itemMessages -> saveMessagesToDataBase(itemMessages))
                .subscribe(itemMessages -> {
                    fillAdapterListWithMessages(dialogId);

                }, throwable -> {
                    Log.e("AAA", throwable.getMessage().toString());
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
        if (XMPPService.getState().equals(XMPPConnection.ConnectionState.CONNECTED)) {

            if (!message.equalsIgnoreCase("")) {
                Log.e("AAA", "The client is connected to the server, sending Message");

                //mPreferencesManager.getToken();
//                mModel.createDialogMessage(token, dialogId, message)
//                        .subscribe(itemMessage -> {
//                            saveMessagesToDataBase(itemMessage);
//                            addNewMessageToAdapterList(itemMessage.getId());
//                        }, throwable -> {
//
//                        });

                //Send the message to the server
                Intent intent = new Intent(XMPPService.SEND_MESSAGE);
                intent.putExtra(XMPPService.BUNDLE_MESSAGE_BODY, message);
                intent.putExtra(XMPPService.BUNDLE_TO, sendTo);
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
            Toast.makeText(mContext, "All history up to date", Toast.LENGTH_SHORT).show();
        }
    }
}
