package com.example.lsdchat.ui.main.conversation;


import android.content.Context;

import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.example.lsdchat.api.dialog.response.MessagesResponse;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.model.ContentModel;
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

        void getMessages(String dialogId, int limit, int skip);

        void fillAdapterListWithMessages(List<ItemMessage> list);

        void fillAdapterListWithMessages(String dialogID);

        void addNewMessageToAdapterList(String messageId);

        void onAdapterItemClicked(String id, int position);

        void sendMessage(String dialogId, String message, String sendTo, int dialogType);

        void loadMoreFromDataBase(String dialogId, int page);

        void loadMore(String dialogId, int page);

        void navigateToEditchatFragment(String dialogId);

        void getUsersListFromDatabase();

        void getUsersAvatarsFromDatabase();

        boolean isOnline();

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

        void passUsersListToAdapter(List<LoginUser> users);

        void passUsersAvatarsToAdapter(List<ContentModel> users);

        void showAppropriateMessage(int msg);

        void showErrorDialog(String message);
    }

    interface Model {
        Observable<MessagesResponse> getMessagesByDialogId(String token, String dialogId, int limit, int skip, int readMark);

        Observable<ItemMessage> createDialogMessage(String token, String dialogId, String message);

        Observable<MessagesResponse> getMessageByMessageID(String token, String dialogID, String messageID, int readMark);

        Observable<RealmDialogModel> getDialogFromDatabase(String dialogID);

        Observable<User> getCurrentUserFromDatabase();

        Observable<List<LoginUser>> getUsersFromDatabase();

        Observable<List<ContentModel>> getUserAvatarFromDatabase();
    }
}
