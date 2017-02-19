package com.example.lsdchat.api;

import android.util.Log;

import com.example.lsdchat.api.dialog.DialogService;
import com.example.lsdchat.api.forgot_password.ForgotPasswordService;
import com.example.lsdchat.api.login.service.LoginService;
import com.example.lsdchat.api.registration.service.RegistrationService;
import com.example.lsdchat.constant.ApiConstant;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private Retrofit mRetrofit;
    private LoginService mLoginService;
    private ForgotPasswordService mForgotPasswordService;

    private RegistrationService mRegistrationService;

    private DialogService mDialogService;

    public ApiManager() {
        initRetrofit();
        initServices();
    }

    public DialogService getDialogService() {
        return mDialogService;
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
                .client(createClient())
                .addConverterFactory(createGsonConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private void initServices() {
        if (mRetrofit != null) {
            mLoginService = mRetrofit.create(LoginService.class);
            mRegistrationService = mRetrofit.create(RegistrationService.class);
            mForgotPasswordService = mRetrofit.create(ForgotPasswordService.class);
            mDialogService = mRetrofit.create(DialogService.class);
        }

    }

    private GsonConverterFactory createGsonConverter() {
        GsonBuilder builder = new GsonBuilder()
                .setLenient()
                .serializeNulls();
        return GsonConverterFactory.create(builder.create());
    }

    private OkHttpClient createClient(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> Log.e("LOGGING",message));
//        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
    }
}

