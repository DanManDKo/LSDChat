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
    private Integer facebookId;
    @SerializedName("blob_id")
    private Integer blobId;


    public RegistrationRequestUser(String email, String password, String fullName, String phone, String website, Integer facebookId, Integer blobId) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.website = website;
        this.facebookId = facebookId;
        this.blobId = blobId;
    }
}
