package com.example.lsdchat.api;

import com.example.lsdchat.api.login.service.LoginService;
import com.example.lsdchat.api.registration.RegistrationAmazonService;
import com.example.lsdchat.api.registration.RegistrationService;
import com.example.lsdchat.constant.ApiConstant;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private Retrofit mRetrofit;
    private Retrofit mRetrofitAmazon;
    private LoginService mLoginService;
    private RegistrationService mRegistrationService;

    private RegistrationAmazonService mRegistrationAmazonService;

    public ApiManager() {
        initRetrofit();
        initServices();
    }

    public RegistrationAmazonService getRegistrationAmazonService() {
        return mRegistrationAmazonService;
    }

    public LoginService getmLoginService() {
        return mLoginService;
    }

    public RegistrationService getRegistrationService() {
        return mRegistrationService;
    }

    private void initRetrofit() {

//        builder.interceptors().add(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request original = chain.request();
//                Request.Builder requestBuilder = original.newBuilder().method(original.method(), original.body());
//
//                requestBuilder.header(ApiConstant.UploadParametres.CONTENT_TYPE, token);
//                requestBuilder.header(ApiConstant.UploadParametres.EXPIRES, token);
//                requestBuilder.header(ApiConstant.UploadParametres.ACL, token);
//                requestBuilder.header(ApiConstant.UploadParametres.KEY, token);
//                requestBuilder.header(ApiConstant.UploadParametres.POLICY, token);
//                requestBuilder.header(ApiConstant.UploadParametres.SUCCESS_ACTION_STATUS, token);
//                requestBuilder.header(ApiConstant.UploadParametres.ALGORITHM, token);
//                requestBuilder.header(ApiConstant.UploadParametres.CREDENTIAL, token);
//                requestBuilder.header(ApiConstant.UploadParametres.DATE, token);
//                requestBuilder.header(ApiConstant.UploadParametres.SIGNATURE, token);
//
//                return chain.proceed(requestBuilder.build());
//            }
//        });
//
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        builder.interceptors().add(interceptor);
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS);

        mRetrofitAmazon = new Retrofit.Builder()
                .baseUrl(ApiConstant.SERVER_AMAZON)
                .client(builder.build())
                .addConverterFactory(createGsonConverter())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

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
        }
        if (mRetrofitAmazon != null) {
            mRegistrationAmazonService = mRetrofitAmazon.create(RegistrationAmazonService.class);
        }
    }

    private GsonConverterFactory createGsonConverter() {
        GsonBuilder builder = new GsonBuilder()
                .setLenient()
                .serializeNulls();
        return GsonConverterFactory.create(builder.create());
    }
}

