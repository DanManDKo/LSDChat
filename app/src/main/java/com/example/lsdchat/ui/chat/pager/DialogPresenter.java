package com.example.lsdchat.ui.chat.pager;


import android.util.Log;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.manager.model.RealmItemDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 12.02.2017.
 */

public class DialogPresenter implements DialogContract.Presenter {
    private List<RealmItemDialog> mItemDialogs;
    private DialogContract.View mView;
    private DialogContract.Model mModel;
    private SharedPreferencesManager mSharedPreferencesManager;

    public DialogPresenter(DialogContract.View view, ArrayList<Integer> userIds, SharedPreferencesManager sharedPreferencesManager) {
        mView = view;
        mModel = new DialogModel(this, userIds, App.getDataManager(), App.getApiManager());
        mSharedPreferencesManager = sharedPreferencesManager;
    }


    @Override
    public void getAllDialogsAndSave() {
        mModel.getAllDialogs(getToken())
                .subscribe(dialogsResponse -> {
                    List<ItemDialog> itemDialogs = dialogsResponse.getItemDialogList();
                    mItemDialogs = copyToRealmItemDialog(itemDialogs);
                    if (mModel.saveDialogsToDb(mItemDialogs)) ;
                    mView.onDialoguesLoaded(mModel.getDialogByType(mView.getType()));
                }, throwable -> {
                    Log.d("tag", throwable.getMessage());
                });


    }

    private List<RealmItemDialog> copyToRealmItemDialog(List<ItemDialog> dialogs) {
        List<RealmItemDialog> realmDialogs = new ArrayList<>();
        for (ItemDialog d : dialogs) {
            realmDialogs.add(new RealmItemDialog(d));
        }
        return realmDialogs;
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
