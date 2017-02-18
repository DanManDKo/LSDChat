package com.example.lsdchat.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ContactsModel extends RealmObject {

    private String name;
    private String email;
    private boolean checked;
    @PrimaryKey
    private long userId;
    private String uri;

    public ContactsModel() {
    }

    public ContactsModel(String name, String email, long userId) {
        this.name = name;
        this.email = email;
        this.userId = userId;
    }

    public ContactsModel(String name, String email, String uri, long userId) {
        this.name = name;
        this.email = email;
        this.uri = uri;
        this.userId = userId;

    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
