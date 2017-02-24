package com.example.lsdchat.ui.main.users;


import android.net.Uri;
import android.util.Log;

import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.util.Utils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

public class UsersPresenter implements UsersContract.Presenter {
    private UsersContract.View mView;
    private SharedPreferencesManager mSharedPreferencesManager;
    private UsersContract.Model mModel;


    public UsersPresenter(UsersContract.View mView, SharedPreferencesManager sharedPreferencesManager) {
        this.mView = mView;
        this.mSharedPreferencesManager = sharedPreferencesManager;
        mModel = new UsersModel();

    }


    @Override
    public void downloadImage(long blobId, String token, SimpleDraweeView imageView) {
        Utils.downloadImage(blobId, token)
                .subscribe(file -> {
                    imageView.setImageURI(Uri.fromFile(new File(file.getPath())));
                }, throwable -> {
                    Log.e("TETS", throwable.getMessage());
                });
    }

    @Override
    public String getToken() {
        return mSharedPreferencesManager.getToken();
    }
}
