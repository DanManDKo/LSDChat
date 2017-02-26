package com.example.lsdchat.api.registration.request;

import com.google.gson.annotations.SerializedName;

public class UpdateRequest {
    @SerializedName("user")
    private UpdateRequestUser user;

    public UpdateRequest(UpdateRequestUser user) {
        this.user = user;
    }

}
