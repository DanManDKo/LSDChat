package com.example.lsdchat.api.login.service;

import com.example.lsdchat.api.login.request.LoginRequest;
import com.example.lsdchat.api.login.response.LoginResponse;
import com.example.lsdchat.api.login.request.SessionRequestAuth;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.api.login.response.SessionResponse;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

public interface LoginService {

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @POST(ApiConstant.SESSION_REQUEST)
    Observable<SessionResponse> getSession(@Body SessionRequestAuth body);

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @POST(ApiConstant.LOGIN_REQUEST)
    Observable<LoginResponse> getLogin(@Header(ApiConstant.QB_TOKEN) String token, @Body LoginRequest body);



}
