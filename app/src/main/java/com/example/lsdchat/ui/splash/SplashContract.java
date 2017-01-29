package com.example.lsdchat.ui.splash;

import android.content.Context;

import com.example.lsdchat.api.response.SessionResponse;
import com.example.lsdchat.base.BaseMvpPresenter;
import com.example.lsdchat.base.BaseMvpView;
import com.example.lsdchat.api.request.SessionRequestNoAuth;

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
    }

    interface View {
        void navigateToLogin();

        Context getContext();

        void navigateToMain();
    }
}
