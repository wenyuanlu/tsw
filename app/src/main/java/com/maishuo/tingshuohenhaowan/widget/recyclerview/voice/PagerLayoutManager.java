package com.maishuo.tingshuohenhaowan.widget.recyclerview.voice;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author ：Seven
 * date : 5/25/21
 * description :音视频播放的 LayoutManager
 */
public class PagerLayoutManager extends LinearLayoutManager implements RecyclerView.OnChildAttachStateChangeListener {
    private CustomPagerSnapHelper      mPagerSnapHelper;
    private OnPageChangeListener mOnPageChangeListener;
    private int                  currentPosition;
    private boolean              haveSelect;

    PagerLayoutManager(Context context) {
        super(context);
        mPagerSnapHelper = new CustomPagerSnapHelper();
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        view.addOnChildAttachStateChangeListener(this);
        mPagerSnapHelper.attachToRecyclerView(view);
        super.onAttachedToWindow(view);
    }

    @Override
    public void onChildViewAttachedToWindow(@NonNull View view) {
        if (!haveSelect) {
            haveSelect = true;
            currentPosition = getPosition(view);
            mOnPageChangeListener.onPageSelected(currentPosition, view);
        }
    }

    @Override
    public void onChildViewDetachedFromWindow(@NonNull View view) {
    }

    @Override
    public void onScrollStateChanged(int state) {
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            View view = mPagerSnapHelper.findSnapView(this);
            if (view != null && mOnPageChangeListener != null) {
                int position = getPosition(view);
                if (currentPosition != position) {
                    currentPosition = position;
                    mOnPageChangeListener.onPageSelected(currentPosition, view);
                }
            }
        }
        super.onScrollStateChanged(state);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    void setOnPageChangeListener(OnPageChangeListener mOnPageChangeListener) {
        this.mOnPageChangeListener = mOnPageChangeListener;
    }

    public void setHaveSelect(boolean haveSelect) {
        this.haveSelect = haveSelect;
    }
}