package com.example.lsdchat.api.dialog.request;

import com.example.lsdchat.api.dialog.model.MessageAttachments;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by serj on 12.02.17.
 */

public class CreateMessageRequest {
    @SerializedName("chat_dialog_id")
    private String chatDialogId;
    @SerializedName("message")
    private String message;
    @SerializedName("recipient_id")
    private Integer recipientId;
    @SerializedName("attachments")
    private List<MessageAttachments> attachments;

    public CreateMessageRequest(String chatDialogId, String message) {
        this.chatDialogId = chatDialogId;
        this.message = message;
    }

    public CreateMessageRequest(String message, Integer recipientId) {
        this.message = message;
        this.recipientId = recipientId;
    }

    public CreateMessageRequest() {
    }

    public CreateMessageRequest(String chatDialogId, String message, List<MessageAttachments> attachments) {
        this.chatDialogId = chatDialogId;
        this.message = message;
        this.attachments = attachments;
    }

    public CreateMessageRequest(String chatDialogId, String message, Integer recipientId, List<MessageAttachments> attachments) {

        this.chatDialogId = chatDialogId;
        this.message = message;
        this.recipientId = recipientId;
        this.attachments = attachments;
    }

    public String getChatDialogId() {
        return chatDialogId;
    }

    public void setChatDialogId(String chatDialogId) {
        this.chatDialogId = chatDialogId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public List<MessageAttachments> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MessageAttachments> attachments) {
        this.attachments = attachments;
    }
}
