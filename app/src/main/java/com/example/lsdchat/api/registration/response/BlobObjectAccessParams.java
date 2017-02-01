package com.example.lsdchat.api.registration.response;

import com.google.gson.annotations.SerializedName;

import org.simpleframework.xml.Element;

import javax.xml.transform.sax.SAXResult;


public class BlobObjectAccessParams {
    @Element(name = "Content-Type")
    private String contentType;
    @Element(name = "Expires")
    private String expires;
    @Element(name = "acl")
    private String acl;
    @Element(name = "key")
    private String key;
    @Element(name = "policy")
    private String policy;
    @Element(name = "success_action_status")
    private String actionStatuse;
    @Element(name = "x-amz-algorithm")
    private String algorithm;
    @Element(name = "x-amz-credential")
    private String credential;
    @Element(name = "x-amz-date")
    private String date;
    @Element(name = "x-amz-signature")
    private String signature;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getAcl() {
        return acl;
    }

    public void setAcl(String acl) {
        this.acl = acl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getActionStatuse() {
        return actionStatuse;
    }

    public void setActionStatuse(String actionStatuse) {
        this.actionStatuse = actionStatuse;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
