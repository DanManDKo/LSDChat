package com.example.lsdchat.ui.splash;

/**
 * Created by User on 18.01.2017.
 */

public interface SplashContract {
    interface Presenter {
        void leaveSplashScreen();

        boolean isOnLine();

        boolean isLogged();


        void getSession();
    }

    interface View {
        void navigateToLogin();

        void navigateToMain();
    }
}
