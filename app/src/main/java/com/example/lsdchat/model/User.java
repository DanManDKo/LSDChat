package com.example.lsdchat.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    @PrimaryKey
    private String email;
    private String password;
    private String fullName;
    private int id;
    private long blobId;
    private boolean isSignIn;

    public User() {
    }

    public User(String email, String password, String fullName, int id, long blobId, boolean isSignIn) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.id = id;
        this.blobId = blobId;
        this.isSignIn = isSignIn;
    }

    public User(String email, String password, String fullName, int id, boolean isSignIn) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.id = id;
        this.isSignIn = isSignIn;
    }


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

    public long getBlobId() {
        return blobId;
    }

    public void setBlobId(long blobId) {
        this.blobId = blobId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSignIn() {
        return isSignIn;
    }

    public void setSignIn(boolean signIn) {
        isSignIn = signIn;
    }
}
