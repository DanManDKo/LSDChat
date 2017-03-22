package com.example.lsdchat.ui.main.usersinfo;


import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.model.RealmDialogModel;

import rx.Observable;

public interface UserInfoContract {

    interface Model {
        Observable<String> getImagePath(int userId);

        Observable<ItemDialog> createDialog(CreateDialogRequest createDialogRequest);

        void saveDialog(RealmDialogModel itemDialog);
    }

    interface View {
        void setImagePath(String path);
        void showDialogError(Throwable throwable);

        void navigateToChat(Fragment fragment);
    }

    interface Presenter {
       void getUserAvatar(int userId);

        void createDialog(int idUser);

    }

}
