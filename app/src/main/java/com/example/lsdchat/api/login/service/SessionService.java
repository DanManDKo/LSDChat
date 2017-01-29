package com.example.lsdchat.api.login.service;

import com.example.lsdchat.api.login.request.SessionRequestNoAuth;
import com.example.lsdchat.api.login.response.SessionResponse;
import com.example.lsdchat.constant.ApiConstant;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;


public interface SessionService {

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @POST(ApiConstant.SESSION_REQUEST)
    Observable<SessionResponse> getSession(@Body SessionRequestNoAuth body);

}
