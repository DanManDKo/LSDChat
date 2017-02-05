package com.example.lsdchat.util;

import com.example.lsdchat.constant.ApiConstant;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RequestBuilder {
    private static final String MIME_TYPE = "image/jpg";
    private static final String FILE_NAME = "avatar";
    //Upload request body
    public static MultipartBody uploadRequestBody(String type, String expires, String acl, String key, String policy, String actionStatus, String algorithm, String credential, String date, String signature, File file) {

        MediaType mediaType = MediaType.parse(MIME_TYPE);
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(ApiConstant.UploadParametres.CONTENT_TYPE, type)
                .addFormDataPart(ApiConstant.UploadParametres.EXPIRES, expires)
                .addFormDataPart(ApiConstant.UploadParametres.ACL, acl)
                .addFormDataPart(ApiConstant.UploadParametres.KEY, key)
                .addFormDataPart(ApiConstant.UploadParametres.POLICY, policy)
                .addFormDataPart(ApiConstant.UploadParametres.SUCCESS_ACTION_STATUS, actionStatus)
                .addFormDataPart(ApiConstant.UploadParametres.ALGORITHM, algorithm)
                .addFormDataPart(ApiConstant.UploadParametres.CREDENTIAL, credential)
                .addFormDataPart(ApiConstant.UploadParametres.DATE, date)
                .addFormDataPart(ApiConstant.UploadParametres.SIGNATURE, signature)

                .addFormDataPart(ApiConstant.UploadParametres.FILE, FILE_NAME, RequestBody.create(mediaType, file))

                .build();
    }
}
