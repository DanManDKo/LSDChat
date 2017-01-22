package com.example.lsdchat.presenter;

import com.example.lsdchat.contract.RegistrationContract;
import com.facebook.CallbackManager;


public class RegistrationPresenter implements RegistrationContract.Presenter {
    private RegistrationContract.View mView;

    @Override
    public void attachView(RegistrationContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
    }


    @Override
    public String getFbToken() {
       return null;
    }
}
