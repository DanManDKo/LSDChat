package com.example.lsdchat.ui.login;


import android.widget.EditText;

import com.example.lsdchat.api.login.response.LoginResponse;
import com.example.lsdchat.api.login.response.SessionResponse;

import rx.Observable;


public interface LoginContract {
    interface Model {
        Observable<SessionResponse> getSessionAuth(String email, String password);

        Observable<LoginResponse> getLogin(String email, String password, String token);
    }

    interface Presenter {

        void onDestroy();

        void validateCredentials(EditText email, EditText password);

        void requestSessionAndLogin(String email, String password);

        boolean isValidPassword(CharSequence password);

        boolean isValidEmail(CharSequence email);

        void btnSignInClick(String email, String password);

        void btnSignUpClick();

        void btnSignForgotPasswordClick();
    }

    interface View {
        void showProgressBar();

        void hideProgressBar();

        void setEmailError();

        void setPasswordError();

        void hideEmailError();

        void hidePasswordError();

        void setLoginButtonEnabled(boolean enabled);

        void navigateToRegistration();

        void navigateToMainScreen();

        boolean isKeepSignIn();
        void showDialogForgotPassword();

    }

}
