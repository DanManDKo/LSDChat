package com.example.lsdchat.ui.registration;

import com.example.lsdchat.App;
import com.example.lsdchat.api.login.request.LoginRequest;
import com.example.lsdchat.api.login.request.SessionRequestAuth;
import com.example.lsdchat.api.login.request.SessionRequestNoAuth;
import com.example.lsdchat.api.login.response.LoginResponse;
import com.example.lsdchat.api.login.response.SessionResponse;
import com.example.lsdchat.api.registration.request.RegistrationCreateFileRequest;
import com.example.lsdchat.api.registration.request.RegistrationCreateFileRequestBlob;
import com.example.lsdchat.api.registration.response.RegistrationCreateFileResponse;
import com.example.lsdchat.api.registration.request.RegistrationRequest;
import com.example.lsdchat.api.registration.request.RegistrationRequestUser;
import com.example.lsdchat.api.registration.response.RegistrationResponse;
import com.example.lsdchat.api.registration.RegistrationService;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.util.Signature;

import java.util.Random;

import okhttp3.MultipartBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegistrationModel implements RegistrationContract.Model {
    private RegistrationService mRegistrationService;

    public RegistrationModel() {
        mRegistrationService = App.getApiManager().getRegistrationService();
    }

    @Override
    public Observable<SessionResponse> getSessionNoAuth() {
        int nonce = new Random().nextInt();
        long timestamp = System.currentTimeMillis() / 1000;
        String signature = Signature.calculateSignatureNoAuth(nonce, timestamp);

        SessionRequestNoAuth session = new SessionRequestNoAuth(ApiConstant.APP_ID, ApiConstant.AUTH_KEY, nonce, timestamp, signature);
        return mRegistrationService.getSession(session)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<RegistrationResponse> getRegistration(String token, RegistrationForm form) {
        RegistrationRequestUser user = new RegistrationRequestUser(form.getEmail(),form.getPassword(),
                form.getFullName(),form.getPhone(),form.getWebsite(),form.getFacebookId(),form.getBlobId());

        RegistrationRequest body = new RegistrationRequest(user);
        return mRegistrationService.getRegistrationRequest(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<RegistrationCreateFileResponse> createFile(String token, String mime, String fileName) {
        RegistrationCreateFileRequestBlob blob = new RegistrationCreateFileRequestBlob(mime, fileName);
        RegistrationCreateFileRequest body = new RegistrationCreateFileRequest(blob);

        return mRegistrationService.createFileRequest(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<LoginResponse> getLogin(String email, String password, String token) {
        LoginRequest body = new LoginRequest(email, password);

        return mRegistrationService.getLogin(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Void> uploadFile(String type, String expires, String acl, String key, String policy, String actionStatus, String algorithm, String credential, String date, String signature, MultipartBody.Part file) {

        return mRegistrationService.getUploadFileRequest(type, expires, acl, key, policy, actionStatus, algorithm, credential, date, signature, file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
