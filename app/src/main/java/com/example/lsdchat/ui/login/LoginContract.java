package com.example.lsdchat.ui.login;


import android.widget.EditText;

import com.example.lsdchat.api.request.SessionRequestAuth;
import com.example.lsdchat.api.response.LoginResponse;
import com.example.lsdchat.api.response.SessionResponse;
import com.example.lsdchat.base.BaseMvpPresenter;
import com.example.lsdchat.base.BaseMvpView;

import rx.Observable;


public interface LoginContract {
    interface Model {
        Observable<SessionResponse> getSessionAuth(String email, String password);
        Observable<LoginResponse> getLogin(String email, String password,String token);
    }

    interface Presented extends BaseMvpPresenter<View> {

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
        void navigateToForgotPassword();
        void navigateToMainScreen();

        boolean isKeepSignIn();



    }

}
