package com.example.lsdchat.api.dialog.model;

import com.example.lsdchat.api.login.model.LoginUser;
import com.google.gson.annotations.SerializedName;

/**
 * Created by serj on 14.02.17.
 */

public class ItemUser {
    @SerializedName("user")
    private LoginUser user;

    public LoginUser getUser() {
        return user;
    }
}
