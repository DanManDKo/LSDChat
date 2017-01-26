package com.example.lsdchat.api;

import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.model.SessionRequestBody;
import com.example.lsdchat.model.SessionResponse;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

public interface ApiService {
    @Headers("Content-Type: application/json")
    @POST(ApiConstant.SESSION_REQUEST)
    Observable<Response<SessionResponse>> getSession(@Body SessionRequestBody body);
}
