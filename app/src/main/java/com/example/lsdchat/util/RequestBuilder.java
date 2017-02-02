package com.example.lsdchat.util;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RequestBuilder {
    //Upload request body
    public static MultipartBody uploadRequestBody(String type, String expires, String acl, String key, String policy, String actionStatus, String algorithm, String credential, String date, String signature, File file) {

        MediaType mediaType = MediaType.parse("image/" + "jpg");
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("Content-Type", type)
                .addFormDataPart("Expires", expires)
                .addFormDataPart("acl", acl) //e.g. title.png --> imageFormat = png
                .addFormDataPart("key", key)
                .addFormDataPart("policy", policy)
                .addFormDataPart("success_action_status", actionStatus)
                .addFormDataPart("x-amz-algorithm", algorithm)
                .addFormDataPart("x-amz-credential", credential)
                .addFormDataPart("x-amz-date", date)
                .addFormDataPart("x-amz-signature", signature)

                .addFormDataPart("file", "...", RequestBody.create(mediaType, file))

                .build();
    }
}
