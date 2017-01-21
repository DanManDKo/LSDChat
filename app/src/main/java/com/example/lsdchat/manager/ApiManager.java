package com.example.lsdchat.manager;

import com.example.lsdchat.api.ApiService;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    public static final String SCHEME = "https://";
    public static final String HOSTNAME = "api.quickblox.com/";
    public static final String SERVER = SCHEME + HOSTNAME;
    public static final String APP_ID = "52416";
    public static final String AUTH_KEY = "CYzPSEdHBjAsAKL";
    public static final String AUTH_SECRET = "NuadG9LH5h3UXat";

    private Retrofit mRetrofit;
    private ApiService mApiService;

    public void init() {
        initRetrofit();
        initServices();
    }

    public void initRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
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
        //a cap
    public boolean isUserLogged() {
        return false;
    }

}

