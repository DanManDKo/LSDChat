package com.example.lsdchat.util;


import android.util.Log;

import com.example.lsdchat.App;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.model.RealmDialogModel;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class DialogUtil {

    public static void saveImageDialog(List<RealmDialogModel> dialogList, String token) {
        Observable.from(dialogList)
                .observeOn(AndroidSchedulers.mainThread())
                .filter(dialogModel -> dialogModel.getPhoto() != null)
                .filter(dialogModel -> !dialogModel.getPhoto().isEmpty())
                .subscribe(dialogModel ->
                        Utils.downloadImage(Long.parseLong(dialogModel.getPhoto()), token)
                                .flatMap(file -> Observable.just(file.getAbsolutePath()))
                                .subscribe(path -> {
                                    App.getDataManager().saveUserAvatar(new ContentModel(dialogModel.getId(), path));
                                }, throwable -> {
                                    Log.e("getImage", throwable.getMessage());
                                }));

    }

}
