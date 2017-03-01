package com.example.lsdchat.api.dialog.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemMessage {
    @SerializedName("_id")
    private String id;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("attachments")
    private List<MessageAttachments> attachments;
    @SerializedName("read_ids")
    private List<Integer> readIdsList;
    @SerializedName("delivered_ids")
    private List<Integer> deliveredIdsList;
    @SerializedName("chat_dialog_id")
    private String chatDialogId;
    @SerializedName("date_sent")
    private String dateSent;
    @SerializedName("message")
    private String message;
    @SerializedName("recipient_id")
    private Integer recipientId;
    @SerializedName("sender_id")
    private Integer sender_id;
    @SerializedName("read")
    private Integer read;

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public List<MessageAttachments> getAttachments() {
        return attachments;
    }

    public List<Integer> getReadIdsList() {
        return readIdsList;
    }

    public List<Integer> getDeliveredIdsList() {
        return deliveredIdsList;
    }

    public String getChatDialogId() {
        return chatDialogId;
    }

    public String getDateSent() {
        return dateSent;
    }

    public String getMessage() {
        return message;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public Integer getSender_id() {
        return sender_id;
    }

    public Integer getRead() {
        return read;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setAttachments(List<MessageAttachments> attachments) {
        this.attachments = attachments;
    }

    public void setReadIdsList(List<Integer> readIdsList) {
        this.readIdsList = readIdsList;
    }

    public void setDeliveredIdsList(List<Integer> deliveredIdsList) {
        this.deliveredIdsList = deliveredIdsList;
    }

    public void setChatDialogId(String chatDialogId) {
        this.chatDialogId = chatDialogId;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public void setSender_id(Integer sender_id) {
        this.sender_id = sender_id;
    }

    public void setRead(Integer read) {
        this.read = read;
    }
}
