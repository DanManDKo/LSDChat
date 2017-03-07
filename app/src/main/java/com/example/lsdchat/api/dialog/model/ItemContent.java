package com.example.lsdchat.api.dialog.model;

import com.google.gson.annotations.SerializedName;



public class ItemContent {
    @SerializedName("params")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }
}
