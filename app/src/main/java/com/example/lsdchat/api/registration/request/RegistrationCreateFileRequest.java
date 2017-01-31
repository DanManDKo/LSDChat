package com.example.lsdchat.api.registration.request;

import com.google.gson.annotations.SerializedName;

public class RegistrationCreateFileRequest {
    @SerializedName("blob")
    private RegistrationCreateFileRequestBlob blob;

    public RegistrationCreateFileRequest(RegistrationCreateFileRequestBlob blob) {
        this.blob = blob;
    }
}
