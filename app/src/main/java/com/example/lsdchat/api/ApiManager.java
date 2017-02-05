package com.example.lsdchat.api;

import com.example.lsdchat.api.forgot_password.ForgotPasswordService;
import com.example.lsdchat.api.login.service.LoginService;
import com.example.lsdchat.api.registration.RegistrationService;
import com.example.lsdchat.constant.ApiConstant;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private Retrofit mRetrofit;
    private LoginService mLoginService;
    private SessionService mSessionService;
    private ForgotPasswordService mForgotPasswordService;

    private RegistrationService mRegistrationService;


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

    public ForgotPasswordService getForgotPasswordService() {
        return mForgotPasswordService;
    }

    private void initRetrofit() {

             mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiConstant.SERVER)
                .addConverterFactory(createGsonConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private void initServices() {
        if (mRetrofit != null) {
            mLoginService = mRetrofit.create(LoginService.class);
            mRegistrationService = mRetrofit.create(RegistrationService.class);
            mForgotPasswordService = mRetrofit.create(ForgotPasswordService.class);
        }

    }

    private GsonConverterFactory createGsonConverter() {
        GsonBuilder builder = new GsonBuilder()
                .setLenient()
                .serializeNulls();
        return GsonConverterFactory.create(builder.create());
    }
}

