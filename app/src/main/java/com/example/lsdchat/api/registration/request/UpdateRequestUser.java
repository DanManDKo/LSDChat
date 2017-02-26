package com.example.lsdchat.api.registration.request;

import com.google.gson.annotations.SerializedName;

public class UpdateRequestUser {
    @SerializedName("blob_id")
    private long blobId;

    public UpdateRequestUser(long blobId) {
        this.blobId = blobId;
    }
}
