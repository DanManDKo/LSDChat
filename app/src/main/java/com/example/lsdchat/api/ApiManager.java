package com.example.lsdchat.api;

import com.example.lsdchat.api.request.LoginRequest;
import com.example.lsdchat.api.request.SessionRequestNoAuth;
import com.example.lsdchat.api.response.LoginResponse;
import com.example.lsdchat.api.request.SessionRequestAuth;
import com.example.lsdchat.api.response.SessionResponse;
import com.example.lsdchat.api.service.LoginService;
import com.example.lsdchat.api.service.SessionService;
import com.example.lsdchat.constant.ApiConstant;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApiManager {


    private Retrofit mRetrofit;
    private LoginService mLoginService;
    private SessionService mSessionService;

    // TODO: 28.01.2017 [Code Review] it'd be better to call init() method in constructor to be sure
    // class properties are not null. Otherwise you should throw exception in get... methods
    // if init methods were not called
    public void init() {
        initRetrofit();
        initServices();
    }

    // TODO: 28.01.2017 [Code Review] seems like it should be private.
    public void initRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiConstant.SERVER)
                .addConverterFactory(createGsonConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    // TODO: 28.01.2017 [Code Review] seems like it should be private, also mRetrofit may be null.
    // Pass it as parameter
    public void initServices() {
        mLoginService = mRetrofit.create(LoginService.class);
        mSessionService = mRetrofit.create(SessionService.class);
    }

    // TODO: 28.01.2017 [Code Review]
    private GsonConverterFactory createGsonConverter() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        return GsonConverterFactory.create(builder.create());
    }

    public Observable<SessionResponse> getSessionNoAuth(SessionRequestNoAuth body) {
        return mSessionService.getSession(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<SessionResponse> getSessionAuth(SessionRequestAuth body) {
        return mLoginService.getSession(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LoginResponse> getLogin(LoginRequest body, String token) {
        return mLoginService.getLogin(token,body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

