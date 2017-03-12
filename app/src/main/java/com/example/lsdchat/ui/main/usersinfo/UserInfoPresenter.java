package com.example.lsdchat.ui.main.usersinfo;


import android.util.Log;
import android.widget.ImageView;

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
                        Utils.setImageByUrl(imageUrl, imageView);
                    }, throwable -> {
                        Log.e("IMAGE-error", throwable.getMessage());
                    });

        }
    }


}
