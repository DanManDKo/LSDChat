package com.example.lsdchat.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.lsdchat.R;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.User;
import com.quickblox.users.model.QBUser;

import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private DataManager manager = new DataManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


    }
}
