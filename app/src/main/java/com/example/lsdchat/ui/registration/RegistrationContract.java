package com.example.lsdchat.ui.registration;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

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
        boolean isOnline(Context context);

        void onDestroy();

        void setPhoneNumber(String phone);

        boolean validateRegForm(String email, String pass, String confPass, String fullName);

        boolean validateEmail(String email);

        boolean validatePassword(String pass);

        boolean validateConfPassword(String pass, String confPass);

        boolean validateFullName(String name);

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void onAvatarClickListener();

        void getPhotoFromGallery();

        void getPhotoFromCamera();

        void onFacebookButtonClickListener();

        void requestSessionAndRegistration(boolean validateValue, RegistrationForm form);

        void onSignupButtonClickListener(String email, String password, String confPassword, String name, String website);
    }

    interface View {
        Context getContext();

        void setInvalideEmailError();

        void setWeakPasswordError();

        void setLengthPasswordError();

        void setEquelsPasswordError();

        void setFullNameError();

        void resetErrorMessages();

        void setClickableSignupButton(boolean value);

        void setClickableFacebookButton(boolean value);

        void showProgressBar();

        void hideProgressBar();

        void setLinkedStatus();

        void getUserpicUri(Uri uri);

        void showResponseDialogError(String title, String message);

        void showNetworkErrorDialog();

        void showDialogImageSourceChooser();

        void navigateToMainScreen();
    }


    interface Model {
        Observable<SessionResponse> getSessionNoAuth();

        Observable<LoginResponse> getLogin(String email, String password, String token);

        Observable<RegistrationResponse> getRegistration(String token, RegistrationForm form);

        Observable<RegistrationCreateFileResponse> createFile(String token, String mime, String fileName);

        Observable<Void> declareFileUploaded(long size, String token, long blobId);

        Observable<Void> uploadFileMap(Map<String, RequestBody> map, MultipartBody.Part part);

        Observable<LoginResponse> updateUserInfo(String token, int userId, long blobId);
    }
}
