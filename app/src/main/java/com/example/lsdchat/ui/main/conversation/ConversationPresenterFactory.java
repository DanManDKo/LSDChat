package com.example.lsdchat.ui.main.conversation;


import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.ui.PresenterFactory;

public class ConversationPresenterFactory implements PresenterFactory<ConversationPresenter> {
    private ConversationContract.View mView;
    private ConversationContract.Model mModel;
    private SharedPreferencesManager mPreferencesManager;

    public ConversationPresenterFactory(ConversationContract.View view, ConversationContract.Model model, SharedPreferencesManager preferencesManager) {
        mView = view;
        mModel = model;
        mPreferencesManager = preferencesManager;
    }

    public ConversationPresenter create() {
        return new ConversationPresenter(mView, mModel, mPreferencesManager);
    }
}
