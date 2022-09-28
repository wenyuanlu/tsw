package com.maishuo.tingshuohenhaowan.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * author: yh
 * date: 2021/2/22 17:23
 * description: TODO:
 */
public class CustomScrollView extends ScrollView {

    private OnScrollListener onScrollListener;

    public CustomScrollView (Context context) {
        super(context);
    }

    public CustomScrollView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int computeVerticalScrollRange () {
        return super.computeVerticalScrollRange();
    }

    @Override
    protected void onScrollChanged (int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) {
            onScrollListener.onScroll(t);
        }
    }

    /**
     * 接口对外公开
     *
     * @param onScrollListener
     */
    public void setOnScrollListener (OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**
     * 滚动的回调接口
     *
     * @author xiaanming
     */
    public interface OnScrollListener {
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         *
         * @param scrollY 、
         */
        void onScroll (int scrollY);
    }
}

