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
import com.example.lsdchat.model.IdsListInteger;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.model.User;
import com.example.lsdchat.util.Network;

import java.util.List;

import io.realm.RealmList;
import rx.Observable;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ConversationPresenter implements ConversationContract.Presenter {
    private static final int PUBLIC_GROUP_TYPE = 1;
    private static final int PRIVATE_GROUP_TYPE = 2;
    private static final int PRIVATE_TYPE = 3;
    private static final int UNREAD_MARK = 0;

    private ConversationContract.View mView;
    private ConversationContract.Model mModel;

    private Context mContext;
    private SharedPreferencesManager mPreferencesManager;
    private BroadcastReceiver mBroadcastReceiver;

    public ConversationPresenter(ConversationContract.View view, ConversationContract.Model model, SharedPreferencesManager manager) {
        mView = view;
        mContext = view.getContext();
        mModel = model;
        mPreferencesManager = manager;
    }

    @Override
    public void onDetach() {

    }

    @Override
    public void onDestroy() {
        mView = null;
        mModel = null;
        mPreferencesManager = null;
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
                        String messageID = intent.getStringExtra(XMPPService.MESSAGE_ID);

                        retrieveNewMessage(mView.getCurrentDialogID(), messageID);
                        return;
                    case XMPPService.NEW_MESSAGE_PRIVATE:
                        retrieveNewMessagePrivate(mView.getCurrentDialogID());
                        return;
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(XMPPService.NEW_MESSAGE);
        filter.addAction(XMPPService.NEW_MESSAGE_PRIVATE);
        mContext.registerReceiver(mBroadcastReceiver, filter);
    }

    private void retrieveNewMessage(String dialogID, String messageID) {
        mModel.getMessageByMessageID(mPreferencesManager.getToken(), dialogID, messageID, UNREAD_MARK)
                .subscribe(messagesResponse -> {
                    ItemMessage im = messagesResponse.getItemMessageList().get(0);
                    saveMessagesToDataBase(im);
                    addNewMessageToAdapterList(messageID);
                }, throwable -> {
                    Log.e("retrieveNewMessage", throwable.getMessage().toString());
                });
    }

    private void retrieveNewMessagePrivate(String dialogID) {
        mModel.getMessagesByDialogId(mPreferencesManager.getToken(), dialogID, 1, 0, UNREAD_MARK)
                .subscribe(messagesResponse -> {
                    ItemMessage im = messagesResponse.getItemMessageList().get(0);
                    saveMessagesToDataBase(im);
                    addNewMessageToAdapterList(im.getId());
                }, throwable -> {
                    Log.e("retrieveNewMessage", throwable.getMessage().toString());
                });

    }

    @Override
    public void getMessages(String dialogId, int limit, int skip) {
        mModel.getMessagesByDialogId(mPreferencesManager.getToken(), dialogId, limit, skip, UNREAD_MARK)
                .map(messagesResponse -> messagesResponse.getItemMessageList())
                .doOnNext(itemMessages -> saveMessagesToDataBase(itemMessages))
                .subscribe(itemMessages -> {

                    fillAdapterListWithMessages(itemMessages);

                }, throwable -> {
                    Log.e("AAA", throwable.getMessage().toString());
                });
    }


    @Override
    public void onAdapterItemClicked(String id, int position, String message, String dialogID) {
        mView.showConfirmationWindow(id, position, message, dialogID);
    }

    @Override
    public void deleteMessage(String dialogID, int position) {
        mModel.deleteMessage(mPreferencesManager.getToken(), dialogID)
                .subscribe(aVoid -> {
                    mView.notifyAdapterItemDeleted(position);
                }, throwable -> {

                });
    }

    @Override
    public void updateMessage(String messageID, int position, String message, String dialogID) {
        mModel.updateMessage(mPreferencesManager.getToken(), messageID, message, dialogID)
                .subscribe(aVoid -> {
                    mView.notifyAdapterItemUpdated(position, message);
                }, throwable -> {

                });
    }

    @Override
    public boolean isOnline() {
        return Network.isOnline(mContext);
    }

    @Override
    public void sendMessage(String dialogId, String message, String sendTo, int dialogType) {
        if (XMPPService.getState().equals(XMPPConnection.ConnectionState.CONNECTED)) {

            if (!message.equalsIgnoreCase("")) {
                Log.e("AAA", "The client is connected to the server, sending Message");
                //Send the message to the server
                if (dialogType != 3) {
                    Intent intent = new Intent(XMPPService.SEND_MESSAGE);
                    intent.putExtra(XMPPService.BUNDLE_MESSAGE_BODY, message);
                    intent.putExtra(XMPPService.BUNDLE_TO, sendTo);
                    getApplicationContext().sendBroadcast(intent);
                } else {
                    Intent intent = new Intent(XMPPService.SEND_MESSAGE_PRIVATE);
                    intent.putExtra(XMPPService.BUNDLE_MESSAGE_BODY, message);
                    intent.putExtra(XMPPService.BUNDLE_TO, sendTo);
                    getApplicationContext().sendBroadcast(intent);
                }
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Client not connected to server, message not sent!",
                    Toast.LENGTH_LONG).show();
        }
        mView.clearEditableField();
    }

    @Override
    public void fillAdapterListWithMessages(List<ItemMessage> list) {
        mView.fillConversationAdapter(list);
    }

    @Override
    public void fillAdapterListWithMessages(String dialogID) {
        List<ItemMessage> list = App.getDataManager().retrieveMessagesByDialogId(dialogID);
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
    public void navigateToEditchatFragment(String dialogId) {
        Observable<Integer> observableUserID =
                mModel.getCurrentUserFromDatabase()
                        .map(User::getId);

        Observable<RealmDialogModel> observableDialogModel =
                mModel.getDialogFromDatabase(dialogId);

        Observable.combineLatest(observableUserID, observableDialogModel, ((value, dialogModel) -> {
            boolean result = checkAccessLevel(dialogModel, value);
            return result;
        })).subscribe(aBoolean -> {
            permissionForEditDialog(aBoolean, dialogId);

        }, throwable -> {
            Log.e("AAA", throwable.getMessage());
        });
    }

    private void permissionForEditDialog(boolean value, String dialogID) {
        if (value) {
            mView.replaceFragment(dialogID);
        } else {
            mView.showAppropriateMessage(0);
        }
    }

    private boolean checkAccessLevel(RealmDialogModel dialog, int value) {
        switch (dialog.getType()) {
            case PUBLIC_GROUP_TYPE:
                if (dialog.getOwnerId() == value) return true;
                return false;
            case PRIVATE_GROUP_TYPE:
                RealmList<IdsListInteger> list = dialog.getOccupantsIdsList();
                for (IdsListInteger item : list) {
                    if (item.getValue() == value) return true;
                }
                return false;
            case PRIVATE_TYPE:
                return false;
            default:
                return false;
        }
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

    @Override
    public void loadMore(String dialogId, int skip) {
        mModel.getMessagesByDialogId(mPreferencesManager.getToken(), dialogId, ApiConstant.MessageRequestParams.MESSAGE_LIMIT, skip, UNREAD_MARK)
                .map(messagesResponse -> messagesResponse.getItemMessageList())
                .doOnNext(itemMessages -> saveMessagesToDataBase(itemMessages))
                .subscribe(itemMessages -> {

                    fillAdapterListWithMessages(itemMessages);

                }, throwable -> {
                    Log.e("AAA", throwable.getMessage().toString());
                });
    }

    @Override
    public void getUsersListFromDatabase() {
        mModel.getUsersFromDatabase()
                .subscribe(loginUsers -> {
                    mView.passUsersListToAdapter(loginUsers);
                }, throwable -> {

                });
    }

    @Override
    public void getUsersAvatarsFromDatabase() {
        mModel.getUserAvatarFromDatabase()
                .subscribe(contentModel -> {
                    mView.passUsersAvatarsToAdapter(contentModel);
                }, throwable -> {

                });

    }
}
