package com.example.lsdchat.ui.main.usersinfo;


import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.ui.main.conversation.ConversationFragment;

import rx.android.schedulers.AndroidSchedulers;

public class UserInfoPresenter implements UserInfoContract.Presenter {

    private UserInfoContract.View mView;
    private UserInfoContract.Model mModel;

    public UserInfoPresenter(UserInfoContract.View mView, UserInfoContract.Model mModel) {
        this.mView = mView;
        this.mModel = mModel;

    }

    @Override
    public void onDestroy() {
        mView = null;
        mModel = null;
    }

    @Override
    public void getUserAvatar(int userId) {
        mModel.getImagePath(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> mView.setImagePath(s));

    }

    @Override
    public void createDialog(int idUser) {
        CreateDialogRequest createDialogRequest = new CreateDialogRequest();
        createDialogRequest.setType(ApiConstant.TYPE_DIALOG_PRIVATE);
        createDialogRequest.setIdU(String.valueOf(idUser));
        if (mView.isNetworkConnect()) {
            mModel.createDialog(createDialogRequest)
                    .subscribe(itemDialog -> {
                        mModel.saveDialog(new RealmDialogModel(itemDialog));

                        mView.navigateToChat(ConversationFragment
                                .newInstance(itemDialog.getId(), itemDialog.getName(), itemDialog.getType(), idUser));

                    }, throwable -> mView.showDialogError(throwable));
        }
    }

}
