package com.example.lsdchat.model;

import io.realm.RealmObject;



public class Session extends RealmObject {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
