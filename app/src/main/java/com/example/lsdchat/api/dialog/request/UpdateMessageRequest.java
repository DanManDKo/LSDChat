package com.example.lsdchat.api.dialog.request;

import com.google.gson.annotations.SerializedName;

public class UpdateMessageRequest {
    @SerializedName("message")
    private String message;
    @SerializedName("chat_dialog_id")
    private String dialogID;
    @SerializedName("read")
    private String read;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDialogID() {
        return dialogID;
    }

    public void setDialogID(String dialogID) {
        this.dialogID = dialogID;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }
}
