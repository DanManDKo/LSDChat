package com.example.lsdchat.ui.chat.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<DialogFragment> adapterArray;
    private List<String> titleList;

    public ViewPagerAdapter(FragmentManager fm, List<DialogFragment> adapterArray, List<String> titleList) {
        super(fm);
        this.adapterArray = adapterArray;
        this.titleList = titleList;
    }




    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return adapterArray.get(position);
    }

    @Override
    public int getCount() {
        return adapterArray.size();
    }
}
