package com.example.lsdchat.api.response;

import com.example.lsdchat.api.model.LoginUser;
import com.google.gson.annotations.SerializedName;


public class LoginResponse {
    @SerializedName("user")
    private LoginUser loginUser;

    public LoginUser getLoginUser() {
        return loginUser;
    }

}
