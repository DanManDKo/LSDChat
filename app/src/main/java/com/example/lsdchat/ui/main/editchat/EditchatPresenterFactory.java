package com.example.lsdchat.ui.main.editchat;


import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.ui.PresenterFactory;

public class EditchatPresenterFactory implements PresenterFactory<EditchatPresenter> {
    private EditchatContract.Model mModel;
    private EditchatContract.View mView;
    private SharedPreferencesManager mPreferencesManager;

    public EditchatPresenterFactory(EditchatContract.Model model, EditchatContract.View view, SharedPreferencesManager manager) {
        mModel = model;
        mView = view;
        mPreferencesManager = manager;
    }

    public EditchatPresenter create() {
        return new EditchatPresenter(mView, mModel, mPreferencesManager);
    }
}
