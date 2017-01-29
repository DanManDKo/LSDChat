package com.example.lsdchat.api.login.request;

import com.google.gson.annotations.SerializedName;


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

    public SessionRequestNoAuth(String applicationId, String authKey, int nonce, long timestamp, String signature) {
        this.applicationId = applicationId;
        this.authKey = authKey;
        this.nonce = nonce;
        this.timestamp = timestamp;
        this.signature = signature;
    }




}
