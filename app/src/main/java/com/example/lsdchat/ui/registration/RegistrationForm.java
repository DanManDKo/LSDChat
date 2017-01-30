package com.example.lsdchat.ui.registration;

public class RegistrationForm {
    private String mEmail;
    private String mPassword;
    private Integer mBlobId;
    private String mFullName;
    private String mWebsite;
    private Integer mFacebookId;
    private String mPhone;

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public Integer getBlobId() {
        return mBlobId;
    }

    public void setBlobId(Integer blobId) {
        mBlobId = blobId;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String fullName) {
        mFullName = fullName;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String website) {
        mWebsite = website;
    }

    public Integer getFacebookId() {
        return mFacebookId;
    }

    public void setFacebookId(Integer facebookId) {
        mFacebookId = facebookId;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }
}
