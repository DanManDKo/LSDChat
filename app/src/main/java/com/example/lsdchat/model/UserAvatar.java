package com.example.lsdchat.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class UserAvatar extends RealmObject {

    @PrimaryKey
    private int userId;
    private String imagePath;

    public UserAvatar() {
    }

    public UserAvatar(int userId, String imagePath) {
        this.userId = userId;
        this.imagePath = imagePath;
    }

    public int getUserId() {
        return userId;
    }

    public String getImagePath() {
        return imagePath;
    }
}
