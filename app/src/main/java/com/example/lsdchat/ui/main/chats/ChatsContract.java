package com.example.lsdchat.ui.main.chats;


import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.model.User;
import com.example.lsdchat.model.ContentModel;

import java.util.List;

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

        void saveDialog(List<RealmDialogModel> dialogList);

        List<RealmDialogModel> getDialogsByType(int type);

        String getToken();

        Observable<List<ContentModel>> getObservableUserAvatar();
    }

}