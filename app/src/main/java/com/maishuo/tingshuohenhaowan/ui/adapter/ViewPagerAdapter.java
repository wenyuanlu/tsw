package com.maishuo.tingshuohenhaowan.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * author: yh
 * date: 2021/2/5 13:13
 * description:
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragments;

    public ViewPagerAdapter (FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem (int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount () {
        return mFragments == null ? 0 : mFragments.size();
    }

}
