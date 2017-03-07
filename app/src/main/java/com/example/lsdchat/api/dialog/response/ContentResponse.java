package com.example.lsdchat.api.dialog.response;

import com.example.lsdchat.api.dialog.model.ItemContent;
import com.google.gson.annotations.SerializedName;


public class ContentResponse {


    @SerializedName("blob_object_access")
    private ItemContent itemContent;

    public ItemContent getItemContent() {
        return itemContent;
    }
}
