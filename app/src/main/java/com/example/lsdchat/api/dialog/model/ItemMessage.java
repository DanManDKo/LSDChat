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


}
