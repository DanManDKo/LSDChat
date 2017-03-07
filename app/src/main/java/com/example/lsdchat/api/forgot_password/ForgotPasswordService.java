package com.example.lsdchat.api.forgot_password;

import com.example.lsdchat.constant.ApiConstant;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by User on 31.01.2017.
 */

public interface ForgotPasswordService {
    @GET(ApiConstant.FORGOT_PASSWORD_REQUEST)
//    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    Observable<Response<Void>> sendEmail(@Query("email") String email, @Header("QB-Token") String token);
}
