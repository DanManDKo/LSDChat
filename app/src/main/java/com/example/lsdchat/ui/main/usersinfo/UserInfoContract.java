package com.example.lsdchat.ui.main.usersinfo;


import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.lsdchat.api.login.model.LoginUser;

public interface UserInfoContract {

    interface View {
        void navigateDial(String phone);
        void navigateSendEmail(String email);
        void navigateWeb(String website);

    }

    interface Presenter {
        void setImageView(ImageView imageView, long blobId);
        void setOnClickListenerRlEmail(RelativeLayout rlEmail,String email);
        void setOnClickListenerRlPhone(RelativeLayout rlPhone,String phone);
        void setOnClickListenerRlWeb(RelativeLayout rlWeb,String web);
        void setOnClickListenerFab(FloatingActionButton fab, LoginUser user);

    }

}
