package com.example.lsdchat.ui.main.fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.lsdchat.App;
import com.example.lsdchat.ui.main.MainActivity;
import com.example.lsdchat.util.ErrorInterface;
import com.example.lsdchat.util.ErrorsCode;

import retrofit2.adapter.rxjava.HttpException;


public class BaseFragment extends Fragment  implements ErrorInterface{


    public BaseFragment() {
        // Required empty public constructor
    }


    public void dialogError(String throwable) {

        new AlertDialog.Builder(getActivity())
                .setMessage(throwable)
                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                .setCancelable(false)
                .create().show();
    }

    public void dialogError(Throwable throwable) {
        String title = "Error " + String.valueOf(((HttpException) throwable).code());
        String message = ErrorsCode.getErrorMessage(App.getContext(), throwable);

        new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                .setCancelable(false)
                .create().show();
    }


    public boolean onBackPressed() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
        return false;
    }

    public void initToolbar(Toolbar mToolbar,String title) {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        }

    }

    @Override
    public void showErrorDialog(Throwable throwable) {
        dialogError(throwable);
    }
}
