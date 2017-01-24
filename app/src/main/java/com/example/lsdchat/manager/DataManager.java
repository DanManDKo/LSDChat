package com.example.lsdchat.manager;

import com.example.lsdchat.model.Session;
import com.example.lsdchat.model.User;

import io.realm.Realm;
import io.realm.RealmResults;

public class DataManager {
    private Realm realm;

    public DataManager() {
        realm = Realm.getDefaultInstance();
    }



    public void addToRealm(User user) {
        clearDb();
        realm.beginTransaction();
        realm.insert(user);
        realm.commitTransaction();
    }

    public RealmResults<User> readRealm() {
        return realm.where(User.class).findAll();
    }

    public void clearDb() {
        RealmResults<User> realmResults = realm.where(User.class).findAll();
        if (!realmResults.isEmpty()) {
            realm.executeTransaction(realm1 -> realm1.deleteAll());
        }
    }


//    Session management

    public void addToken(String token) {
        clearToken();
        Session session = new Session();
        session.setToken(token);
        realm.beginTransaction();
        realm.insert(session);
        realm.commitTransaction();
    }

    public String readToken() {
        String token = null;
        RealmResults<Session> results = realm.where(Session.class).findAll();
        for (Session session : results) {
            token = session.getToken();
        }
        return token;
    }

    public void clearToken() {
        RealmResults<Session> results = realm.where(Session.class).findAll();
        if (!results.isEmpty()) {
            realm.executeTransaction(realm1 -> realm1.deleteAll());
        }
    }
}
