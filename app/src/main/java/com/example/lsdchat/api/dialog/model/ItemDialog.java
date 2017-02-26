package com.example.lsdchat.api.dialog.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmObject;


public class ItemDialog extends RealmObject {
    @SerializedName("_id")
    private String id;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("last_message")
    private String lastMessage;
    @SerializedName("last_message_date_sent")
    private String lastMessageDateSent;
    @SerializedName("last_message_user_id")
    private Integer lastMessageUserId;
    @SerializedName("name")
    private String name;
    @SerializedName("photo")
    private String photo;
    @SerializedName("occupants_ids")
    private List<Integer> occupantsIdsList;
    @SerializedName("type")
    private Integer type;
    @SerializedName("unread_messages_count")
    private Integer unreadMessagesCount;
    @SerializedName("xmpp_room_jid")
    private String xmppRoomJid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageDateSent() {
        return lastMessageDateSent;
    }

    public void setLastMessageDateSent(String lastMessageDateSent) {
        this.lastMessageDateSent = lastMessageDateSent;
    }

    public Integer getLastMessageUserId() {
        return lastMessageUserId;
    }

    public void setLastMessageUserId(Integer lastMessageUserId) {
        this.lastMessageUserId = lastMessageUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<Integer> getOccupantsIdsList() {
        return occupantsIdsList;
    }

    public void setOccupantsIdsList(List<Integer> occupantsIdsList) {
        this.occupantsIdsList = occupantsIdsList;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    public void setUnreadMessagesCount(Integer unreadMessagesCount) {
        this.unreadMessagesCount = unreadMessagesCount;
    }

    public String getXmppRoomJid() {
        return xmppRoomJid;
    }

    public void setXmppRoomJid(String xmppRoomJid) {
        this.xmppRoomJid = xmppRoomJid;
    }
}
