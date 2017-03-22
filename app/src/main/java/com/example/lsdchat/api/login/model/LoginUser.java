package com.example.lsdchat.api.login.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LoginUser extends RealmObject implements Parcelable {
    public static final Creator<LoginUser> CREATOR = new Creator<LoginUser>() {
        @Override
        public LoginUser createFromParcel(Parcel in) {
            return new LoginUser(in);
        }

        @Override
        public LoginUser[] newArray(int size) {
            return new LoginUser[size];
        }
    };
    @PrimaryKey
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
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("last_request_at")
    private String lastRequestAt;
    @SerializedName("external_user_id")
    private int externalUserId;
    @SerializedName("facebook_id")
    private String facebookId;
    @SerializedName("twitter_id")
    private String twitterId;
    @SerializedName("twitter_digits_id")
    private int twitterDigitsId;
    @SerializedName("blob_id")
    private int blobId;
    @SerializedName("custom_data")
    private String customData;
    @SerializedName("user_tags")
    private String userTags;

    private String imagePath;

    public LoginUser() {
    }

    public LoginUser(LoginUser user, String imagePath) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.login = user.getLogin();
        this.phone = user.getPhone();
        this.website = user.getWebsite();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.lastRequestAt = user.getLastRequestAt();
        this.externalUserId = user.getExternalUserId();
        this.facebookId = user.getFacebookId();
        this.twitterId = user.getTwitterId();
        this.twitterDigitsId = user.getTwitterDigitsId();
        this.blobId = user.getBlobId();
        this.customData = user.getCustomData();
        this.userTags = user.getUserTags();
        this.imagePath = imagePath;
    }

    protected LoginUser(Parcel in) {
        fullName = in.readString();
        email = in.readString();
        login = in.readString();
        phone = in.readString();
        website = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        lastRequestAt = in.readString();
        externalUserId = in.readInt();
        facebookId = in.readString();
        twitterId = in.readString();
        twitterDigitsId = in.readInt();
        blobId = in.readInt();
        customData = in.readString();
        userTags = in.readString();
        imagePath = in.readString();
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(fullName);
        dest.writeString(email);
        dest.writeString(login);
        dest.writeString(phone);
        dest.writeString(website);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(lastRequestAt);
        dest.writeInt(externalUserId);
        dest.writeString(facebookId);
        dest.writeString(twitterId);
        dest.writeInt(twitterDigitsId);
        dest.writeInt(blobId);
        dest.writeString(customData);
        dest.writeString(userTags);
        dest.writeString(imagePath);
    }
}
