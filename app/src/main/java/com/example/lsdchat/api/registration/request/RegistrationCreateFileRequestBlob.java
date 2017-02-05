package com.example.lsdchat.api.registration.request;

import com.google.gson.annotations.SerializedName;

public class RegistrationCreateFileRequestBlob {
    @SerializedName("content_type")
    private String contentType;
    @SerializedName("name")
    private String name;

    public RegistrationCreateFileRequestBlob(String contentType, String name) {
        this.contentType = contentType;
        this.name = name;
    }
}
