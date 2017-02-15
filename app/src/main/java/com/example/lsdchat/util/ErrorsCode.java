package com.example.lsdchat.util;

import android.content.Context;

import com.example.lsdchat.R;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;


public class ErrorsCode {

    public static String getErrorMessage(Context context, Throwable throwable) {
        if (throwable instanceof HttpException) {
            int code = ((HttpException) throwable).code();
            switch (code) {
                case 400:
                    return context.getString(R.string.error_code_400);
                case 401:
                    return context.getString(R.string.error_code_401);
                case 403:
                    return context.getString(R.string.error_code_403);
                case 404:
                    return context.getString(R.string.error_code_404);
                case 422:
                    return context.getString(R.string.error_code_422);
                case 500:
                    return context.getString(R.string.error_code_500);
                case 503:
                    return context.getString(R.string.error_code_503);
                default:
                    return context.getString(R.string.error_code_unknown);
            }
        } else if (throwable instanceof IOException) {
            return context.getString(R.string.error_code_noconnect);
        } else {
            return context.getString(R.string.error_code_unknown);
        }
    }



}
