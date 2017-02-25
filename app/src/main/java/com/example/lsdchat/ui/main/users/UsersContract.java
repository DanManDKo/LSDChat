package com.example.lsdchat.ui.main.users;


import com.example.lsdchat.api.dialog.model.ItemUser;
import com.example.lsdchat.api.dialog.response.UserListResponse;
import com.example.lsdchat.model.UserQuick;

import io.realm.RealmResults;
import rx.Observable;

public interface UsersContract {

    interface Model {
        Observable<UserListResponse> getUserList(String token);
        void insertUsersQuick(UserQuick user);

        RealmResults<UserQuick> getUsersQuick();
    }

    interface View {

    }

    interface Presenter {

        String getToken();
        void downloadImage(long blobId, String token, ItemUser user);

        void getUserList();

        RealmResults<UserQuick> getUsersQuick();
    }

}
