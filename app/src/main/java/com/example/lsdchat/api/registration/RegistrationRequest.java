package com.example.lsdchat.api.registration;

import com.google.gson.annotations.SerializedName;

public class RegistrationRequest {
    @SerializedName("user")
    private RegistrationRequestUser user;

    public RegistrationRequest(RegistrationRequestUser user) {
        this.user = user;
    }

    public RegistrationRequestUser getUser() {
        return user;
    }

    public void setUser(RegistrationRequestUser user) {
        this.user = user;
    }
}
