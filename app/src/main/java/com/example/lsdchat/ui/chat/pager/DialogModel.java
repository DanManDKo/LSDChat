package com.example.lsdchat.ui.chat.pager;

import android.app.Activity;

import com.example.lsdchat.App;
import com.example.lsdchat.api.ApiManager;
import com.example.lsdchat.api.dialog.response.DialogsResponse;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by User on 12.02.2017.
 */

public class DialogModel implements DialogContract.Model {
    private DialogContract.Presenter mPresenter;
    private ArrayList<Integer> mUserIds;

    private ApiManager mApiManager = App.getApiManager();

    public DialogModel(DialogContract.Presenter presenter, ArrayList<Integer> userIds) {
        mPresenter = presenter;
        mUserIds = userIds;
    }


    @Override
    public Observable<DialogsResponse> getAllDialogs(String token) {
        return App.getApiManager()
                .getDialogService()
                .getDialog(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public void onDestroy() {
        mPresenter = null;
    }
}
