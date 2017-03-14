package com.example.lsdchat.ui.main.editchat;


import android.content.Context;
import android.util.Log;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.model.RealmDialogModel;

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
                .subscribe(realmDialogModel -> {
//                    mView.fillDialogInformation(realmDialogModel.getName());
                    Log.e("AAA - photo =", realmDialogModel.getPhoto().toString());
                    Log.e("AAA - dialog name =", realmDialogModel.getName().toString());
                }, throwable -> {
                    Log.e("AAA", throwable.getMessage().toString());
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

    private void fillInformationInFields(RealmDialogModel object) {
//        mView.fillDialogInformation(object.getName());
        CreateDialogRequest body = new CreateDialogRequest(object.getName());

        mModel.updateDialog(object.getName(), mPreferencesManager.getToken(), body)
                .subscribe(itemDialog -> {

                    mView.fillDialogInformation(itemDialog.getName());
                }, throwable -> {

                });
    }
}
