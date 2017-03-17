package com.example.lsdchat.ui.main.usersinfo;

import com.example.lsdchat.App;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.ContentModel;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;


public class UserInfoModel implements UserInfoContract.Model{
    private DataManager mDataManager;

    public UserInfoModel(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public Observable<String> getImagePath(int userId) {
        Map<String, String> mapAvatar = new HashMap<>();
        return Observable.fromCallable(() -> {
            mDataManager.getObservableUserAvatar()
                    .subscribe(userAvatars -> {
                        for (ContentModel user : userAvatars) {
                            mapAvatar.put(user.getId(), user.getImagePath());
                        }
                    });
            return mapAvatar.get(String.valueOf(userId));
        });
    }
}
