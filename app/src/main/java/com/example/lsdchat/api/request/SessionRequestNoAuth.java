package com.example.lsdchat.api.request;

import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.util.SignatureTest;
import com.google.gson.annotations.SerializedName;

import java.util.Random;



public class SessionRequestNoAuth {
    @SerializedName("application_id")
    private String applicationId;
    @SerializedName("auth_key")
    private String authKey;
    @SerializedName("nonce")
    private int nonce;
    @SerializedName("timestamp")
    private long timestamp;
    @SerializedName("signature")
    private String signature;


    public SessionRequestNoAuth() {
        // TODO: 28.01.2017 [Code Review] The same as in SessionRequestAuth constructor. Factory is a
        // good approach for creating SessionRequestAuth and SessionRequestNoAuth class instances
        this.applicationId = ApiConstant.APP_ID;
        this.authKey = ApiConstant.AUTH_KEY;
        this.nonce = new Random().nextInt();
        this.timestamp = System.currentTimeMillis() / 1000;
        this.signature = SignatureTest.calculateSignatureNoAuth(nonce, timestamp);

    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }






}
