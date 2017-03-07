package com.example.lsdchat.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by serj on 2/27/17.
 */

public class UserImage extends RealmObject {
    @PrimaryKey
    private int userId;
    private String imagePath;

    public UserImage() {
    }

    public UserImage(int userId, String imagePath) {
        this.userId = userId;
        this.imagePath = imagePath;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
