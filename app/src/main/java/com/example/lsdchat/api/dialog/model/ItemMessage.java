package com.example.lsdchat.api.dialog.model;


import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class ItemMessage extends RealmObject {
    @Ignore
    public static final String CHAT_DIALOG_ID = "chatDialogId";
    @Ignore
    public static final String MESSAGE_ID = "id";
    @Ignore
    public static final String DATE_SENT = "dateSent";

    @PrimaryKey
    @SerializedName("_id")
    private String id;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
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
