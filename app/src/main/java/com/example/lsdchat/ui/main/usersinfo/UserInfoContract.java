package com.example.lsdchat.ui.main.usersinfo;


import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.lsdchat.api.login.model.LoginUser;

public interface UserInfoContract {

    interface View {


    }

    interface Presenter {
        void setImageView(ImageView imageView, long blobId);


    }

}
