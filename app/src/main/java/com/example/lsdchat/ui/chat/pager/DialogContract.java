package com.example.lsdchat.ui.chat.pager;

import android.app.Activity;

import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.manager.model.RealmItemDialog;

import java.util.List;

import rx.Observable;

/**
 * Created by User on 12.02.2017.
 */

public interface DialogContract {
    interface View {
        void onDialoguesLoaded(List<RealmItemDialog> dialogs);


        int getType();

        Activity getActivity();
    }

    interface Model {
        Observable<DialogsResponse> getAllDialogs(String token);

        List<RealmItemDialog> getDialogByType(int type);

        boolean saveDialogsToDb(List<RealmItemDialog> itemDialogs);

        void onDestroy();
    }

    interface Presenter {

        void getAllDialogsAndSave();

        String getToken();

        void onDestroy();
    }
}
