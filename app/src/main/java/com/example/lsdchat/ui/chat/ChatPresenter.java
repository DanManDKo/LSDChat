package com.example.lsdchat.ui.chat;

/**
 * Created by User on 12.02.2017.
 */

public class ChatPresenter implements ChatContract.Presenter {

    private ChatContract.View mView;
    private ChatContract.Model mModel;

    public ChatPresenter(ChatContract.View view) {
        mView = view;
        mModel = new ChatModel(this);
    }

    @Override
    public int getUserId() {
        return mModel.getUserId();
    }

    @Override
    public void onDestroy() {
        mView = null;
        mModel.onDestroy();
        mModel = null;
    }
}
