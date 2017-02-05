package com.example.lsdchat.api.registration.request;

import com.google.gson.annotations.SerializedName;

public class RegistrationDeclaringRequest {
    @SerializedName("blob")
    private RegistrationDeclaringRequestSize size;

    public RegistrationDeclaringRequest(RegistrationDeclaringRequestSize size) {
        this.size = size;
    }
}
