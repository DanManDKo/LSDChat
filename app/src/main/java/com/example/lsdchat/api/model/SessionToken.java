package com.example.lsdchat.api.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;


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
