package com.example.lsdchat.ui.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lsdchat.R;
import com.example.lsdchat.ui.chat.ChatContract;
import com.example.lsdchat.ui.chat.ChatPresenter;
import com.example.lsdchat.ui.chat.pager.DialogFragment;
import com.example.lsdchat.ui.chat.pager.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 26.02.2017.
 */

public class ChatFragment extends Fragment implements ChatContract.View {
    private ChatContract.Presenter mPresenter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private int mUserId;
    private List<Fragment> mFragmentsList;
    private List<String> mTitleList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        initView(view);
        mPresenter = new ChatPresenter(this);
        mUserId = mPresenter.getUserId();
        mFragmentsList = new ArrayList<>();
        mTitleList = new ArrayList<>();
        fillFragmentList(mFragmentsList);
        fillTitles(mTitleList);
        mViewPager.setAdapter(new ViewPagerAdapter(getActivity().getSupportFragmentManager(), mFragmentsList, mTitleList));
        mTabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    private void fillTitles(List<String> titleList) {
        titleList.add(getString(R.string.public_chat));
        titleList.add(getString(R.string.private_chat));
    }

    private void fillFragmentList(List<Fragment> list) {
        mFragmentsList.add(DialogFragment.newInstance(DialogFragment.GROUP, mUserId));
        mFragmentsList.add(DialogFragment.newInstance(DialogFragment.PRIVATE, mUserId));
    }

    private void initView(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) view.findViewById(R.id.tab);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroy();
        mPresenter = null;
    }
}
