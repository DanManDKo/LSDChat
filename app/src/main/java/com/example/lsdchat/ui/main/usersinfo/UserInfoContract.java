package com.example.lsdchat.ui.main.usersinfo;


import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.lsdchat.api.login.model.LoginUser;

import rx.Observable;

public interface UserInfoContract {

    interface Model {
        Observable<String> getImagePath(int userId);
    }

    interface View {
        void setImagePath(String path);

    }

    interface Presenter {
       void getUserAvatar(int userId);


    }

}
