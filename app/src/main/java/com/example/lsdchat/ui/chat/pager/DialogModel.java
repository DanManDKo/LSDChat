package com.example.lsdchat.ui.chat.pager;

import com.example.lsdchat.App;
import com.example.lsdchat.api.ApiManager;
import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.manager.model.RealmItemDialog;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by User on 12.02.2017.
 */

public class DialogModel implements DialogContract.Model {
    private DialogContract.Presenter mPresenter;
    private ArrayList<Integer> mUserIds;
    private DataManager mDataManager;
    private ApiManager mApiManager;

    public DialogModel(DialogContract.Presenter presenter, ArrayList<Integer> userIds, DataManager dataManager, ApiManager apiManager) {
        mPresenter = presenter;
        mUserIds = userIds;
        mDataManager = dataManager;
        mApiManager = apiManager;
    }


    @Override
    public Observable<DialogsResponse> getAllDialogs(String token) {
        return mApiManager
                .getDialogService()
                .getDialog(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    @Override
    public List<RealmItemDialog> getDialogByType(int type) {
        return mDataManager.getDialogs(type);
    }

    @Override
    public boolean saveDialogsToDb(List<RealmItemDialog> itemDialogs) {
        return mDataManager.saveDialogs(itemDialogs);
    }

    public void onDestroy() {
        mPresenter = null;
    }
}
