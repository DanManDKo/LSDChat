package com.example.lsdchat.ui.main.usersinfo;


import android.util.Log;
import android.widget.ImageView;

import com.example.lsdchat.App;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.model.UserAvatar;
import com.example.lsdchat.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

public class UserInfoPresenter implements UserInfoContract.Presenter {

    private UserInfoContract.View mView;
    private SharedPreferencesManager mSharedPreferencesManager;

    public UserInfoPresenter(UserInfoContract.View mView, SharedPreferencesManager sharedPreferencesManager) {
        this.mView = mView;
        this.mSharedPreferencesManager = sharedPreferencesManager;
    }


    @Override
    public String getImagePath(int userId) {
        List<UserAvatar> mUserAvatars = App.getDataManager().getListUserAvatar();
        Map<Integer, String> mMapAvatar = new HashMap<>();
        for (UserAvatar user: mUserAvatars) {
            mMapAvatar.put(user.getUserId(),user.getImagePath());
        }

        return mMapAvatar.get(userId);
    }
}
