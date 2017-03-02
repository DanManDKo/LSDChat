package com.example.lsdchat.ui.main.dialogs;


import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

import com.example.lsdchat.model.User;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;

public interface DialogsContract {

    interface View {
        void startNewChat();
        DrawerLayout getDrawerLayout();

        void startUsers();
        void startInviteUsers();
        void startSetting();

        void logOut();
        void showMessageError(Throwable throwable);

    }

    interface Presenter {

        void fabClick(FloatingActionButton mFloatingActionButton);
        void setNavigationItemSelectedListener(NavigationView mNavigationView);

        void setHeaderData(CircleImageView imageView, TextView fullName, TextView email);

        void destroySession();
    }

    interface Model {

        User getCurrentUser();

        Observable<Void> destroySession(String token);

        void deleteUser();
    }

}