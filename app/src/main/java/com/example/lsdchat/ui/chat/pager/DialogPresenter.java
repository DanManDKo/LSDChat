package com.example.lsdchat.ui.chat.pager;


import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.manager.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 12.02.2017.
 */

public class DialogPresenter implements DialogContract.Presenter {
    private List<ItemDialog> mItemDialogs;
    private DialogContract.View mView;
    private DialogContract.Model mModel;
    private SharedPreferencesManager mSharedPreferencesManager;

    public DialogPresenter(DialogContract.View view, ArrayList<Integer> userIds, SharedPreferencesManager sharedPreferencesManager) {
        mView = view;
        mModel = new DialogModel(this, userIds);
        mSharedPreferencesManager = sharedPreferencesManager;
    }

    @Override
    public List<ItemDialog> getAllDialogs() {
        mModel.getAllDialogs(getToken())
                .subscribe(dialogsResponse -> {
                    mItemDialogs = dialogsResponse.getItemDialogList();
                }, throwable -> {

                });


        return mItemDialogs;
    }

    @Override
    public String getToken() {
        return mSharedPreferencesManager.getToken();
    }

    public void onDestroy() {
        mView = null;
        mModel.onDestroy();
    }
}
