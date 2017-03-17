package com.example.lsdchat.model;


import com.example.lsdchat.api.dialog.model.ItemDialog;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import rx.Observable;

public class RealmDialogModel extends RealmObject {

    @PrimaryKey
    private String id;
    private Integer ownerId;
    private String createdAt;
    private String updatedAt;
    private String lastMessage;
    private String lastMessageDateSent;
    private Integer lastMessageUserId;
    private String name;
    private String photo;
    private RealmList<IdsListInteger> occupantsIdsList;
    private Integer type;
    private Integer unreadMessagesCount;
    private String xmppRoomJid;

    public RealmDialogModel() {
    }

    public RealmDialogModel(ItemDialog dialog) {
        this.id = dialog.getId();
        this.ownerId = dialog.getOwnerId();
        this.createdAt = dialog.getCreatedAt();
        this.updatedAt = dialog.getUpdatedAt();
        this.lastMessage = dialog.getLastMessage();
        this.lastMessageDateSent = dialog.getLastMessageDateSent();
        this.lastMessageUserId = dialog.getLastMessageUserId();
        this.name = dialog.getName();
        this.photo = dialog.getPhoto();
//        this.occupantsIdsList = dialog.getOccupantsIdsList();
        this.occupantsIdsList = new RealmList<>();

        Observable.from(dialog.getOccupantsIdsList())
                .subscribe(integer -> this.occupantsIdsList.add(new IdsListInteger(integer)));


        this.type = dialog.getType();
        this.unreadMessagesCount = dialog.getUnreadMessagesCount();
        this.xmppRoomJid = dialog.getXmppRoomJid();
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
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

    public RealmList<IdsListInteger> getOccupantsIdsList() {
        return occupantsIdsList;
    }

    public void setOccupantsIdsList(RealmList<IdsListInteger> occupantsIdsList) {
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
