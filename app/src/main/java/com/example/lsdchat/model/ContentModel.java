package com.example.lsdchat.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class ContentModel extends RealmObject {

    @PrimaryKey
    private String id;
    private String imagePath;

    public ContentModel() {
    }

    public ContentModel(String id, String imagePath) {
        this.id = id;
        this.imagePath = imagePath;
    }

    public String getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }
}
