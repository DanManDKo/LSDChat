package com.example.lsdchat.model;

import io.realm.RealmObject;


public class IdsListInteger extends RealmObject {


    private Integer value;

    public IdsListInteger() {
    }

    public IdsListInteger(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
