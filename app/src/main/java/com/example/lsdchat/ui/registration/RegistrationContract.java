package com.example.lsdchat.ui.registration;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;

public class RegistrationContract {
    public interface Presenter {
        //screen navigation
        void navigateToMainScreen(boolean validatevalue);

        void showDialogImageSourceChooser();

        void initFacebookSdk();

        void loginWithFacebook();

        void getFacebookToken();

        void setTextChangedListenerWithInputMask(TextInputEditText phone);

        boolean validateRegForm(String email, String pass, String confPass);

        boolean validateEmail(String email);

        boolean validatePassword(String pass);

        boolean validateConfPassword(String pass, String confPass);

        void requestSessionAndRegistration();

        void onActivityResult(int requestCode, int resultCode, Intent data);

    }

    public interface View {
        //void onError();
        void setInvalideEmailError();

        void setWeakPasswordError();

        void setLengthPasswordError();

        void setEquelsPasswordError();

        void resetErrorMessages();

        void showProgressBar();

        void hideProgressBar();

        void getUserpicUri(Uri uri);
    }
}
