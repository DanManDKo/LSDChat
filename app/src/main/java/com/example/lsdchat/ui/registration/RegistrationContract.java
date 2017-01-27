package com.example.lsdchat.ui.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;

import com.example.lsdchat.base.BaseMvpPresenter;
import com.example.lsdchat.base.BaseMvpView;

public class RegistrationContract {
    public interface Presenter extends BaseMvpPresenter<View> {
        //screen navigation
        void navigateToMainScreen(boolean validatevalue);

        void showDialogImageSourceChooser();

        void initFacebookSdk();

        void loginWithFacebook();

        void getFacebookeToken();

        void setTextChangedListenerWithInputMask(TextInputEditText phone);

        boolean validateRegForm(String email, String pass, String confPass);

        boolean validateEmail(String email);

        boolean validatePassword(String pass);

        boolean validateConfPassword(String pass, String confPass);

        Uri getUserImageFromCameraOrGallery(int requestCode, int resultCode, Intent data);

    }

    public interface View extends BaseMvpView {
        //void onError();
        void setInvalideEmailError();

        void setWeakPasswordError();

        void setLengthPasswordError();

        void setEquelsPasswordError();

        void resetErrorMessages();

        void showProgressBar();

        void hideProgressBar();
    }
}
