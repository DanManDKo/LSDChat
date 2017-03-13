package com.example.lsdchat.ui.main.chats.dialogs;


import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.model.RealmDialogModel;

import java.util.List;

import rx.Observable;

public interface DialogsContract {

    interface Model {
        List<RealmDialogModel> getDialogsByType(int type);
        Observable<DialogsResponse> getAllDialogs(String token);

        void saveDialog(List<RealmDialogModel> dialogList);

        String getToken();

    }

    interface View {
        void setListDialog(List<RealmDialogModel> list);
        int getType();

        void navigateToChat(Fragment fragment);
    }

    interface Presenter {
        List<RealmDialogModel> showDialogs(int type);
        void getAllDialogAndSave();

        void setOnRefreshListener(SwipeRefreshLayout swipeRefreshLayout);

        void setClickRl(RealmDialogModel realmDialogModel);
    }

}
