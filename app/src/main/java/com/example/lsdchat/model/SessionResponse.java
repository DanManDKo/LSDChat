package com.example.lsdchat.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by User on 25.01.2017.
 */

public class SessionResponse extends RealmObject {
    @SerializedName("_id")
    // TODO: 28.01.2017 [Code Review] do not use '_'
    private String _id;
    @SerializedName("application_id")
    private long applicationId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("device_id")
    private long deviceId;
    @SerializedName("nonce")
    private int nonce;
    @PrimaryKey
    @SerializedName("token")
    private String token;
    @SerializedName("ts")
    private int ts;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("user_id")
    private long userId;
    @SerializedName("id")
    private long id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTs() {
        return ts;
    }

    public void setTs(int ts) {
        this.ts = ts;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
