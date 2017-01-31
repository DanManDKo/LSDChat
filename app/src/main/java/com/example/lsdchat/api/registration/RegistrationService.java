package com.example.lsdchat.api.registration;

import com.example.lsdchat.api.login.request.SessionRequestAuth;
import com.example.lsdchat.api.login.request.SessionRequestNoAuth;
import com.example.lsdchat.api.login.response.SessionResponse;
import com.example.lsdchat.api.registration.request.RegistrationCreateFileRequest;
import com.example.lsdchat.api.registration.request.RegistrationRequest;
import com.example.lsdchat.api.registration.response.RegistrationCreateFileResponse;
import com.example.lsdchat.api.registration.response.RegistrationResponse;
import com.example.lsdchat.constant.ApiConstant;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

public interface RegistrationService {

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @POST(ApiConstant.SESSION_REQUEST)
    Observable<SessionResponse> getSession(@Body SessionRequestNoAuth body);

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @POST(ApiConstant.SESSION_REQUEST)
    Observable<SessionResponse> getSessionAuthRequest(@Body SessionRequestAuth body);

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @POST(ApiConstant.REGISTRATION_REQUEST)
    Observable<RegistrationResponse> getRegistrationRequest(@Header(ApiConstant.QB_TOKEN) String token, @Body RegistrationRequest body);

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @POST(ApiConstant.BLOB_REQUEST)
    Observable<RegistrationCreateFileResponse> createFileRequest(@Header(ApiConstant.QB_TOKEN) String token, @Body RegistrationCreateFileRequest body);

}
