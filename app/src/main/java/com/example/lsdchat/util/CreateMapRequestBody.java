package com.example.lsdchat.util;

import android.net.Uri;

import com.example.lsdchat.constant.ApiConstant;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class CreateMapRequestBody {

    public static HashMap<String, RequestBody> createMapRequestBody(Uri uri) {
        HashMap<String, RequestBody> map = new HashMap<>();

        RequestBody contentR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.CONTENT_TYPE));
        RequestBody expiresR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.EXPIRES));
        RequestBody aclR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.ACL));
        RequestBody keyR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.KEY));
        RequestBody policyR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.POLICY));
        RequestBody successR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.SUCCESS_ACTION_STATUS));
        RequestBody algorithmR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.ALGORITHM));
        RequestBody credentialR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.CREDENTIAL));
        RequestBody dateR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.DATE));
        RequestBody signatureR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.SIGNATURE));

        map.put(ApiConstant.UploadParametres.CONTENT_TYPE, contentR);
        map.put(ApiConstant.UploadParametres.EXPIRES, expiresR);
        map.put(ApiConstant.UploadParametres.ACL, aclR);
        map.put(ApiConstant.UploadParametres.KEY, keyR);
        map.put(ApiConstant.UploadParametres.POLICY, policyR);
        map.put(ApiConstant.UploadParametres.SUCCESS_ACTION_STATUS, successR);
        map.put(ApiConstant.UploadParametres.ALGORITHM, algorithmR);
        map.put(ApiConstant.UploadParametres.CREDENTIAL, credentialR);
        map.put(ApiConstant.UploadParametres.DATE, dateR);
        map.put(ApiConstant.UploadParametres.SIGNATURE, signatureR);

        return map;
    }



}
