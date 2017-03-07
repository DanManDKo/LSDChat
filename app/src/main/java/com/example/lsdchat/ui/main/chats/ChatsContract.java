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
        void startNewChat();
        DrawerLayout getDrawerLayout();

        void startUsers();
        void startInviteUsers();
        void startSetting();

        void logOut();
        void showMessageError(Throwable throwable);

    }

    interface Presenter {

        void getAllDialogAndSave();

        void fabClick(FloatingActionButton mFloatingActionButton);
        void setNavigationItemSelectedListener(NavigationView mNavigationView);

        void setHeaderData(CircleImageView imageView, TextView fullName, TextView email);

        void destroySession();

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