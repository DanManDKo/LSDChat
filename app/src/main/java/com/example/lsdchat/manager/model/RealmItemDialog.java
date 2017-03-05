package com.example.lsdchat.manager.model;

import com.example.lsdchat.api.dialog.model.ItemDialog;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by User on 05.03.2017.
 */

public class RealmItemDialog extends RealmObject {
    @PrimaryKey
    private String id;
    private String createdAt;
    private String updatedAt;
    private String lastMessage;
    private String lastMessageDateSent;
    private Integer lastMessageUserId;
    private String name;
    private String photo;
    private RealmList<RealmInteger> occupantsIdsList;
    private Integer type;
    private Integer unreadMessagesCount;
    private String xmppRoomJid;

    public RealmItemDialog() {
    }

    public RealmItemDialog(ItemDialog itemDialog) {
        id = itemDialog.getId();
        createdAt = itemDialog.getCreatedAt();
        updatedAt = itemDialog.getUpdatedAt();
        lastMessage = itemDialog.getLastMessage();
        lastMessageDateSent = itemDialog.getLastMessageDateSent();
        lastMessageUserId = itemDialog.getLastMessageUserId();
        name = itemDialog.getName();
        photo = itemDialog.getPhoto();
        occupantsIdsList = new RealmList<>();
        for (Integer i : itemDialog.getOccupantsIdsList()) {
            occupantsIdsList.add(new RealmInteger(i));
        }
        type = itemDialog.getType();
        unreadMessagesCount = itemDialog.getUnreadMessagesCount();
        xmppRoomJid = itemDialog.getXmppRoomJid();

    }

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

    public RealmList<RealmInteger> getOccupantsIdsList() {
        return occupantsIdsList;
    }

    public void setOccupantsIdsList(RealmList<RealmInteger> occupantsIdsList) {
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
