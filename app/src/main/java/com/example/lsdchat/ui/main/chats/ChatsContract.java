package com.example.lsdchat.ui.main.chats;


import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.model.DialogModel;
import com.example.lsdchat.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;

public interface ChatsContract {

    interface View {
        // TODO: 3/9/17 [Code Review] pls rename to navigate... or so
        void startNewChat();
        // TODO: 3/9/17 [Code Review] presenter should know nothing about Android SDK
        // you do not need some getter methods at all in View layer
        DrawerLayout getDrawerLayout();

        // TODO: 3/9/17 [Code Review] navigate... ?
        void startUsers();
        void startInviteUsers();
        void startSetting();

        // TODO: 3/9/17 [Code Review] this is also not a View action, rename pls
        void logOut();
        void showMessageError(Throwable throwable);

    }

    interface Presenter {

        // TODO: 3/9/17 [Code Review] maybe it is better to incapsulate fetching this data in presenter's
        // constructor, if we do not need any callbacks here
        void getAllDialogAndSave();

        // TODO: 3/9/17 [Code Review] this should be in Fragment
        void fabClick(FloatingActionButton mFloatingActionButton);
        // TODO: 3/9/17 [Code Review] this should be in Fragment
        void setNavigationItemSelectedListener(NavigationView mNavigationView);

        // TODO: 3/9/17 [Code Review] this should be in Fragment (make displayUserInfo(User user) method or
        // separate ones setUserAvatar(String url), setUserName() ... in View layer)
        void setHeaderData(CircleImageView imageView, TextView fullName, TextView email);

        // TODO: 3/9/17 [Code Review] this should be something like onLogout()
        void destroySession();

        // TODO: 3/9/17 [Code Review] this should be in Fragment
        List<Fragment> setFragmentList();
    }

    interface Model {

        User getCurrentUser();

        Observable<Void> destroySession(String token);

        void deleteUser();

        Observable<DialogsResponse> getAllDialogs(String token);

        void saveDialog(List<DialogModel> dialogList);

        List<DialogModel> getDialogsByType(int type);
    }

}