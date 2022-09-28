package com.maishuo.tingshuohenhaowan.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.youth.banner.Banner;

/**
 * author : xpSun
 * date : 8/25/21
 * description :
 */
public class CustomBannerView extends Banner {

    private boolean isDisableSlideGroup;

    public void setDisableSlideGroup (boolean disableSlideGroup) {
        isDisableSlideGroup = disableSlideGroup;
    }

    public CustomBannerView (Context context) {
        super(context);
    }

    public CustomBannerView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomBannerView (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent (MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return isDisableSlideGroup;
    }

    @Override
    public boolean onInterceptHoverEvent (MotionEvent event) {
        super.onInterceptHoverEvent(event);
        return isDisableSlideGroup;
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        super.onTouchEvent(event);
        return isDisableSlideGroup;
    }
}
