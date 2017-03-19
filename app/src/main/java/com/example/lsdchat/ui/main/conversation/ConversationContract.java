package com.example.lsdchat.ui.main.conversation;


import android.content.Context;

import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.example.lsdchat.api.dialog.response.MessagesResponse;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.model.User;

import java.util.List;

import rx.Observable;

// TODO: 3/9/17 [Code Review] this should be in Fragment
public interface ConversationContract {
    interface Presenter {

        void onDestroy();

        void onUnregisterBroadcastReceiver();

        void onRegisterBroadcastReceiver();

        void getMessages(String dialogId);

        void fillAdapterListWithMessages(String dialogId);

        void addNewMessageToAdapterList(String messageId);

        void onAdapterItemClicked(String id, int position);

        void sendMessage(String dialogId, String message, String sendTo);

        boolean isOnline();

        void loadMoreFromDataBase(String dialogId, int page);

        void navigateToEditchatFragment(String dialogId);

//        void onNewMessageReceive();
    }

    interface View {
        void replaceFragment(String dialogId);

        Context getContext();

        String getCurrentDialogID();

        void clearEditableField();

        void showLoadProgressBar(boolean visible);

        void fillConversationAdapter(List<ItemMessage> list);

        void fillConversationAdapter(ItemMessage item);

        void loadMoreData(List<ItemMessage> list);
    }

    interface Model {
        Observable<MessagesResponse> getMessagesByDialogId(String token, String dialogId, int readMark);

        Observable<ItemMessage> createDialogMessage(String token, String dialogId, String message);

        Observable<MessagesResponse> getMessageByMessageID(String token, String dialogID, String messageID);

        Observable<RealmDialogModel> getDialogFromDatabase(String dialogID);

        Observable<User> getCurrentUserFromDatabase();
    }
}
