package com.example.lsdchat.api.registration.request;

import com.google.gson.annotations.SerializedName;

public class RegistrationDeclaringRequestSize {
    @SerializedName("size")
    private long size;

    public RegistrationDeclaringRequestSize(long size) {
        this.size = size;
    }
}
