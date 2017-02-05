package com.example.lsdchat.api.registration.response;

import com.google.gson.annotations.SerializedName;

public class RegistrationCreateFileResponse {
    @SerializedName("blob")
    RegistrationCreateFileBlob blob;

    public RegistrationCreateFileBlob getBlob() {
        return blob;
    }

    public void setBlob(RegistrationCreateFileBlob blob) {
        this.blob = blob;
    }
}
