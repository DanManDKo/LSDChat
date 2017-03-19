package com.example.lsdchat.api.dialog.request;


import com.example.lsdchat.api.dialog.model.OccupantsPull;
import com.example.lsdchat.api.dialog.model.OccupantsPush;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateDialogRequest {
    @SerializedName("name")
    private String name;
    @SerializedName("photo")
    private long photoId;
    @SerializedName("pull_all")
    private OccupantsPull pullAll;
    @SerializedName("push_all")
    private OccupantsPush pushAll;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }

    public OccupantsPull getPullAll() {
        return pullAll;
    }

    public void setPullAll(OccupantsPull pullAll) {
        this.pullAll = pullAll;
    }

    public OccupantsPush getPushAll() {
        return pushAll;
    }

    public void setPushAll(OccupantsPush pushAll) {
        this.pushAll = pushAll;
    }
}
