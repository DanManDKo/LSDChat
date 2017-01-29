package com.example.lsdchat.api.login.response;

import com.example.lsdchat.api.login.model.LoginUser;
import com.google.gson.annotations.SerializedName;


public class LoginResponse {
    @SerializedName("user")
    private LoginUser loginUser;

    public LoginUser getLoginUser() {
        return loginUser;
    }

}
