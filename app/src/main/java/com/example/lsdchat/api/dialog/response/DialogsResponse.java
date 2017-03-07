package com.example.lsdchat.api.dialog.response;

import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class DialogsResponse {

    @SerializedName("total_entries")
    private Integer totalEntries;
    @SerializedName("limit")
    private Integer limit;
    @SerializedName("skip")
    private Integer skip;
    @SerializedName("items")
    private List<ItemDialog> itemDialogList;

    public Integer getTotalEntries() {
        return totalEntries;
    }

    public void setTotalEntries(Integer totalEntries) {
        this.totalEntries = totalEntries;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    public List<ItemDialog> getItemDialogList() {
        return itemDialogList;
    }

    public void setItemDialogList(List<ItemDialog> itemDialogList) {
        this.itemDialogList = itemDialogList;
    }
}
