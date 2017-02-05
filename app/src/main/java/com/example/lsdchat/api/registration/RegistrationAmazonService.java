package com.example.lsdchat.api.registration;

import com.example.lsdchat.constant.ApiConstant;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

public interface RegistrationAmazonService {
    @Multipart
    @POST(ApiConstant.SERVER_AMAZON)
    Observable<Void> uploadFileRequest(
            @Part(ApiConstant.UploadParametres.CONTENT_TYPE) RequestBody contentType,
            @Part(ApiConstant.UploadParametres.EXPIRES) RequestBody expires,
            @Part(ApiConstant.UploadParametres.ACL) RequestBody acl,
            @Part(ApiConstant.UploadParametres.KEY) RequestBody key,
            @Part(ApiConstant.UploadParametres.POLICY) RequestBody policy,
            @Part(ApiConstant.UploadParametres.SUCCESS_ACTION_STATUS) RequestBody status,
            @Part(ApiConstant.UploadParametres.ALGORITHM) RequestBody algorithm,
            @Part(ApiConstant.UploadParametres.CREDENTIAL) RequestBody credential,
            @Part(ApiConstant.UploadParametres.DATE) RequestBody date,
            @Part(ApiConstant.UploadParametres.SIGNATURE) RequestBody signature,
            @Part MultipartBody.Part file);
}
