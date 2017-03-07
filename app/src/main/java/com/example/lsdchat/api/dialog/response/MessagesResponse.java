package com.example.lsdchat.api.dialog.response;


import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessagesResponse {
    @SerializedName("limit")
    private Integer limit;
    @SerializedName("skip")
    private Integer skip;
    @SerializedName("items")
    private List<ItemMessage> itemMessageList;

    public Integer getLimit() {
        return limit;
    }

    public Integer getSkip() {
        return skip;
    }

    public List<ItemMessage> getItemMessageList() {
        return itemMessageList;
    }
}
