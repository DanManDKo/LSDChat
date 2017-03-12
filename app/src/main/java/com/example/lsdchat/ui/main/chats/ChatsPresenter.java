package com.example.lsdchat.ui.main.chats;


import com.example.lsdchat.model.DialogModel;
import com.example.lsdchat.model.User;
import com.example.lsdchat.util.Utils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class ChatsPresenter implements ChatsContract.Presenter {
    private ChatsContract.View mView;
    private ChatsContract.Model mModel;

    private User mUser;


    public ChatsPresenter(ChatsContract.View mView, ChatsContract.Model model) {
        this.mView = mView;
        this.mModel = model;
        mUser = mModel.getCurrentUser();
        getAllDialogAndSave();
    }


    @Override
    public Observable<String> getUserAvatar() {
        return Utils.getUrlImage(mUser.getBlobId(), mModel.getToken());
    }

    @Override
    public User getUserModel() {
        return mUser;
    }


    @Override
    public void onLogout() {
        mModel.destroySession(mModel.getToken())
                .subscribe(aVoid -> {
                    mModel.deleteUser();
                    mView.navigateToLoginActivity();
                }, throwable -> mView.showMessageError(throwable));
    }

    private void getAllDialogAndSave() {
        List<DialogModel> list = new ArrayList<>();
        mModel.getAllDialogs(mModel.getToken())
                .flatMap(dialogsResponse -> Observable.just(dialogsResponse.getItemDialogList()))
                .subscribe(dialogList -> {
                    Observable.from(dialogList)
                            .subscribe(dialog -> list.add(new DialogModel(dialog)));
                    mModel.saveDialog(list);
                }, throwable -> mView.showMessageError(throwable));

    }
}