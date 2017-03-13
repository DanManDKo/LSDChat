package com.example.lsdchat.ui.main.usersinfo;


import com.example.lsdchat.App;
import com.example.lsdchat.model.ContentModel;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

public class UserInfoPresenter implements UserInfoContract.Presenter {

    private UserInfoContract.View mView;

    public UserInfoPresenter(UserInfoContract.View mView) {
        this.mView = mView;

    }


    @Override
    public Observable<String> getUserAvatar(int userId) {
        Map<String, String> mapAvatar = new HashMap<>();
        return Observable.fromCallable(() -> {
            App.getDataManager().getObservableUserAvatar()
                    .subscribe(userAvatars -> {
                        for (ContentModel user : userAvatars) {
                            mapAvatar.put(user.getId(), user.getImagePath());
                        }
                    });
            return mapAvatar.get(String.valueOf(userId));
        });

    }
}
