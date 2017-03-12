package com.example.lsdchat.ui.main.chats;


import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.model.DialogModel;
import com.example.lsdchat.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;

public interface ChatsContract {

    interface View {

        void navigateToUsers();
        void navigateToInviteUsers();
        void navigateToSetting();

        // TODO: 3/9/17 [Code Review] this is also not a View action, rename pls
        void logOut();
        void showMessageError(Throwable throwable);

    }

    interface Presenter {

        // TODO: 3/9/17 [Code Review] maybe it is better to incapsulate fetching this data in presenter's
        // constructor, if we do not need any callbacks here
        void getAllDialogAndSave();




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

        String getToken();
    }

}