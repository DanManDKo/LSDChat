package com.example.lsdchat.api.service;

import com.example.lsdchat.api.request.SessionRequestNoAuth;
import com.example.lsdchat.api.response.SessionResponse;
import com.example.lsdchat.constant.ApiConstant;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;


public interface SessionService {

    @Headers("Content-Type: application/json")
    @POST(ApiConstant.SESSION_REQUEST)
    Observable<SessionResponse> getSession(@Body SessionRequestNoAuth body);

}
