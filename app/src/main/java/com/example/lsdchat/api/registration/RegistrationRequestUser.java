package com.example.lsdchat.api.registration;

import com.google.gson.annotations.SerializedName;

public class RegistrationRequestUser {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("phone")
    private String phone;
    @SerializedName("website")
    private String website;
    @SerializedName("facebook_id")
    private String facebookId;

    public RegistrationRequestUser(String email, String password, String fullName, String phone, String website) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.website = website;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }
}
