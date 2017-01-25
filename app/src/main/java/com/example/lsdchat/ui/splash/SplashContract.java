package com.example.lsdchat.ui.splash;

import com.example.lsdchat.base.BaseMvpPresenter;
import com.example.lsdchat.base.BaseMvpView;
import com.example.lsdchat.model.SessionRequestBody;
import com.example.lsdchat.model.SessionResponse;

/**
 * Created by User on 18.01.2017.
 */

public interface SplashContract {
    interface Presenter extends BaseMvpPresenter<View> {
        void leaveSplashScreen();

        boolean isOnLine();

        boolean isLogged();

        void navigateToLogin();

        void navigateToMain();
        void getSession(SessionRequestBody body);
        String getSignature(int random, long timestamp, String user, String password);
    }

    interface View extends BaseMvpView {
    }
}
