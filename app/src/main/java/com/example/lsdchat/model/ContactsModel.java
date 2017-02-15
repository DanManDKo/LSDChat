package com.example.lsdchat.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ContactsModel extends RealmObject {
    @PrimaryKey
    private String name;
    private String email;
    private boolean checked;

    private String uri;

    public ContactsModel() {
    }

    public ContactsModel(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public ContactsModel(String name, String email, String uri) {
        this.name = name;
        this.email = email;
        this.uri = uri;
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
