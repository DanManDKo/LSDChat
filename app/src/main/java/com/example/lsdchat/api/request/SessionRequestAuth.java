package com.example.lsdchat.api.request;

import com.google.gson.annotations.SerializedName;


public class SessionRequestAuth {
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
    @SerializedName("user[email]")
    private String email;
    @SerializedName("user[password]")
    private String password;

    public SessionRequestAuth(String applicationId, String authKey, int nonce, long timestamp, String signature, String email, String password) {
        this.applicationId = applicationId;
        this.authKey = authKey;
        this.nonce = nonce;
        this.timestamp = timestamp;
        this.signature = signature;
        this.email = email;
        this.password = password;
    }


}
