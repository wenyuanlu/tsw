package com.maishuo.tingshuohenhaowan.widget.indicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;

/**
 * author: yh
 * date: 2021/2/7 09:51
 * description: 横向渐变色的指示器
 */
public class HXLinePagerIndicator extends LinePagerIndicatorEx {
    private final int[] mGradientColors;

    public HXLinePagerIndicator (Context context) {
        this(context, new int[]{0xFF4F48FF, 0xFFFF42A1});
    }

    public HXLinePagerIndicator (Context context, int[] gradientColors) {
        super(context);
        this.mGradientColors = gradientColors;
    }

    @Override
    protected void onDraw (Canvas canvas) {
        @SuppressLint("DrawAllocation")
        LinearGradient lg = new LinearGradient(
                getLineRect().left,
                getLineRect().top,
                getLineRect().right,
                getLineRect().bottom,
                mGradientColors,
                null,
                LinearGradient.TileMode.CLAMP
        );
        getPaint().setShader(lg);
        canvas.drawRoundRect(getLineRect(), getRoundRadius(), getRoundRadius(), getPaint());
    }
}
