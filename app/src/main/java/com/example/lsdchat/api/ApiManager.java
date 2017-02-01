package com.example.lsdchat.api;

import com.example.lsdchat.api.login.service.LoginService;
import com.example.lsdchat.api.registration.RegistrationService;
import com.example.lsdchat.constant.ApiConstant;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class ApiManager {


    private Retrofit mRetrofit;
    private LoginService mLoginService;
    private RegistrationService mRegistrationService;

    // TODO: 28.01.2017 [Code Review] it'd be better to call init() method in constructor to be sure
    // class properties are not null. Otherwise you should throw exception in get... methods
    // if init methods were not called


    public ApiManager() {
        initRetrofit();
        initServices();
    }

    public LoginService getmLoginService() {
        return mLoginService;
    }

    public RegistrationService getRegistrationService() {
        return mRegistrationService;
    }

    private void initRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiConstant.SERVER)
                .client(new OkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addConverterFactory(createGsonConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private void initServices() {
        if (mRetrofit != null) {
            mLoginService = mRetrofit.create(LoginService.class);
            mRegistrationService = mRetrofit.create(RegistrationService.class);
        }
    }

    private GsonConverterFactory createGsonConverter() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        return GsonConverterFactory.create(builder.create());
    }
}

