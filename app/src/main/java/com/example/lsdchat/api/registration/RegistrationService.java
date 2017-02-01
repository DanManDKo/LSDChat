package com.example.lsdchat.api.registration;

import com.example.lsdchat.api.login.request.LoginRequest;
import com.example.lsdchat.api.login.request.SessionRequestAuth;
import com.example.lsdchat.api.login.request.SessionRequestNoAuth;
import com.example.lsdchat.api.login.response.LoginResponse;
import com.example.lsdchat.api.login.response.SessionResponse;
import com.example.lsdchat.api.registration.request.RegistrationCreateFileRequest;
import com.example.lsdchat.api.registration.request.RegistrationRequest;
import com.example.lsdchat.api.registration.response.RegistrationCreateFileResponse;
import com.example.lsdchat.api.registration.response.RegistrationResponse;
import com.example.lsdchat.constant.ApiConstant;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @POST(ApiConstant.LOGIN_REQUEST)
    Observable<LoginResponse> getLogin(@Header(ApiConstant.QB_TOKEN) String token, @Body LoginRequest body);

//    @Multipart
//    @POST(ApiConstant.SERVER_AMAZON)
//    Observable<Void> getUploadFileRequest(@Field("Content-Type")String type,
//                                          @Field("Expires") String expires,
//                                          @Part MultipartBody.Part file);

}
