package com.example.lsdchat.ui.conversation;


import android.content.Context;

import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.example.lsdchat.api.dialog.response.MessagesResponse;

import java.util.List;

import rx.Observable;

public interface ConversationContract {
    interface Presenter{
        //void onDestroy();
        void getMessages(String dialogId);
    }
    interface View{
        Context getContext();
        void fillListOfMessages(List<ItemMessage> list);
    }
    interface Model{
        Observable<MessagesResponse> getMessagesByDialogId(String token, String dialogId);
    }
}
