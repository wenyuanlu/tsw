package com.maishuo.tingshuohenhaowan.main.adapter;

import android.os.Parcelable;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * author : xpSun
 * date : 2021/3/19
 * description :
 */
public class CommonViewPagerAdapter extends FragmentStatePagerAdapter {
    private SparseArray<Fragment> fragmentSparseArray;
    private SparseArray<String>   titles;

    public CommonViewPagerAdapter (@NonNull FragmentManager fm,
            SparseArray<Fragment> fragmentSparseArray,
            SparseArray<String> titles) {
        super(fm);
        this.fragmentSparseArray = fragmentSparseArray;
        this.titles = titles;
    }

    @Override
    public int getItemPosition (@NonNull Object object) {
        return POSITION_NONE;
    }

    @Nullable
    @Override
    public Parcelable saveState () {
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle (int position) {
        return null == titles ? "" : titles.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem (int position) {
        return fragmentSparseArray.get(position);
    }

    @Override
    public int getCount () {
        return null == fragmentSparseArray ? 0 : fragmentSparseArray.size();
    }
}
