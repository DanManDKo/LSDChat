package com.example.lsdchat.api.registration.response;

import com.example.lsdchat.api.registration.model.RegistrationUser;
import com.google.gson.annotations.SerializedName;

public class RegistrationResponse {

    @SerializedName("user")
    private RegistrationUser user;


    public RegistrationUser getUser() {
        return user;
    }

    public void setUser(RegistrationUser user) {
        this.user = user;
    }
}

