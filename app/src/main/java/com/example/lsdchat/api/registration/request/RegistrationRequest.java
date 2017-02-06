package com.example.lsdchat.api.registration.request;

import com.google.gson.annotations.SerializedName;

public class RegistrationRequest {
    @SerializedName("user")
    private RegistrationRequestUser user;

    public RegistrationRequest(RegistrationRequestUser user) {
        this.user = user;
    }

}
