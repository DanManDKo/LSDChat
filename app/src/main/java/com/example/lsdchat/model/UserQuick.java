package com.example.lsdchat.model;

import com.example.lsdchat.api.dialog.model.ItemUser;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class UserQuick extends RealmObject{
    @PrimaryKey
    private Integer id;

    private String fullName;
    private String email;
    private String login;
    private String phone;
    private String website;
    private String createdAt;
    private String updatedAt;
    private String lastRequestAt;
    private int externalUserId;
    private String facebookId;
    private String twitterId;
    private int twitterDigitsId;
    private int blobId;
    private String customData;
    private String userTags;
    private String imagePath;

    public UserQuick(ItemUser user) {
        this.id = user.getUser().getId();
        this.fullName = user.getUser().getFullName();
        this.email = user.getUser().getEmail();
        this.login = user.getUser().getLogin();
        this.phone = user.getUser().getPhone();
        this.website = user.getUser().getWebsite();
        this.createdAt = user.getUser().getCreatedAt();
        this.updatedAt = user.getUser().getUpdatedAt();
        this.lastRequestAt = user.getUser().getLastRequestAt();
        this.externalUserId = user.getUser().getExternalUserId();
        this.facebookId = user.getUser().getFacebookId();
        this.twitterId = user.getUser().getTwitterId();
        this.twitterDigitsId = user.getUser().getTwitterDigitsId();
        this.blobId = user.getUser().getBlobId();
        this.customData = user.getUser().getCustomData();
        this.userTags = user.getUser().getUserTags();
    }

    public UserQuick(ItemUser user,String imagePath) {
        this.id = user.getUser().getId();
        this.fullName = user.getUser().getFullName();
        this.email = user.getUser().getEmail();
        this.login = user.getUser().getLogin();
        this.phone = user.getUser().getPhone();
        this.website = user.getUser().getWebsite();
        this.createdAt = user.getUser().getCreatedAt();
        this.updatedAt = user.getUser().getUpdatedAt();
        this.lastRequestAt = user.getUser().getLastRequestAt();
        this.externalUserId = user.getUser().getExternalUserId();
        this.facebookId = user.getUser().getFacebookId();
        this.twitterId = user.getUser().getTwitterId();
        this.twitterDigitsId = user.getUser().getTwitterDigitsId();
        this.blobId = user.getUser().getBlobId();
        this.customData = user.getUser().getCustomData();
        this.userTags = user.getUser().getUserTags();
        this.imagePath = imagePath;
    }

    public UserQuick() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLastRequestAt() {
        return lastRequestAt;
    }

    public void setLastRequestAt(String lastRequestAt) {
        this.lastRequestAt = lastRequestAt;
    }

    public int getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(int externalUserId) {
        this.externalUserId = externalUserId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public int getTwitterDigitsId() {
        return twitterDigitsId;
    }

    public void setTwitterDigitsId(int twitterDigitsId) {
        this.twitterDigitsId = twitterDigitsId;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
