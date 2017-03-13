package com.example.lsdchat.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class UserAvatar extends RealmObject {

    @PrimaryKey
    private String userId;
    private String imagePath;

    public UserAvatar() {
    }

    public UserAvatar(String userId, String imagePath) {
        this.userId = userId;
        this.imagePath = imagePath;
    }

    public String getUserId() {
        return userId;
    }

    public String getImagePath() {
        return imagePath;
    }
}
