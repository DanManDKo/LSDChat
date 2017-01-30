package com.example.lsdchat.ui.forgot_password;

import com.example.lsdchat.R;
import com.example.lsdchat.util.Email;

/**
 * Created by User on 30.01.2017.
 */

public class ForgotPasswordPresenter implements ForgotPasswordContract.Presenter {
    private ForgotPasswordContract.View mView;
    private ForgotPasswordContract.Model mModel;

    public ForgotPasswordPresenter(ForgotPasswordContract.View view) {
        mView = view;
        mModel = new ForgotPasswordModel();
    }

    public void onDestroy() {
        mView = null;
    }

    @Override
    public void onCancelClick() {
        mView.dismiss();
    }

    @Override
    public void onSendClick() {
        String email = mView.getEmail();
        if (isValidEmail(email)) {
            mModel.sendEmail(email);
        } else mView.setEmailError(R.string.invalid_email);

    }

    @Override
    public boolean isValidEmail(String email) {
        return Email.checkEmail(email);
    }

    @Override
    public void onInputClick() {
        mView.hideEmailError();
    }
}
