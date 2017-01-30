package com.example.lsdchat.ui.forgot_password;

/**
 * Created by User on 30.01.2017.
 */

public class ForgotPasswordPresenter implements ForgotPasswordContract.Presenter {
    private ForgotPasswordContract.View mView;

    public ForgotPasswordPresenter(ForgotPasswordContract.View view) {
        mView = view;
    }

    public void onDestroy() {
        mView = null;
    }
}
