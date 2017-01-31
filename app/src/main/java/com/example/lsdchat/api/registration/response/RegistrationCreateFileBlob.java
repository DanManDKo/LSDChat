package com.example.lsdchat.api.registration.response;

import com.google.gson.annotations.SerializedName;

public class RegistrationCreateFileBlob {
    @SerializedName("content_type")
    private String contentType;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("name")
    private String name;
    @SerializedName("size")
    private Integer size;
    @SerializedName("uid")
    private String uid;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("blob_object_access")
    private RegistrationCreateFileBlobObjectAccess blobObjestAccess;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public RegistrationCreateFileBlobObjectAccess getBlobObjestAccess() {
        return blobObjestAccess;
    }

    public void setBlobObjestAccess(RegistrationCreateFileBlobObjectAccess blobObjestAccess) {
        this.blobObjestAccess = blobObjestAccess;
    }
}
