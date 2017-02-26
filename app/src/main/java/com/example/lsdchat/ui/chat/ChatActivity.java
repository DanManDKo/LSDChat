package com.example.lsdchat.ui.chat;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.lsdchat.R;
import com.example.lsdchat.ui.chat.pager.DialogFragment;
import com.example.lsdchat.ui.chat.pager.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements ChatContract.View {
    private ChatContract.Presenter mPresenter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private int mUserId;
    private List<Fragment> mFragmentsList;
    private List<String> mTitleList;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        mPresenter = new ChatPresenter(this);
        mUserId = mPresenter.getUserId();
        mFragmentsList = new ArrayList<>();
        mTitleList = new ArrayList<>();
        fillFragmentList(mFragmentsList);
        fillTitles(mTitleList);
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mFragmentsList, mTitleList));
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void fillTitles(List<String> titleList) {
        titleList.add(getString(R.string.public_chat));
        titleList.add(getString(R.string.private_chat));
    }

    private void fillFragmentList(List<Fragment> list) {
        mFragmentsList.add(DialogFragment.newInstance(DialogFragment.GROUP, mUserId));
        mFragmentsList.add(DialogFragment.newInstance(DialogFragment.PRIVATE, mUserId));
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        mPresenter = null;
    }
}
