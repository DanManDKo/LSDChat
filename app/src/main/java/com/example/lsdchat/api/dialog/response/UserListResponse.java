package com.example.lsdchat.api.dialog.response;

import com.example.lsdchat.api.dialog.model.ItemUser;
import com.google.gson.annotations.SerializedName;

import java.util.List;



public class UserListResponse {
    @SerializedName("total_entries")
    private Integer totalEntries;

    @SerializedName("items")
    private List<ItemUser> itemUserList;

    public Integer getTotalEntries() {
        return totalEntries;
    }

    public List<ItemUser> getItemUserList() {
        return itemUserList;
    }
}
