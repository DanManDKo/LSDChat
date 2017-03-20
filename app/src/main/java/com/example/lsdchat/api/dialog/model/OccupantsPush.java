package com.example.lsdchat.api.dialog.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Set;

public class OccupantsPush {
    @SerializedName("occupants_ids")
    private Set<Integer> pushAll;

    public OccupantsPush(Set<Integer> pushAll) {
        this.pushAll = pushAll;
    }
}
