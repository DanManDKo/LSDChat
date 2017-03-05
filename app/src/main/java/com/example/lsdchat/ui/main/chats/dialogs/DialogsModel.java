package com.example.lsdchat.ui.main.chats.dialogs;


import com.example.lsdchat.App;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.DialogModel;

import java.util.List;

public class DialogsModel implements DialogsContract.Model {
    private DataManager mDataManager;

    public DialogsModel() {
        mDataManager = App.getDataManager();
    }

    @Override
    public List<DialogModel> getDialogsByType(int type) {
        return mDataManager.getDialogsByType(type);
    }
}
