package com.maishuo.tingshuohenhaowan.gift.view;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.maishuo.tingshuohenhaowan.widget.GiftDialogItemView;

import java.util.List;

/**
 * author: yh
 * date: 2021/1/29 13:25
 */
class GiftPageAdapter extends PagerAdapter {
    private final List<GiftDialogItemView> mData;

    public GiftPageAdapter (List<GiftDialogItemView> list) {
        mData = list;
    }

    @Override
    public int getCount () {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public boolean isViewFromObject (View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem (ViewGroup container, int pagePosition) {
        GiftDialogItemView view = mData.get(pagePosition);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem (ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
