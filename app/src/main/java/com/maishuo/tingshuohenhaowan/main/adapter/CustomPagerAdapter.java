package com.maishuo.tingshuohenhaowan.main.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class CustomPagerAdapter extends PagerAdapter {
    private ArrayList<GridView> views;

    public void setViews (ArrayList<GridView> views) {
        this.views = views;
        notifyDataSetChanged();
    }

    @Override
    public int getCount () {
        return null == views ? 0 : views.size();
    }

    @Override
    public boolean isViewFromObject (@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem (ViewGroup container, int position) {
        container.addView(views.get(position));
        return (views.get(position));
    }

    @Override
    public void destroyItem (ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }
}