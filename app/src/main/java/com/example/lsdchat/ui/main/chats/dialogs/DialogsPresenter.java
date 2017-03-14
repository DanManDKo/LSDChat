package com.example.lsdchat.ui.main.chats.dialogs;


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
    public Observable<List<RealmDialogModel>> getObservableDialogByType(int type) {
        if (type == ApiConstant.TYPE_DIALOG_PUBLIC) {
            return getObservableDialogsByType(ApiConstant.TYPE_DIALOG_PUBLIC);
        } else {
            List<RealmDialogModel> list = new ArrayList<>();

            getObservableDialogsByType(ApiConstant.TYPE_DIALOG_GROUP).subscribe(list::addAll);
            getObservableDialogsByType(ApiConstant.TYPE_DIALOG_PRIVATE).subscribe(list::addAll);
            return Observable.just(list);

        }
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

   private Observable<List<RealmDialogModel>> getObservableDialogsByType(int type) {
        return mModel.getObservableDialogsByType(type);
    }
}
