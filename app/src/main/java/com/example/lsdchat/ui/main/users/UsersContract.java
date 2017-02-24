package com.example.lsdchat.ui.main.users;


import com.example.lsdchat.api.dialog.response.UserListResponse;
import com.facebook.drawee.view.SimpleDraweeView;

import rx.Observable;

public interface UsersContract {

    interface Model {
        Observable<UserListResponse> getUserList(String token);

    }

    interface View {

    }

    interface Presenter {

        String getToken();
        void downloadImage(long blobId, String token, SimpleDraweeView imageView);
    }

}
