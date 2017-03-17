package com.example.lsdchat.api.dialog.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OccupantsPull {
    @SerializedName("occupants_ids")
    private List<Integer> pullAll;

    public OccupantsPull(List<Integer> pullAll) {
        this.pullAll = pullAll;
    }

    public List<Integer> getPullAll() {
        return pullAll;
    }

    public void setPullAll(List<Integer> pullAll) {
        this.pullAll = pullAll;
    }
}
