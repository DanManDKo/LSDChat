package com.example.lsdchat.ui;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.lsdchat.App;
import com.example.lsdchat.util.ErrorsCode;

import retrofit2.adapter.rxjava.HttpException;

public class BaseActivity extends AppCompatActivity {


    public void dialogError(String throwable) {


        new AlertDialog.Builder(this)
                .setMessage(throwable)
                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                .setCancelable(false)
                .create().show();
    }

    public void dialogError(Throwable throwable) {
        String title = "Error " + String.valueOf(((HttpException) throwable).code());
        String message = ErrorsCode.getErrorMessage(App.getContext(), throwable);

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                .setCancelable(false)
                .create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
