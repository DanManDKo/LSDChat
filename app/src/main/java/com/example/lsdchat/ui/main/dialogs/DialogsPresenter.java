package com.example.lsdchat.ui.main.dialogs;


import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.widget.TextView;

import com.example.lsdchat.R;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.model.User;
import com.example.lsdchat.util.Utils;
import com.facebook.common.util.UriUtil;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;

public class DialogsPresenter implements DialogsContract.Presenter {
    private DialogsContract.View mView;
    private DialogsContract.Model mModel;
    private SharedPreferencesManager mSharedPreferencesManager;
    private User mUser;


    public DialogsPresenter(DialogsContract.View mView, SharedPreferencesManager sharedPreferencesManager) {
        this.mView = mView;
        mModel = new DialogsModel();
        this.mSharedPreferencesManager = sharedPreferencesManager;
        mUser = mModel.getCurrentUser();

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
    public void setHeaderData(CircleImageView imageView, TextView fullName, TextView email) {


        if ((mUser.getBlobId()) != 0) {
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

    private void downLoadImage(String token, long blobId, CircleImageView imageView) {
        Utils.downloadContent(blobId, token)
                .flatMap(contentResponse -> Observable.just(contentResponse.getItemContent().getImageUrl()))
                .subscribe(imageUrl -> {
//                    imageView.setImageURI(Uri.parse(imageUrl));
                    Utils.downloadImageToView(imageUrl,imageView);
                }, throwable -> {
                    Log.e("IMAGE-error", throwable.getMessage());
                });

       /* Utils.downloadImage(blobId, token)
                .subscribe(file -> {
                    imageView.setImageURI(Uri.fromFile(new File(file.getPath())));
                    Log.e("TETS", file.getPath());
                }, throwable -> {
                    mView.showMessageError(throwable);
                });*/
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
