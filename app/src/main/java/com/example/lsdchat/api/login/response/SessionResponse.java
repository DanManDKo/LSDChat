package com.example.lsdchat.api.login.response;

import com.example.lsdchat.api.login.model.SessionToken;
import com.google.gson.annotations.SerializedName;


public class SessionResponse {

    @SerializedName("session")
    private SessionToken session;

    public SessionToken getSession() {
        return session;
    }


}