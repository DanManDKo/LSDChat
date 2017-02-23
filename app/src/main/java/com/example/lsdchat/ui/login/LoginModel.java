package com.example.lsdchat.ui.login;

import com.example.lsdchat.App;
import com.example.lsdchat.api.login.request.LoginRequest;
import com.example.lsdchat.api.login.request.SessionRequestAuth;
import com.example.lsdchat.api.login.response.LoginResponse;
import com.example.lsdchat.api.login.response.SessionResponse;
import com.example.lsdchat.api.login.service.LoginService;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.User;
import com.example.lsdchat.util.Signature;

import java.util.Random;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginModel implements LoginContract.Model {
    private LoginService mLoginService;
    private DataManager mDataManager;

    public LoginModel() {
        mLoginService = App.getApiManager().getmLoginService();
        mDataManager = App.getDataManager();
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

    @Override
    public void saveUser(User user) {
        mDataManager.insertUser(user);
    }
}
