package com.example.lsdchat.ui.main.dialogs;


import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.model.User;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import rx.Observable;

public interface DialogsContract {

    interface View {
        void startNewChat();

        DrawerLayout getDrawerLayout();

        void onDialogsLoaded(List<ItemDialog> dialogs);

        void startUsers();

        void startInviteUsers();

        void startSetting();

        void logOut();

        void showMessageError(Throwable throwable);

    }

    interface Presenter {
        void loadDialogs();

        void fabClick(FloatingActionButton mFloatingActionButton);

        void setNavigationItemSelectedListener(NavigationView mNavigationView);

        void setHeaderData(SimpleDraweeView imageView, TextView fullName, TextView email);

        void destroySession();
    }

    interface Model {

        User getCurrentUser();

        Observable<DialogsResponse> getDialogs(String token);

        Observable<Void> destroySession(String token);

        void deleteUser();
    }

}
