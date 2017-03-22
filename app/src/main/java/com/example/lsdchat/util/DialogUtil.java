package com.example.lsdchat.util;


import android.content.Context;

import com.example.lsdchat.App;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.ui.main.chats.dialogs.DialogsContract;
import com.example.lsdchat.ui.main.chats.dialogs.DialogsModel;
import com.example.lsdchat.ui.main.fragment.BaseFragment;
import com.example.lsdchat.util.error.ErrorInterface;
import com.example.lsdchat.util.error.NetworkConnect;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class DialogUtil {


    public static void getAllDialogAndSave(String token,Context context) {
        NetworkConnect networkConnect = ((NetworkConnect) context);
        ErrorInterface errorInterface = ((ErrorInterface) context);

        DialogsContract.Model mModel = new DialogsModel(App.getSharedPreferencesManager(context));
        List<RealmDialogModel> list = new ArrayList<>();
        if (networkConnect.isNetworkConnect()) {
            mModel.getAllDialogs(token)
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(dialogsResponse -> Observable.just(dialogsResponse.getItemDialogList()))
                    .subscribe(dialogList -> {
                        Observable.from(dialogList)
                                .subscribe(dialog -> list.add(new RealmDialogModel(dialog)));

                        mModel.saveDialog(list);

                        checkSizeDb(list,mModel);

                    }, errorInterface::showErrorDialog);
        }

    }

    private static void checkSizeDb(List<RealmDialogModel> list,DialogsContract.Model mModel) {
        if (list != null) {
            mModel.getAllDialogFromDb()
                    .filter(realmDialogModels -> realmDialogModels.size() > list.size())
                    .flatMap(Observable::from)
                    .subscribe(dialogModel -> {
                        if (!list.contains(dialogModel)) {
                            mModel.deleteItemDialog(dialogModel.getId());
                        }
                    });
        }
    }

    public static void saveImageDialog(List<RealmDialogModel> dialogList, String token) {
        ErrorInterface errorInterface = new BaseFragment();
        Observable.from(dialogList)
                .filter(dialogModel -> dialogModel.getPhoto() != null)
                .filter(dialogModel -> !dialogModel.getPhoto().isEmpty())
                .subscribe(dialogModel -> {
                            long blobId = Long.parseLong(dialogModel.getPhoto());
                            if (blobId != 0) {
                                Utils.downloadImage(blobId, token)
                                        .flatMap(file -> Observable.just(file.getAbsolutePath()))
                                        .subscribe(path -> {
                                            App.getDataManager().saveUserAvatar(new ContentModel(dialogModel.getId(), path));
                                        }, throwable -> errorInterface.showErrorDialog(throwable));

                            }
                        }
                );

    }

}
