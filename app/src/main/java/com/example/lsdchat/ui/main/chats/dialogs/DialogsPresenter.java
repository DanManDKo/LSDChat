package com.example.lsdchat.ui.main.chats.dialogs;


import android.util.Log;

import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.model.DialogModel;

import java.util.ArrayList;
import java.util.List;

public class DialogsPresenter implements DialogsContract.Presenter {

    private DialogsContract.View mView;
    private DialogsContract.Model mModel;

    public DialogsPresenter(DialogsContract.View mView) {
        this.mView = mView;
        mModel = new DialogsModel();
    }

    @Override
    public void showDialogs(int type) {
        if (type == ApiConstant.TYPE_DIALOG_PUBLIC) {
            List<DialogModel> list = mModel.getDialogsByType(ApiConstant.TYPE_DIALOG_PUBLIC);
            Log.e("showDialogs", String.valueOf(list.size()));
        }
        else {
            List<DialogModel> list =new ArrayList<>();
            list.addAll(mModel.getDialogsByType(ApiConstant.TYPE_DIALOG_GROUP));
            list.addAll(mModel.getDialogsByType(ApiConstant.TYPE_DIALOG_PRIVATE));
            Log.e("showDialogs", String.valueOf(list.size()));
        }
    }
}
