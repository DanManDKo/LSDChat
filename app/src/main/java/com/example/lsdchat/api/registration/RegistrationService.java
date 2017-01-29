package com.example.lsdchat.api.registration;

import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.ui.registration.RegistrationContract;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

public interface RegistrationService {

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @POST(ApiConstant.REGISTRATION_REQUEST)
    Observable<RegistrationResponse> getRegistrationRequest(@Header("QB-Token") String token,
                                                             @Body RegistrationRequest body);

}
