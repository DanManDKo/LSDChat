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
            @Part(ApiConstant.UploadParametres.CONTENT_TYPE) String contentType,
            @Part(ApiConstant.UploadParametres.EXPIRES) String expires,
            @Part(ApiConstant.UploadParametres.ACL) String acl,
            @Part(ApiConstant.UploadParametres.KEY) String key,
            @Part(ApiConstant.UploadParametres.POLICY) String policy,
            @Part(ApiConstant.UploadParametres.SUCCESS_ACTION_STATUS) String status,
            @Part(ApiConstant.UploadParametres.ALGORITHM) String algorithm,
            @Part(ApiConstant.UploadParametres.CREDENTIAL) String credential,
            @Part(ApiConstant.UploadParametres.DATE) String date,
            @Part(ApiConstant.UploadParametres.SIGNATURE) String signature,
            @Part(ApiConstant.UploadParametres.FILE) MultipartBody.Part file);
}
