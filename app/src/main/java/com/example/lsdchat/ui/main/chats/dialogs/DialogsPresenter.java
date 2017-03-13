package com.example.lsdchat.ui.main.chats.dialogs;


import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.ui.main.conversation.ConversationFragment;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class DialogsPresenter implements DialogsContract.Presenter {

    private DialogsContract.View mView;
    private DialogsContract.Model mModel;


    public DialogsPresenter(DialogsContract.View mView, DialogsContract.Model mModel) {
        this.mView = mView;
        this.mModel = mModel;

    }

    @Override
    public List<RealmDialogModel> showDialogs(int type) {
        if (type == ApiConstant.TYPE_DIALOG_PUBLIC) {
            return mModel.getDialogsByType(ApiConstant.TYPE_DIALOG_PUBLIC);

        } else {
            List<RealmDialogModel> list = new ArrayList<>();
            list.addAll(mModel.getDialogsByType(ApiConstant.TYPE_DIALOG_GROUP));
            list.addAll(mModel.getDialogsByType(ApiConstant.TYPE_DIALOG_PRIVATE));
            return list;
        }
    }

    @Override
    public void getAllDialogAndSave() {
        List<RealmDialogModel> list = new ArrayList<>();
        mModel.getAllDialogs(mModel.getToken())
                .flatMap(dialogsResponse -> Observable.just(dialogsResponse.getItemDialogList()))
                .subscribe(dialogList -> {

                    Observable.from(dialogList)
                            .subscribe(dialog -> list.add(new RealmDialogModel(dialog)));

                    mModel.saveDialog(list);

                    mView.setListDialog(showDialogs(mView.getType()));
                }, throwable -> {
                    Log.e("getAllDialogAndSave", throwable.getMessage());
                });

    }


    @Override
    public void setOnRefreshListener(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getAllDialogAndSave();
            swipeRefreshLayout.setRefreshing(false);
        });
    }


    @Override
    public void setClickRl(RealmDialogModel realmDialogModel) {
        mView.navigateToChat(ConversationFragment
                .newInstance(realmDialogModel.getId(), realmDialogModel.getType(), realmDialogModel.getName()));

    }

    @Override
    public Observable<List<ContentModel>> getObservableUserAvatar() {
        return mModel.getObservableUserAvatar();
    }

    @Override
    public Observable<List<RealmDialogModel>> getObservableDialogsByType(int type) {
        return mModel.getObservableDialogsByType(type);
    }
}
