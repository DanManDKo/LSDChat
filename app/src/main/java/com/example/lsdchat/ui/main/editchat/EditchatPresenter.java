package com.example.lsdchat.ui.main.editchat;


import android.content.Context;
import android.util.Log;

public class EditchatPresenter implements EditchatContract.Presenter {
    private EditchatContract.View mView;
    private EditchatContract.Model mModel;
    private Context mContext;

    public EditchatPresenter(EditchatContract.View view, EditchatContract.Model model) {
        Log.e("AAA", "constructor");
        mView = view;
        mContext = view.getViewContext();
        mModel = model;
    }

    @Override
    public void onDetach() {
        mView = null;
        mModel = null;
    }

    @Override
    public void onDestroy() {

    }
}
