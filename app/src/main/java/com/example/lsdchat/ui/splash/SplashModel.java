package com.example.lsdchat.ui.splash;

import com.example.lsdchat.App;
import com.example.lsdchat.api.login.request.LoginRequest;
import com.example.lsdchat.api.login.request.SessionRequestAuth;
import com.example.lsdchat.api.login.response.LoginResponse;
import com.example.lsdchat.api.login.response.SessionResponse;
import com.example.lsdchat.api.login.service.LoginService;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.util.Signature;

import java.util.Random;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by User on 29.01.2017.
 */

public class SplashModel implements SplashContract.Model {
    private LoginService mLoginService;

    public SplashModel() {
        mLoginService = App.getApiManager().getmLoginService();
    }

    @Override
    public Observable<SessionResponse> getSessionAuth(String email, String password) {
        int nonce = new Random().nextInt();
        long timestamp = System.currentTimeMillis() / 1000;
        String signature = Signature.calculateSignatureAuth(email, password, nonce, timestamp);
        SessionRequestAuth auth = new SessionRequestAuth(ApiConstant.APP_ID, ApiConstant.AUTH_KEY, nonce, timestamp, signature, email, password);
        return mLoginService.getSession(auth)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    @Override
    public Observable<LoginResponse> getLogin(String email, String password, String token) {
        LoginRequest loginRequest = new LoginRequest(email, password);

        return mLoginService.getLogin(token, loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
