package com.example.lsdchat.api.dialog.model;

import com.google.gson.annotations.SerializedName;


public class MessageAttachments {
    @SerializedName("type")
    private String type;
    @SerializedName("id")
    private Integer id;
    @SerializedName("url")
    private String url;

    public MessageAttachments() {
    }

    public MessageAttachments(String type, Integer id, String url) {

        this.type = type;
        this.id = id;
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public Integer getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
