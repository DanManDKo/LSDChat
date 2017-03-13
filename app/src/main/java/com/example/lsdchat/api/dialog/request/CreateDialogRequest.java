package com.example.lsdchat.api.dialog.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CreateDialogRequest {
    @SerializedName("type")
    private Integer type;
    @SerializedName("name")
    private String name;
//    @SerializedName("occupants_ids")
    private List<String> occupantsIdsList;
    @SerializedName("photo")
    private long photoId;
        @SerializedName("occupants_ids")
    private String idU;

    public CreateDialogRequest() {

    }

    public CreateDialogRequest(String name) {
        this.name = name;
    }

    public void setOccupantsIdsList(List<String> occupantsIdsList) {
        this.occupantsIdsList = occupantsIdsList;
    }

    public void setIdU(String idU) {
        this.idU = idU;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }


}
