package com.example.lsdchat.ui.registration;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.widget.Button;
import android.widget.ImageView;

import com.example.lsdchat.api.login.response.LoginResponse;
import com.example.lsdchat.api.login.response.SessionResponse;
import com.example.lsdchat.api.registration.response.RegistrationCreateFileResponse;
import com.example.lsdchat.api.registration.response.RegistrationResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

public interface RegistrationContract {

    interface Presenter {
        boolean isOnline();

        void onDestroy();

        void getFacebookToken();

        void setTextChangedInputMaskListener(TextInputEditText phone);

        boolean validateRegForm(String email, String pass, String confPass);

        boolean validateEmail(String email);

        boolean validatePassword(String pass);

        boolean validateConfPassword(String pass, String confPass);

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onAvatarClickListener(ImageView imageView);

        void onFacebookButtonClickListener(Button button);

        void onSignupButtonClickListener(Button button,
                                         TextInputEditText email,
                                         TextInputEditText pass,
                                         TextInputEditText confpass,
                                         TextInputEditText name,
                                         TextInputEditText web);
    }

    interface View {
        void setInvalideEmailError();

        void setWeakPasswordError();

        void setLengthPasswordError();

        void setEquelsPasswordError();

        void resetErrorMessages();

        void showProgressBar();

        void hideProgressBar();

        void getUserpicUri(Uri uri);

        Context getContext();

        void showResponseDialogError(String title, String message);

        void navigatetoMainScreen();

        //just alert for test
        void showAlertD();

    }


    interface Model {
        Observable<SessionResponse> getSessionNoAuth();

        Observable<LoginResponse> getLogin(String email, String password, String token);

        Observable<RegistrationResponse> getRegistration(String token, RegistrationForm form);

        Observable<RegistrationCreateFileResponse> createFile(String token, String mime, String fileName);


        Observable<Void> declareFileUploaded(long size, String token, long blobId);



        Observable<Void> uploadFileMap(Map<String, RequestBody> map,MultipartBody.Part part);
    }


}
