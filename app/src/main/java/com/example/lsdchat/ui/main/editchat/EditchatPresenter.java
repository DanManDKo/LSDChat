package com.example.lsdchat.ui.main.editchat;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.model.IdsListInteger;
import com.example.lsdchat.model.RealmDialogModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditchatPresenter implements EditchatContract.Presenter {
    private EditchatContract.View mView;
    private EditchatContract.Model mModel;

    private Context mContext;
    private SharedPreferencesManager mPreferencesManager;
    private int mDialogType;

    public EditchatPresenter(EditchatContract.View view, EditchatContract.Model model, SharedPreferencesManager manager) {
        Log.e("AAA", "constructor");
        mView = view;
        mContext = view.getContext();
        mModel = model;
        mPreferencesManager = manager;
    }

    @Override
    public void onDetach() {
        mView = null;
    }

    @Override
    public void onDestroy() {
        //nothing to clean at this point
    }

    @Override
    public void loadDialogCredentials(String dialogID) {
        mModel.getDialogFromDatabase(dialogID)
                .subscribe(dialogModel -> {
                    Log.e("AAA - DIALOG_NAME", dialogModel.getName().toString());
                    mView.fillDialogNameField(dialogModel.getName());

                    prepareAndShowDialogInformation(dialogModel);
                }, throwable -> {
                    Log.e("AAA - nameError", throwable.getMessage().toString());
                });

//        RealmDialogModel dialog = App.getDataManager().getDialogByID(dialogID);
//        mView.fillDialogInformation(dialog.getName());
//        Log.d("AAA", dialog.getName().toString());
//        mModel.getDialogByID(mPreferencesManager.getToken(), dialogID)
//                .map(dialogsResponse -> dialogsResponse.getItemDialogList().get(0))
//                .doOnNext(itemDialog -> mDialogType = itemDialog.getType())
//                .subscribe(itemDialog -> {
//                    fillInformationInFields(itemDialog);
//                }, throwable -> {
//
//                });
    }

    private void prepareAndShowDialogInformation(RealmDialogModel dialogModel) {
        mModel.getDialogAvatarFromDatabase(dialogModel.getId())
                .map(contentModel -> {
                    if (contentModel != null) {
                        if (!contentModel.getImagePath().equals("0"))
                            return contentModel.getImagePath();
                        return "";
                    }
                    return "";
                }).subscribe(s -> {
                    Log.e("AAA - AVATAR_STRING", "avatar string <" + s + ">");

                    mView.showDialogAvatar(Uri.fromFile(new File(s)));
                },
                throwable -> {
                    Log.e("AAA - avatarError", throwable.getMessage().toString());
                });

        int dialogType = dialogModel.getType();
        List<Integer> dialogOccupantsIDs = new ArrayList<>();
        for (IdsListInteger id : dialogModel.getOccupantsIdsList()) {
            dialogOccupantsIDs.add(id.getValue());
        }

        mModel.getAppUsersFromDatabase()
                .subscribe(loginUsers -> {
                            Log.e("AAA - APP_USERS", String.valueOf(loginUsers.size()));
                            mView.fillDialogAdapter(dialogOccupantsIDs, loginUsers, dialogType);
                        },
                        throwable -> {
                            Log.e("AAA - usersError", throwable.getMessage());
                        });
    }

    @Override
    public List<ContentModel> getAvatarsFromDatabase() {
        return App.getDataManager().getUserAvatars();
    }

    private void fillInformationInFields(RealmDialogModel object) {
        CreateDialogRequest body = new CreateDialogRequest(object.getName());

        mModel.updateDialog(object.getName(), mPreferencesManager.getToken(), body)
                .subscribe(itemDialog -> {

                }, throwable -> {

                });
    }
}
