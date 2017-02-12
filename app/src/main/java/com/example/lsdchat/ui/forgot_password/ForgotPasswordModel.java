package com.example.lsdchat.ui.forgot_password;

import com.example.lsdchat.App;
import com.example.lsdchat.api.forgot_password.ForgotPasswordService;
import com.example.lsdchat.api.login.request.SessionRequestNoAuth;
import com.example.lsdchat.api.login.response.SessionResponse;
import com.example.lsdchat.api.registration.service.RegistrationService;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.util.Signature;

import java.util.Random;

import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by User on 30.01.2017.
 */

public class ForgotPasswordModel implements ForgotPasswordContract.Model {
    private RegistrationService mRegistrationService;
    private ForgotPasswordService mForgotPasswordService;

    public ForgotPasswordModel() {
        mRegistrationService = App.getApiManager().getRegistrationService();
        mForgotPasswordService = App.getApiManager().getForgotPasswordService();
    }

    @Override
    public Observable<Response<Void>> sendEmail(String email, String token) {
        return mForgotPasswordService.sendEmail(email, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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
}
