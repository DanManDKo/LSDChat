package com.example.lsdchat.ui.chat.pager;

import android.app.Activity;

import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.api.dialog.response.DialogsResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by User on 12.02.2017.
 */

public interface DialogContract {
    interface View {
        void onDialoguesLoaded(List<ItemDialog> dialogs);

        Activity getActivity();
    }

    interface Model {
        Observable<DialogsResponse> getAllDialogs(String token);

        void onDestroy();
    }

    interface Presenter {
        List<ItemDialog> getAllDialogs();
        String getToken();

        void onDestroy();
    }
}
