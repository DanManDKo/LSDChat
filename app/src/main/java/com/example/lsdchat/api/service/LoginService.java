package com.example.lsdchat.api.service;

import com.example.lsdchat.api.request.LoginRequest;
import com.example.lsdchat.api.response.LoginResponse;
import com.example.lsdchat.api.request.SessionRequestAuth;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.api.response.SessionResponse;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

public interface LoginService {

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.SESSION_REQUEST)
    Observable<SessionResponse> getSession(@Body SessionRequestAuth body);

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.LOGIN_REQUEST)
    Observable<LoginResponse> getLogin(@Header("QB-Token") String token, @Body LoginRequest body);

}
