package com.maishuo.tingshuohenhaowan.widget.popmenu;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;

import androidx.annotation.ColorInt;

import com.maishuo.tingshuohenhaowan.utils.Utils;

/**
 * author ：yh
 * date : 2021/1/26 09:55
 * description :三角形箭头图片
 */
final class ArrowDrawable extends ColorDrawable {

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int   mBackgroundColor;
    private final int   mGravity;

    private Path mPath;

    ArrowDrawable (@ColorInt int foregroundColor, int gravity) {
        mGravity = Utils.gravityToArrowDirection(gravity);
        mBackgroundColor = Color.TRANSPARENT;

        mPaint.setColor(foregroundColor);
    }

    @Override
    protected void onBoundsChange (Rect bounds) {
        super.onBoundsChange(bounds);
        updatePath(bounds);
    }

    private synchronized void updatePath (Rect bounds) {
        mPath = new Path();

        switch (mGravity) {
            case Gravity.START:
                mPath.moveTo(bounds.width(), bounds.height());
                mPath.lineTo(0, bounds.height() / 2);
                mPath.lineTo(bounds.width(), 0);
                mPath.lineTo(bounds.width(), bounds.height());
                break;
            case Gravity.TOP:
                mPath.moveTo(0, bounds.height());
                mPath.lineTo(bounds.width() / 2, 0);
                mPath.lineTo(bounds.width(), bounds.height());
                mPath.lineTo(0, bounds.height());
                break;
            case Gravity.END:
                mPath.moveTo(0, 0);
                mPath.lineTo(bounds.width(), bounds.height() / 2);
                mPath.lineTo(0, bounds.height());
                mPath.lineTo(0, 0);
                break;
            case Gravity.BOTTOM:
                mPath.moveTo(0, 0);
                mPath.lineTo(bounds.width() / 2, bounds.height());
                mPath.lineTo(bounds.width(), 0);
                mPath.lineTo(0, 0);
                break;
        }

        mPath.close();
    }

    @Override
    public void draw (Canvas canvas) {
        canvas.drawColor(mBackgroundColor);
        if (mPath == null) {
            updatePath(getBounds());
        }
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void setAlpha (int alpha) {
        mPaint.setAlpha(alpha);
    }

    public void setColor (@ColorInt int color) {
        mPaint.setColor(color);
    }

    @Override
    public void setColorFilter (ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity () {
        if (mPaint.getColorFilter() != null) {
            return PixelFormat.TRANSLUCENT;
        }

        switch (mPaint.getColor() >>> 24) {
            case 255:
                return PixelFormat.OPAQUE;
            case 0:
                return PixelFormat.TRANSPARENT;
        }
        return PixelFormat.TRANSLUCENT;
    }
}
