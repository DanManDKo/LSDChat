package com.example.lsdchat.ui.forgot_password;

import com.example.lsdchat.R;
import com.example.lsdchat.api.login.response.SessionResponse;
import com.example.lsdchat.util.Email;

import retrofit2.Response;

/**
 * Created by User on 30.01.2017.
 */

public class ForgotPasswordPresenter implements ForgotPasswordContract.Presenter {
    private static final int SUCCESS_CODE = 200;
    private ForgotPasswordContract.View mView;
    private ForgotPasswordContract.Model mModel;
    private String mToken;
    private SessionResponse mSessionResponse;

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
            requestSessionAndSendEmail(email);
        } else mView.setEmailError(mView.getContext().getString(R.string.invalid_email));

    }

    @Override
    public boolean isValidEmail(String email) {
        return Email.checkEmail(email);
    }

    @Override
    public void onInputClick() {
        mView.hideEmailError();
    }

    @Override
    public void requestSessionAndSendEmail(String email) {
        mModel.getSessionNoAuth().subscribe(sessionResponse -> {
            mModel.sendEmail(email, sessionResponse.getSession().getToken())
                    .subscribe(o -> {
                        Response<Void> response = (Response<Void>) o;
                        if (response.code() == SUCCESS_CODE) {
                            mView.showEmailSuccessToast();
                            mView.dismiss();
                        } else {
                            mView.setEmailError(response.message());
                        }

                    }, throwable -> {

                    });

        }, throwable -> {
            mView.showEmailError(throwable.getMessage());
        });

    }
}
