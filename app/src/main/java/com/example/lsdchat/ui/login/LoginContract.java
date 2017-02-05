package com.example.lsdchat.ui.login;


import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
        void onResume();
        void onPause();

        void validateCredentials(EditText email, EditText password);

        void requestSessionAndLogin(String email, String password);

        boolean isValidPassword(CharSequence password);

        void btnSignInClick(Button btnSignIn, EditText email, EditText password);

        void btnSignUpClick(Button btnSignUp);

        void btnSignForgotPasswordClick(TextView btnForgotPassword);

        void startService(Context context);

        void stopService(Context context);

        boolean isOnline();
    }

    interface View {
        void dialogError(Throwable throwable);

        Context getContext();

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
