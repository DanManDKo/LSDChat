package com.example.lsdchat.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.lsdchat.R;
import com.example.lsdchat.manager.SharedPreferencesManager;


public class MainActivity extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferencesManager = new SharedPreferencesManager(this);

        token = sharedPreferencesManager.getToken();
        Log.e("MAIN TOKEB", token);




    }
}
