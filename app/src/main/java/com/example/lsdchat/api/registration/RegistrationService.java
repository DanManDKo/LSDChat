package com.example.lsdchat.api.registration;

import com.example.lsdchat.api.login.request.LoginRequest;
import com.example.lsdchat.api.login.request.SessionRequestAuth;
import com.example.lsdchat.api.login.request.SessionRequestNoAuth;
import com.example.lsdchat.api.login.response.LoginResponse;
import com.example.lsdchat.api.login.response.SessionResponse;
import com.example.lsdchat.api.registration.request.RegistrationCreateFileRequest;
import com.example.lsdchat.api.registration.request.RegistrationDeclaringRequest;
import com.example.lsdchat.api.registration.request.RegistrationRequest;
import com.example.lsdchat.api.registration.response.RegistrationCreateFileResponse;
import com.example.lsdchat.api.registration.response.RegistrationResponse;
import com.example.lsdchat.constant.ApiConstant;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

public interface RegistrationService {

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @POST(ApiConstant.SESSION_REQUEST)
    Observable<SessionResponse> getSession(@Body SessionRequestNoAuth body);

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
//    Observable<Void> uploadFileRequest(
//            @Part(ApiConstant.UploadParametres.CONTENT_TYPE) String contentType,
//            @Part(ApiConstant.UploadParametres.EXPIRES) String expires,
//            @Part(ApiConstant.UploadParametres.ACL) String acl,
//            @Part(ApiConstant.UploadParametres.KEY) String key,
//            @Part(ApiConstant.UploadParametres.POLICY) String policy,
//            @Part(ApiConstant.UploadParametres.SUCCESS_ACTION_STATUS) String status,
//            @Part(ApiConstant.UploadParametres.ALGORITHM) String algorithm,
//            @Part(ApiConstant.UploadParametres.CREDENTIAL) String credential,
//            @Part(ApiConstant.UploadParametres.DATE) String date,
//            @Part(ApiConstant.UploadParametres.SIGNATURE) String signature,
//            @Part(ApiConstant.UploadParametres.FILE) RequestBody file);

    @PUT(ApiConstant.DECLARING_REQUEST)
    Observable<Void> declaringFileUploadedRequest(@Path(ApiConstant.UploadParametres.BLOB_ID) long bloId, @Header(ApiConstant.QB_TOKEN) String token, @Body RegistrationDeclaringRequest body);
}
