package com.example.lsdchat.ui.main.editchat;


import android.content.Context;

import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.api.dialog.response.MessagesResponse;

import rx.Observable;

public interface EditchatContract {
    interface Presenter extends BasePresenter {
        void loadDialogCredentials(String dialogID);

    }

    interface View {
        Context getContext();

        void fillDialogInformation(String dialogName);
    }

    interface Model {
        Observable<DialogsResponse> getDialogByID(String token, String dialogID);

        Observable<ItemDialog> updateDialog(String dialogID, String token, CreateDialogRequest body);
    }
}
