package com.maishuo.tingshuohenhaowan.main.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * author ：Seven
 * date : 5/24/21
 * description :适用于首页与drawerLayout滑动冲突的ViewPager
 */
public class CustomViewPager extends ViewPager {

    private float startX, startY;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                startX = ev.getX();
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //来到新的坐标
                float endX = ev.getX();
                float endY = ev.getY();
                //计算偏移量
                float distanceX = endX - startX;
                float distanceY = endY - startY;
                //判断滑动方向
                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    //水平方向滑动
                    //当滑动到ViewPager的第0个页面，并且是从左到右滑动
                    if (getCurrentItem() == 0 && distanceX > 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    //当滑动到ViewPager的最后一个页面，并且是从右到左滑动
                    else if ((getCurrentItem() == (getAdapter().getCount() - 1)) && distanceX < 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    //其他,中间部分
                    else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                } else {
                    //竖直方向滑动
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
