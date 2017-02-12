package com.example.lsdchat.ui.forgot_password;

import android.content.Context;

import com.example.lsdchat.api.login.response.SessionResponse;

import rx.Observable;

/**
 * Created by User on 30.01.2017.
 */

public class ForgotPasswordContract {
    interface Model {
        Observable sendEmail(String email, String token);
        Observable<SessionResponse> getSessionNoAuth();
    }

    interface Presenter {
        void onDestroy();

        void onCancelClick();

        void onSendClick();

        boolean isValidEmail(String email);

        void onInputClick();

        void requestSessionAndSendEmail(String email);
    }

    interface View {
        void setEmailError(String error);

        void hideEmailError();

        void showEmailSuccessToast();


        String getEmail();

        Context getContext();



        void dismiss();

    }
}
