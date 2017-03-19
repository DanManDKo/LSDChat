package com.example.lsdchat.api.dialog.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OccupantsPush {
    @SerializedName("occupants_ids")
    private List<Integer> pushAll;

    public OccupantsPush(List<Integer> pushAll) {
        this.pushAll = pushAll;
    }
}
