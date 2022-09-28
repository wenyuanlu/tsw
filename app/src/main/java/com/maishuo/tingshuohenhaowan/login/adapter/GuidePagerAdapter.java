package com.maishuo.tingshuohenhaowan.login.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.maishuo.tingshuohenhaowan.R;
import com.qichuang.commonlibs.utils.GlideUtils;

/**
 * author : yun
 * time   : 2019/09/21
 * desc   : 引导页适配器
 */
public final class GuidePagerAdapter extends PagerAdapter {

    private static final int[] GUIDE_BACK = {R.mipmap.guide1, R.mipmap.guide2,
            R.mipmap.guide3};

    @Override
    public int getCount () {
        return GUIDE_BACK.length;
    }

    @Override
    public boolean isViewFromObject (@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem (@NonNull ViewGroup container, int position) {
        View rootView = LayoutInflater.from(container.getContext())
                .inflate(R.layout.view_guide_pager_layout,
                        container,
                        false);
        ImageView imageViewBack = rootView.findViewById(R.id.guide_pager_back);
        ImageView imageCenter   = rootView.findViewById(R.id.guide_pager_center);

        GlideUtils.INSTANCE.loadImage(
                container.getContext(),
                R.mipmap.guide4,
                imageViewBack
        );

        GlideUtils.INSTANCE.loadImage(
                container.getContext(),
                GUIDE_BACK[position],
                imageCenter
        );

        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem (@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}