package com.example.lsdchat.util;


import android.content.Context;
import android.util.Log;

import com.example.lsdchat.App;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.ui.main.chats.dialogs.DialogsContract;
import com.example.lsdchat.ui.main.chats.dialogs.DialogsModel;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class DialogUtil {

    public static void getAllDialogAndSave(SharedPreferencesManager sharedPreferencesManager) {
        DialogsContract.Model model = new DialogsModel(sharedPreferencesManager);
        List<RealmDialogModel> list = new ArrayList<>();
        model.getAllDialogs(model.getToken())
                .flatMap(dialogsResponse -> Observable.just(dialogsResponse.getItemDialogList()))
                .subscribe(dialogList -> {
                    Observable.from(dialogList)
                            .subscribe(dialog -> list.add(new RealmDialogModel(dialog)));

                    model.saveDialog(list);

                }, throwable -> {
                    Log.e("getAllDialogAndSave", throwable.getMessage());
                });

    }

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
