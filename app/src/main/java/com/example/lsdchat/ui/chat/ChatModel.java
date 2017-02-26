package com.example.lsdchat.ui.chat;

import com.example.lsdchat.App;
import com.example.lsdchat.manager.DataManager;

/**
 * Created by User on 12.02.2017.
 */

public class ChatModel implements ChatContract.Model {
    private ChatContract.Presenter mPresenter;
    private DataManager mDataManager;

    public ChatModel(ChatContract.Presenter presenter, DataManager dataManager) {
        mPresenter = presenter;
        mDataManager = App.getDataManager();
    }

    @Override
    public int getUserId() {
        int id = mDataManager.getUser().getId();
        return id;
    }

    @Override
    public void onDestroy() {
        mPresenter = null;
    }
}
