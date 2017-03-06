package com.example.lsdchat.ui.main.chats.dialogs;


import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.model.DialogModel;
import com.example.lsdchat.util.Utils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;

public class DialogsPresenter implements DialogsContract.Presenter {

    private DialogsContract.View mView;
    private DialogsContract.Model mModel;
    private SharedPreferencesManager mSharedPreferencesManager;

    public DialogsPresenter(DialogsContract.View mView, SharedPreferencesManager mSharedPreferencesManager) {
        this.mView = mView;
        mModel = new DialogsModel();
        this.mSharedPreferencesManager = mSharedPreferencesManager;
    }

    @Override
    public List<DialogModel> showDialogs(int type) {
        if (type == ApiConstant.TYPE_DIALOG_PUBLIC) {
            return mModel.getDialogsByType(ApiConstant.TYPE_DIALOG_PUBLIC);

        } else {
            List<DialogModel> list = new ArrayList<>();
            list.addAll(mModel.getDialogsByType(ApiConstant.TYPE_DIALOG_GROUP));
            list.addAll(mModel.getDialogsByType(ApiConstant.TYPE_DIALOG_PRIVATE));
            return list;
        }
    }

    @Override
    public void getAllDialogAndSave() {
        List<DialogModel> list = new ArrayList<>();
        mModel.getAllDialogs(mSharedPreferencesManager.getToken())
                .flatMap(dialogsResponse -> Observable.just(dialogsResponse.getItemDialogList()))
                .subscribe(dialogList -> {

                    Observable.from(dialogList)
                            .subscribe(dialog -> list.add(new DialogModel(dialog)));

                    mModel.saveDialog(list);

                    mView.initAdapter(showDialogs(mView.getType()));
                }, throwable -> {
                    Log.e("getAllDialogAndSave", throwable.getMessage());
                });

    }


    @Override
    public void setNewMessageCounter(TextView textView, DialogModel dialogModel) {
        if (dialogModel.getUnreadMessagesCount() != 0) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(String.valueOf(dialogModel.getUnreadMessagesCount()));
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setImageDialog(CircleImageView imageView, DialogModel dialogModel) {
        if (dialogModel.getPhoto() != null && !dialogModel.getPhoto().isEmpty()) {
            long blobId = Long.parseLong(dialogModel.getPhoto());
            Utils.downloadContent(blobId, mSharedPreferencesManager.getToken())
                    .flatMap(contentResponse -> Observable.just(contentResponse.getItemContent().getImageUrl()))
                    .subscribe(imageUrl -> Utils.downloadImageToView(imageUrl, imageView), throwable -> {
                        Log.e("IMAGE-error", throwable.getMessage());
                    });

        }
    }

    @Override
    public void setOnRefreshListener(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getAllDialogAndSave();
            swipeRefreshLayout.setRefreshing(false);
        });
    }
}
