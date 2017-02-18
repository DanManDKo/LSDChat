package com.example.lsdchat.api.dialog.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CreateDialogRequest {
    @SerializedName("type")
    private Integer type;
    @SerializedName("name")
    private String name;
    @SerializedName("occupants_ids")
    private List<Long> occupantsIdsList;
    @SerializedName("photo")
    private long photoId;

    public void setType(Integer type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOccupantsIdsList(List<Long> occupantsIdsList) {
        this.occupantsIdsList = occupantsIdsList;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }

    public CreateDialogRequest() {

    }

    public CreateDialogRequest(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public CreateDialogRequest(Integer type, String name, long photoId) {
        this.type = type;
        this.name = name;
        this.photoId = photoId;
    }

    public CreateDialogRequest(Integer type, String name, List<Long> occupantsIdsList) {
        this.type = type;
        this.name = name;
        this.occupantsIdsList = occupantsIdsList;
    }

    public CreateDialogRequest(Integer type, String name, List<Long> occupantsIdsList, long photoId) {
        this.type = type;
        this.name = name;
        this.occupantsIdsList = occupantsIdsList;
        this.photoId = photoId;
    }

    public CreateDialogRequest(Integer type, List<Long> occupantsIdsList) {
        this.type = type;
        this.occupantsIdsList = occupantsIdsList;
    }
}
