package com.example.lsdchat.ui.splash;

import android.content.Context;

import com.example.lsdchat.api.login.response.LoginResponse;
import com.example.lsdchat.api.login.response.SessionResponse;

import rx.Observable;

/**
 * Created by User on 18.01.2017.
 */


public interface SplashContract {
    interface Presenter {
        void leaveSplashScreen();

        void requestSessionAndLogin(String email, String password);

        boolean isOnLine();

        void onDestroy();

        boolean isLogged();


    }

    interface Model {
        Observable<SessionResponse> getSessionAuth(String email, String password);
        Observable<LoginResponse> getLogin(String email, String password, String token);

    }

    interface View {
        void navigateToLogin();

        Context getContext();

        void navigateToMain();

        void getDialogAndUser(String token);
    }
}
