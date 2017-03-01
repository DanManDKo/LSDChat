package com.example.lsdchat.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmMessage extends RealmObject {

    @PrimaryKey
    private String messageId;

    private String chatDialogId;
    private int senderId;
    private String message;
    private long dateSent;
    private int read;

    private boolean isSent;

    public static final String CHAT_DIALOG_ID = "chatDialogId";

    public RealmMessage() {
    }

    public RealmMessage(boolean isSent) {
        this.isSent = isSent;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public String getChatDialogId() {
        return chatDialogId;
    }

    public void setChatDialogId(String chatDialogId) {
        this.chatDialogId = chatDialogId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateSent() {
        Date date = new Date(dateSent);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    public void setDateSent(long dateSent) {
        this.dateSent = dateSent;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

}
