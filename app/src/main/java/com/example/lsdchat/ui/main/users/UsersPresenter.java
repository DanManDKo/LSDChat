package com.example.lsdchat.ui.main.users;


import android.util.Log;

import com.example.lsdchat.api.dialog.model.ItemUser;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.util.Utils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;

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
    public String getToken() {
        return mSharedPreferencesManager.getToken();
    }

    @Override
    public void getUserList() {
        mModel.getUserList(getToken())
                .subscribe(userListResponse -> {
                    List<ItemUser> itemUsers = userListResponse.getItemUserList();

                    if (userListResponse.getTotalEntries() < (mModel.getUsersQuick().size() + 2)) {
                        mModel.deleteAllUSerQiuck();
                        getU(itemUsers);
                    } else
                        getU(itemUsers);

                }, throwable -> {
                    Log.e("getUserList-error", throwable.getMessage());
                });

    }


    private void getU(List<ItemUser> itemUsers) {
        Observable.from(itemUsers)
                .flatMap(user -> Observable.just(user.getUser()))
                .subscribe(loginUser -> {

                    mModel.insetUsersQuick(loginUser);

                }, throwable -> {
                    Log.e("getUserList-error", throwable.getMessage());
                });
    }


    @Override
    public void setImageView(CircleImageView imageView, long blobId) {
        if (blobId != 0) {
            Utils.downloadContent(blobId, getToken())
                    .flatMap(contentResponse -> Observable.just(contentResponse.getItemContent().getImageUrl()))
                    .subscribe(imageUrl -> {
                        Utils.downloadImageToView(imageUrl,imageView);
                    }, throwable -> {
                        Log.e("IMAGE-error", throwable.getMessage());
                    });

        }


    }



    private List<LoginUser> filter(List<LoginUser> models, String query) {
        query = query.toLowerCase();
        List<LoginUser> filteredModelList = new ArrayList<>();
        for (LoginUser model : models) {
            String text = model.getFullName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public List<LoginUser> getUsersQuickList(String sort) {
        return mModel.getUsersQuickList(sort);
    }
}
