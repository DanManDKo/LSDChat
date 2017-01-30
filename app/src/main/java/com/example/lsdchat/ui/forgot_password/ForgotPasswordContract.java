package com.example.lsdchat.ui.forgot_password;

/**
 * Created by User on 30.01.2017.
 */

public class ForgotPasswordContract {
    interface Model {
        void sendEmail(String email);
    }

    interface Presenter {
        void onDestroy();
        void onCancelClick();
        void onSendClick();
        boolean isValidEmail(String email);
        void onInputClick();
    }

    interface View {
        void setEmailError(int stringId);

        void hideEmailError();
        String getEmail();
        void dismiss();

    }
}
