package com.example.lsdchat.manager.model;

import io.realm.RealmObject;

/**
 * Created by User on 05.03.2017.
 */

public class RealmInteger extends RealmObject {
    public RealmInteger(){}
    public RealmInteger(Integer value) {
        this.value = value;
    }

    private Integer value;

    public Integer getValue() {
        return value;
    }
}
