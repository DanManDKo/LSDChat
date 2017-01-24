package com.example.lsdchat.manager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.lsdchat.api.ApiService;
import com.google.gson.GsonBuilder;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBSettings;
import com.quickblox.core.exception.QBResponseException;



import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    public static final String SCHEME = "https://";
    public static final String HOSTNAME = "api.quickblox.com/";
    public static final String SERVER = SCHEME + HOSTNAME;
    //    public static final String APP_ID = "52416";
//    public static final String AUTH_KEY = "CYzPSEdHBjAsAKL";
//    public static final String AUTH_SECRET = "NuadG9LH5h3UXat";
    public static final String APP_ID = "52350";
    public static final String AUTH_KEY = "eYHZLgP44jpLOpf";
    public static final String AUTH_SECRET = "FcTEnZY7p7ShrUV";
    public static final String ACCOUNT_KEY = "1JLnFTXvJzEosEP1cPDy";


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


    public void initializeQb(Activity activity) {
        QBSettings.getInstance().init(activity, APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);

        QBAuth.createSession(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                //Successfully created session

                Log.e("AA",qbSession.getToken());
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }


}




