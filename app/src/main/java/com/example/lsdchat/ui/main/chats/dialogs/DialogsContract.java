package com.example.lsdchat.ui.main.chats.dialogs;


import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.model.RealmDialogModel;

import java.util.List;

import rx.Observable;

public interface DialogsContract {

    interface Model {
        Observable<DialogsResponse> getAllDialogs(String token);

        void saveDialog(List<RealmDialogModel> dialogList);


        Observable<List<RealmDialogModel>> getAllDialogFromDb();

        void deleteItemDialog(String idDialog);

        String getToken();

        Observable<List<ContentModel>> getObservableUserAvatar();
        Observable<List<RealmDialogModel>> getObservableDialogsByType(int type);
    }

    interface View {
        void setListDialog(List<RealmDialogModel> list);
        int getType();

        void navigateToChat(Fragment fragment);

        void setContentModelList(List<ContentModel> contentModelList);

        void showErrorDialog(Throwable throwable);

        void deleteItemDialog(RealmDialogModel item);
    }

    interface Presenter {

        void getDialogFilterList(int typeDialog,String query);

        void getObservableDialogByType(int type);

        void setClickRl(RealmDialogModel realmDialogModel);

        void getContentModelList();

        void getAllDialogAndSave();
    }

}
