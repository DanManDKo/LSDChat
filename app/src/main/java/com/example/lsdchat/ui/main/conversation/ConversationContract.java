package com.example.lsdchat.ui.main.conversation;


import android.content.Context;

import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.example.lsdchat.api.dialog.response.MessagesResponse;

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

//        void onNewMessageReceive();
    }

    interface View {
        Context getContext();

        String getCurrentDialogID();

        void clearEditableField();

        void showLoadProgressBar(boolean visible);

        void fillConversationAdapter(List<ItemMessage> list);

        void fillConversationAdapter(ItemMessage item);

        void loadMoreData(List<ItemMessage> list);
    }

    interface Model {
        Observable<MessagesResponse> getMessagesByDialogId(String token, String dialogId);

        Observable<ItemMessage> createDialogMessage(String token, String dialogId, String message);

        Observable<ItemMessage> getMessageByMessageID(String token, String dialogID, String messageID);
    }
}
