package com.example.lsdchat.manager;

import com.example.lsdchat.api.ApiService;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.model.SessionRequestBody;
import com.example.lsdchat.model.SessionResponse;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApiManager {


    private Retrofit mRetrofit;
    private ApiService mApiService;

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
        mApiService = mRetrofit.create(ApiService.class);
    }

    private GsonConverterFactory createGsonConverter() {
        GsonBuilder builder = new GsonBuilder();
        builder.serializeNulls();
        return GsonConverterFactory.create(builder.create());
    }

    public Observable<retrofit2.Response<SessionResponse>> getSession(SessionRequestBody body) {
        return mApiService.getSession(body).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}

