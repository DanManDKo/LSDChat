package com.example.lsdchat.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.lsdchat.R;

public class ViewPagerPageFragment extends Fragment {
//    private LinearLayout rootLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_pagefragment_rootlayout, container, false);
        initView(view);

        return view;
    }

    public void initView(View view) {
//        rootLayout = (LinearLayout) view.findViewById(R.id.viewpager_rootlayout);
    }

    public static ViewPagerPageFragment newInstance() {
        Bundle args = new Bundle();

        ViewPagerPageFragment fragment = new ViewPagerPageFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
