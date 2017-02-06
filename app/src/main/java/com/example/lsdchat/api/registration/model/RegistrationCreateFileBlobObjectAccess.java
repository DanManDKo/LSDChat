package com.example.lsdchat.api.registration.model;

import com.google.gson.annotations.SerializedName;

public class RegistrationCreateFileBlobObjectAccess {
    @SerializedName("blob_id")
    private Integer blobId;
    @SerializedName("expires")
    private String expires;
    @SerializedName("id")
    private Integer id;
    @SerializedName("object_access_type")
    private String objectAccessType;
    @SerializedName("params")
    private String params;

    public Integer getBlobId() {
        return blobId;
    }

    public void setBlobId(Integer blobId) {
        this.blobId = blobId;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObjectAccessType() {
        return objectAccessType;
    }

    public void setObjectAccessType(String objectAccessType) {
        this.objectAccessType = objectAccessType;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
