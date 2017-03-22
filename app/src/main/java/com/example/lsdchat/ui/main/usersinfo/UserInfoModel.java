package com.example.lsdchat.ui.main.usersinfo;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.DialogService;
import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.util.DialogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class UserInfoModel implements UserInfoContract.Model{
    private DataManager mDataManager;
    private DialogService mDialogService;
    private SharedPreferencesManager mSharedPreferencesManager;

    public UserInfoModel(DataManager mDataManager,DialogService mDialogService,SharedPreferencesManager mSharedPreferencesManager) {
        this.mDataManager = mDataManager;
        this.mDialogService = mDialogService;
        this.mSharedPreferencesManager = mSharedPreferencesManager;
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

    @Override
    public Observable<ItemDialog> createDialog(CreateDialogRequest createDialogRequest) {
        return mDialogService.createDialog(mSharedPreferencesManager.getToken(), createDialogRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void saveDialog(RealmDialogModel itemDialog) {
       mDataManager.insertDialogToDB(itemDialog);
    }

}
