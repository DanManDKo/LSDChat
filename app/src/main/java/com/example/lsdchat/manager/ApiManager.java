package com.example.lsdchat.manager;

import com.example.lsdchat.api.registration.RegistrationRequest;
import com.example.lsdchat.api.registration.RegistrationResponse;
import com.example.lsdchat.api.registration.RegistrationService;
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
    private RegistrationService mRegistrationService;

    public void init() {
        initRetrofit();
        initServices();
    }

    public void initRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiConstant.SERVER)
                .addConverterFactory(createGsonConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public void initServices() {
        mRegistrationService = mRetrofit.create(RegistrationService.class);
    }

    private GsonConverterFactory createGsonConverter() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        return GsonConverterFactory.create(builder.create());
    }

    public Observable<RegistrationResponse> getRegistration(String token, RegistrationRequest body) {
        return mRegistrationService.getRegistrationRequest(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}

