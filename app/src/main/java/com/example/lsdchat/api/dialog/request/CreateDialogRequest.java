package com.example.lsdchat.api.dialog.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CreateDialogRequest {
    @SerializedName("type")
    private Integer type;
    @SerializedName("name")
    private String name;
    @SerializedName("occupants_ids")
    private List<Integer> occupantsIdsList;

    public CreateDialogRequest(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public CreateDialogRequest(Integer type, String name, List<Integer> occupantsIdsList) {
        this.type = type;
        this.name = name;
        this.occupantsIdsList = occupantsIdsList;
    }
}
