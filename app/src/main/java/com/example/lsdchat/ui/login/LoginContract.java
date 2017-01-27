package com.example.lsdchat.ui.login;


import com.example.lsdchat.base.BaseMvpPresenter;
import com.example.lsdchat.base.BaseMvpView;


public interface LoginContract {
    interface Presented extends BaseMvpPresenter<View> {

        void onDestroy();

        void validateCredentials(String email, String password);
        void requestSessionAndLogin(String email, String password);

    }

    interface View extends BaseMvpView {
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
        boolean isValidPassword(String password);
        boolean isValidEmail(String email);


    }

}
