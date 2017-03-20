package com.example.lsdchat.api.dialog.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Set;

public class OccupantsPull {
    @SerializedName("occupants_ids")
    private Set<Integer> pullAll;

    public OccupantsPull(Set<Integer> pullAll) {
        this.pullAll = pullAll;
    }
}
