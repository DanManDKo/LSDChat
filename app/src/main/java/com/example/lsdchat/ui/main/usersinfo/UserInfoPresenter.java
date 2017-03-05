package com.example.lsdchat.ui.main.usersinfo;


import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.util.Utils;

import rx.Observable;

public class UserInfoPresenter implements UserInfoContract.Presenter {

    private UserInfoContract.View mView;
    private SharedPreferencesManager mSharedPreferencesManager;

    public UserInfoPresenter(UserInfoContract.View mView, SharedPreferencesManager sharedPreferencesManager) {
        this.mView = mView;
        this.mSharedPreferencesManager = sharedPreferencesManager;
    }


    @Override
    public void setImageView(ImageView imageView, long blobId) {
        if (blobId != 0) {
            Utils.downloadContent(blobId, mSharedPreferencesManager.getToken())
                    .flatMap(contentResponse -> Observable.just(contentResponse.getItemContent().getImageUrl()))
                    .subscribe(imageUrl -> {
                        Utils.downloadImageToView(imageUrl, imageView);
                    }, throwable -> {
                        Log.e("IMAGE-error", throwable.getMessage());
                    });

        }
    }

    @Override
    public void setOnClickListenerRlEmail(RelativeLayout rlEmail, String email) {
        rlEmail.setOnClickListener(v -> {
            if (email != null && !email.isEmpty()) {
                mView.navigateSendEmail(email);
            }
        });
    }

    @Override
    public void setOnClickListenerRlPhone(RelativeLayout rlPhone, String phone) {
        rlPhone.setOnClickListener(v -> {
            if (phone != null && !phone.isEmpty()) {
                mView.navigateDial(phone);
            }
        });
    }

    @Override
    public void setOnClickListenerRlWeb(RelativeLayout rlWeb, String web) {
        rlWeb.setOnClickListener(v -> {
            if (web != null && !web.isEmpty()) {
                mView.navigateWeb(web);
            }
        });
    }


    @Override
    public void setOnClickListenerFab(FloatingActionButton fab, LoginUser user) {
// navigate to chat
    }


}
