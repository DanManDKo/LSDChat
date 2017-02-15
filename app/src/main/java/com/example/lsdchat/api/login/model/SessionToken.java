package com.example.lsdchat.api.login.model;

import com.google.gson.annotations.SerializedName;


public class SessionToken {
    @SerializedName("token")
    private String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
