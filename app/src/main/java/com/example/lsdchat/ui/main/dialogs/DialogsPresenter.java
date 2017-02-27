package com.example.lsdchat.ui.main.dialogs;


import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.widget.TextView;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.model.User;
import com.example.lsdchat.util.Utils;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

public class DialogsPresenter implements DialogsContract.Presenter {
    private DialogsContract.View mView;
    private DialogsContract.Model mModel;
    private SharedPreferencesManager mSharedPreferencesManager;
    private User mUser;


    public DialogsPresenter(DialogsContract.View mView, SharedPreferencesManager sharedPreferencesManager) {
        this.mView = mView;
        mModel = new DialogsModel(App.getDataManager(),App.getApiManager().getLoginService(),App.getApiManager().getDialogService());
        this.mSharedPreferencesManager = sharedPreferencesManager;
        mUser = mModel.getCurrentUser();

    }


    @Override
    public void loadDialogs() {
        mModel.getDialogs(mSharedPreferencesManager.getToken()).subscribe(dialogsResponse -> {
            mView.onDialogsLoaded(dialogsResponse.getItemDialogList());
        }, throwable -> {
            Log.e("TETS", throwable.getMessage());
        });
    }

    @Override
    public void fabClick(FloatingActionButton mFloatingActionButton) {
        mFloatingActionButton.setOnClickListener(v -> mView.startNewChat());
    }

    @Override
    public void setNavigationItemSelectedListener(NavigationView mNavigationView) {
        mNavigationView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);
            switch (item.getItemId()) {
                case R.id.item_create_new_chat:
                    mView.startNewChat();
                    break;
                case R.id.item_users:
                    mView.startUsers();
                    break;
                case R.id.item_invite_users:
                    mView.startInviteUsers();
                    break;

                case R.id.item_settings:
                    mView.startSetting();
                    break;
                case R.id.item_log_out:
                    destroySession();
                    break;
            }
            mView.getDrawerLayout().closeDrawers();
            return false;
        });
    }

    @Override
    public void setHeaderData(SimpleDraweeView imageView, TextView fullName, TextView email) {


        if (mUser.getBlobId() != 0) {
            downLoadImage(mSharedPreferencesManager.getToken(), mUser.getBlobId(), imageView);
//
        } else {
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.userpic))
                    .build();
            imageView.setImageURI(uri);
        }
        fullName.setText(mUser.getFullName());
        email.setText(mUser.getEmail());


    }

    private void downLoadImage(String token, long blobId, SimpleDraweeView imageView) {
        Utils.downloadImage(blobId, token)
                .subscribe(file -> {
                    imageView.setImageURI(Uri.fromFile(new File(file.getPath())));
                    Log.e("TETS", file.getPath());
                }, throwable -> {
                    mView.showMessageError(throwable);
                });
    }

    @Override
    public void destroySession() {

        mModel.destroySession(mSharedPreferencesManager.getToken())
                .subscribe(aVoid -> {
                    mModel.deleteUser();
                    mView.logOut();
                }, throwable -> {
                    mView.showMessageError(throwable);
                });

    }
}
