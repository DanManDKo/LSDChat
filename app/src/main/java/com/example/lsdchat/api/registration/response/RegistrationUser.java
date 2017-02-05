package com.example.lsdchat.api.registration.response;

import com.google.gson.annotations.SerializedName;

public class RegistrationUser {
    @SerializedName("id")
    private Integer id;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("email")
    private String email;
    @SerializedName("login")
    private String login;
    @SerializedName("phone")
    private String phone;
    @SerializedName("website")
    private String website;
    @SerializedName("created_at")
    private String created;
    @SerializedName("updated_at")
    private String updated;
    @SerializedName("last_request_at")
    private String lastRequest;
    @SerializedName("external_user_id")
    private Integer externalUserId;
    @SerializedName("facebook_id")
    private Integer facebookId;
    @SerializedName("blob_id")
    private Integer blobId;
    @SerializedName("custom_data")
    private String customData;
    @SerializedName("user_tags")
    private String userTags;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(String lastRequest) {
        this.lastRequest = lastRequest;
    }

    public int getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(int externalUserId) {
        this.externalUserId = externalUserId;
    }

    public int getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(int facebookId) {
        this.facebookId = facebookId;
    }

    public int getBlobId() {
        return blobId;
    }

    public void setBlobId(int blobId) {
        this.blobId = blobId;
    }

    public String getCustomData() {
        return customData;
    }

    public void setCustomData(String customData) {
        this.customData = customData;
    }

    public String getUserTags() {
        return userTags;
    }

    public void setUserTags(String userTags) {
        this.userTags = userTags;
    }
}
