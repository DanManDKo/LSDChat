package com.example.lsdchat.ui.main.chats;


import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.model.DialogModel;
import com.example.lsdchat.model.User;
import com.example.lsdchat.model.UserAvatar;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;

public interface ChatsContract {

    interface View {

        void navigateToUsers();
        void navigateToInviteUsers();
        void navigateToSetting();

        void navigateToLoginActivity();
        void showMessageError(Throwable throwable);

    }

    interface Presenter {

        Observable<String> getUserAvatar();
        User getUserModel();

        void onLogout();


    }

    interface Model {

        User getCurrentUser();

        Observable<Void> destroySession(String token);

        void deleteUser();

        Observable<DialogsResponse> getAllDialogs(String token);

        void saveDialog(List<DialogModel> dialogList);

        List<DialogModel> getDialogsByType(int type);

        String getToken();

        Observable<List<UserAvatar>> getObservableUserAvatar();
    }

}