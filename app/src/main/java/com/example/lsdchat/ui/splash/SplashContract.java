package com.example.lsdchat.ui.splash;

import com.example.lsdchat.base.BaseMvpPresenter;
import com.example.lsdchat.base.BaseMvpView;

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
    }

    interface View extends BaseMvpView {
    }
}
