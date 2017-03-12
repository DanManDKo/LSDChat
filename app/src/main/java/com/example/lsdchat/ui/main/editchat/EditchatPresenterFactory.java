package com.example.lsdchat.ui.main.editchat;


public class EditchatPresenterFactory implements PresenterFactory<EditchatPresenter> {
    private EditchatContract.Model mModel;
    private EditchatContract.View mView;

    public EditchatPresenterFactory(EditchatContract.Model model, EditchatContract.View view) {
        mModel = model;
        mView = view;
    }

    public EditchatPresenter create() {
        return new EditchatPresenter(mView, mModel);
    }
}
