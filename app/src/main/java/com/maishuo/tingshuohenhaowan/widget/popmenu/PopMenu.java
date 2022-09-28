package com.maishuo.tingshuohenhaowan.widget.popmenu;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.ColorInt;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.utils.Utils;
import com.qichuang.commonlibs.utils.LoggerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：yh
 * date : 2021/1/26 09:54
 * description : PopMenu
 */
public final class PopMenu {

    private final int         mScreenWidthPixels;
    private final int         mScreenHeightPixels;
    private final PopupWindow mPopupWindow;
    private       int         mGravity = Gravity.TOP;

    private LinearLayout mContentView;
    private ImageView    mArrowView;//箭头
    private View         mAnchorView;//展示目标的view

    private RecyclerView        mRecyclerView;
    private PopMenuAdapter      mAdapter;
    private List<ActionItem>    mData = new ArrayList<>();
    private OnItemClickListener mItemClickListener;
    private int                 mBackgroundColor;

    public interface OnItemClickListener {
        void onItemClickListener (ActionItem item, int position);
    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }

    public void addData (List<ActionItem> data) {
        mData.addAll(data);
    }

    public void addData (ActionItem item) {
        mData.add(item);
    }

    public void clearData () {
        mData.clear();
    }

    private PopMenu (Builder builder) {
        DisplayMetrics dm = builder.mContext.getResources().getDisplayMetrics();
        mScreenHeightPixels = dm.heightPixels;
        mScreenWidthPixels = dm.widthPixels;
        mPopupWindow = new PopupWindow(builder.mContext);
        mPopupWindow.setBackgroundDrawable(null);
        mPopupWindow.setClippingEnabled(false);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setContentView(getContentView(builder));
        mPopupWindow.setFocusable(true);//让popupwindow获取焦点
        mPopupWindow.setOutsideTouchable(builder.isCancelable);
    }

    private View getContentView (final Builder builder) {
        GradientDrawable drawable = new GradientDrawable();
        mBackgroundColor = builder.mBackgroundColor;
        drawable.setColor(mBackgroundColor);
        drawable.setCornerRadius(builder.mCornerRadius);
        mData.addAll(builder.mData);

        mRecyclerView = new RecyclerView(builder.mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(builder.mContext));
        mAdapter = new PopMenuAdapter(mData);
        mAdapter.setBuild(builder);
        mAdapter.setItemClick((item, position) -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClickListener(item, position);
            }
            dismiss();
        });

        mRecyclerView.setAdapter(mAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRecyclerView.setBackground(drawable);
        } else {
            //noinspection deprecation
            mRecyclerView.setBackgroundDrawable(drawable);
        }

        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        textViewParams.gravity = Gravity.CENTER;
        int padding = (int) builder.mPadding;
        mRecyclerView.setPadding(padding, padding, padding, padding);
        mRecyclerView.setLayoutParams(textViewParams);

        mArrowView = new ImageView(builder.mContext);

        LinearLayout.LayoutParams arrowLayoutParams = new LinearLayout.LayoutParams((int) builder.mArrowWidth, (int) builder.mArrowHeight, 0);

        arrowLayoutParams.gravity = Gravity.CENTER;
        mArrowView.setLayoutParams(arrowLayoutParams);

        mContentView = new LinearLayout(builder.mContext);
        mContentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mContentView.setOrientation(LinearLayout.VERTICAL);

        mContentView.addView(mArrowView);
        mContentView.addView(mRecyclerView);//为了计算高度
        return mContentView;
    }

    public boolean isShowing () {
        return mPopupWindow.isShowing();
    }

    public void show (final View anchorView) {
        if (!isShowing()) {
            mAnchorView = anchorView;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {//Android 10的OnGlobalLayoutListener有延迟,需要单独处理,不使用addOnGlobalLayoutListener

                //计算mGravity
                RectF anchorRect = Utils.calculateRectOnScreen(mAnchorView);
                if (anchorRect.centerY() <= mScreenHeightPixels >> 1) {
                    mGravity = Gravity.BOTTOM;
                } else {
                    mGravity = Gravity.TOP;
                }

                //根据mGravity设置箭头和内容的位置
                mContentView.removeAllViews();
                if (mGravity == Gravity.TOP) {
                    mContentView.addView(mRecyclerView);
                    mContentView.addView(mArrowView);
                } else {
                    mContentView.addView(mArrowView);
                    mContentView.addView(mRecyclerView);
                }
                mArrowView.setImageDrawable(new ArrowDrawable(Color.parseColor("#2E2C36"), mGravity));//设置三角的颜色等

                //计算内容的宽高等
                mContentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                PointF location = calculateLocation();

                int atX = (int) location.x - mContentView.getMeasuredWidth() / 2;
                int atY = (int) location.y;
                if (mGravity == Gravity.TOP) {
                    atY = atY - mContentView.getMeasuredHeight();
                }

                int finalAtX = atX;
                int finalAtY = atY;
                anchorView.post(new Runnable() {
                    @Override
                    public void run () {
                        mPopupWindow.showAsDropDown(anchorView);
                        mPopupWindow.update(finalAtX, finalAtY, mPopupWindow.getWidth(), mPopupWindow.getHeight());
                    }
                });
            } else {
                mContentView.getViewTreeObserver().addOnGlobalLayoutListener(mLocationLayoutListener);
                anchorView.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
                anchorView.post(new Runnable() {
                    @Override
                    public void run () {
                        mPopupWindow.showAsDropDown(anchorView);
                    }
                });
            }

        }
    }

    public void dismiss () {
        mPopupWindow.dismiss();
    }

    /**
     * LocationLayoutListener监听
     */
    private final ViewTreeObserver.OnGlobalLayoutListener mLocationLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout () {
            Utils.removeOnGlobalLayoutListener(mContentView, this);
            setLocationLayout();
        }
    };

    /**
     * LocationLayoutListener 监听设置
     */
    private void setLocationLayout () {
        RectF anchorRect = Utils.calculateRectOnScreen(mAnchorView);
        if (anchorRect.centerY() <= mScreenHeightPixels >> 1) {
            mGravity = Gravity.BOTTOM;
        } else {
            mGravity = Gravity.TOP;
        }
        mContentView.removeAllViews();
        if (mGravity == Gravity.TOP) {
            mContentView.addView(mRecyclerView);
            mContentView.addView(mArrowView);
        } else {
            mContentView.addView(mArrowView);
            mContentView.addView(mRecyclerView);
        }
        PointF location = calculateLocation();
        mArrowView.setImageDrawable(new ArrowDrawable(Color.parseColor("#2E2C36"), mGravity));//设置三角的颜色等
        mContentView.getViewTreeObserver().addOnGlobalLayoutListener(mArrowLayoutListener);
        mPopupWindow.setClippingEnabled(true);
        mPopupWindow.update((int) location.x, (int) location.y, mPopupWindow.getWidth(), mPopupWindow.getHeight());
    }

    /**
     * ArrowLayoutListener监听
     */
    private final ViewTreeObserver.OnGlobalLayoutListener mArrowLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout () {
            Utils.removeOnGlobalLayoutListener(mContentView, this);
            setArrowLayout();
        }
    };

    /**
     * ArrowLayoutListener 监听设置
     */
    private void setArrowLayout () {
        RectF anchorRect      = Utils.calculateRectOnScreen(mAnchorView);
        RectF contentViewRect = Utils.calculateRectOnScreen(mContentView);
        float x, y;
        if (mGravity == Gravity.BOTTOM || mGravity == Gravity.TOP) {
            x = mContentView.getPaddingLeft() + Utils.dpToPx(2);
            float centerX = (contentViewRect.width() / 2f) - (mArrowView.getWidth() / 2f);
            float newX    = centerX - (contentViewRect.centerX() - anchorRect.centerX());
            if (newX > x) {
                if (newX + mArrowView.getWidth() + x > contentViewRect.width()) {
                    x = contentViewRect.width() - mArrowView.getWidth() - x;
                } else {
                    x = newX;
                }
            }
            y = mArrowView.getTop();
            y = y + (mGravity == Gravity.TOP ? -1 : +1);
        } else {
            y = mContentView.getPaddingTop() + Utils.dpToPx(2);
            float centerY = (contentViewRect.height() / 2f) - (mArrowView.getHeight() / 2f);
            float newY    = centerY - (contentViewRect.centerY() - anchorRect.centerY());
            if (newY > y) {
                if (newY + mArrowView.getHeight() + y > contentViewRect.height()) {
                    y = contentViewRect.height() - mArrowView.getHeight() - y;
                } else {
                    y = newY;
                }
            }
            x = mArrowView.getLeft();
            x = x + (mGravity == Gravity.START ? -1 : +1);
        }
        mArrowView.setX(x);
        mArrowView.setY(y);
        LoggerUtils.INSTANCE.e("弹窗动画X=" + x + "|Y=" + y);

        startAnim(x, y);
    }

    /**
     * 计算位置
     */
    private PointF calculateLocation () {
        PointF location = new PointF();

        final RectF  anchorRect   = Utils.calculateRectInWindow(mAnchorView);
        final PointF anchorCenter = new PointF(anchorRect.centerX(), anchorRect.centerY());
        switch (mGravity) {
            case Gravity.TOP:
                location.x = anchorCenter.x - mContentView.getWidth() / 2f;
                location.y = anchorRect.top - mContentView.getHeight();
                break;
            case Gravity.BOTTOM:
                location.x = anchorCenter.x - mContentView.getWidth() / 2f;
                location.y = anchorRect.bottom;
                break;
            default:
                location.x = anchorCenter.x - mContentView.getWidth() / 2f;
                break;
        }
        float marginX = Utils.dpToPx(2);
        if (mScreenWidthPixels - location.x - mContentView.getWidth() < marginX) {
            location.x = mScreenWidthPixels - mContentView.getWidth() - marginX;
        } else if (location.x < marginX) {
            location.x = marginX;
        }
        LoggerUtils.INSTANCE.e("弹窗位置X=" + location.x + "|Y=" + location.x);
        return location;
    }

    /**
     * 开始动画
     *
     * @param x
     * @param y
     */
    private void startAnim (float x, float y) {
        AnimatorSet    animatorSet = new AnimatorSet();//组合动画
        ObjectAnimator scaleX      = ObjectAnimator.ofFloat(mContentView, "scaleX", 0.2f, 1f);
        ObjectAnimator scaleY      = ObjectAnimator.ofFloat(mContentView, "scaleY", 0.2f, 1f);
        ObjectAnimator alpha       = ObjectAnimator.ofFloat(mContentView, "alpha", 0.5f, 1f);
        mContentView.setPivotX(x + mArrowView.getWidth() / 2);
        if (mGravity == Gravity.TOP) {
            mContentView.setPivotY(y + mArrowView.getHeight());
        } else {
            mContentView.setPivotY(y);
        }
        animatorSet.setDuration(100);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(scaleX).with(scaleY).with(alpha);//三个动画同时开始
        animatorSet.start();
    }

    private final View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow (View v) {

        }

        @Override
        public void onViewDetachedFromWindow (View v) {
            dismiss();
        }
    };

    /**
     * 参数设置
     */
    public static final class Builder {

        private int     mBackgroundColor = Color.parseColor("#2E2C36");
        public  int     mTextColor       = Color.LTGRAY;
        private boolean isCancelable     = true;

        private float mCornerRadius = 0;
        private float mPadding      = 0;
        private float mArrowHeight  = 0;
        private float mArrowWidth   = 0;

        private Context          mContext;
        private List<ActionItem> mData        = new ArrayList<>();
        private int              mLineColor   = Color.GRAY;
        private int              mLineSize    = 1;
        private boolean          mIsShowImage = true;

        public Builder (@NonNull Context context) {
            mContext = context;
        }

        public Builder setCancelable (boolean cancelable) {
            isCancelable = cancelable;
            return this;
        }

        public Builder setBackgroundColor (@ColorInt int color) {
            mBackgroundColor = color;
            return this;
        }

        public Builder setTextColor (@ColorInt int color) {
            mTextColor = color;
            return this;
        }

        public Builder setLineColor (@ColorInt int color) {
            mLineColor = color;
            return this;
        }

        public Builder setLineSize (@Size int size) {
            mLineSize = size;
            return this;
        }

        public Builder setShowImage (boolean isShowImage) {
            mIsShowImage = isShowImage;
            return this;
        }

        public boolean getShowImage () {
            return mIsShowImage;
        }

        public Builder setCornerRadius (@DimenRes int resId) {
            return setCornerRadius(mContext.getResources().getDimension(resId));
        }

        public Builder setCornerRadius (float radius) {
            mCornerRadius = radius;
            return this;
        }

        public float getPadding () {
            return mPadding;
        }

        public Builder setPadding (float padding) {
            mPadding = padding;
            return this;
        }

        public Builder setArrowHeight (@DimenRes int resId) {
            return setArrowHeight(mContext.getResources().getDimension(resId));
        }

        public Builder setArrowHeight (float height) {
            mArrowHeight = height;
            return this;
        }

        public Builder setArrowWidth (@DimenRes int resId) {
            return setArrowWidth(mContext.getResources().getDimension(resId));
        }

        public Builder setArrowWidth (float width) {
            mArrowWidth = width;
            return this;
        }

        public Builder addData (List<ActionItem> items) {
            mData.clear();
            mData.addAll(items);
            return this;
        }

        public Builder addData (ActionItem item) {
            mData.add(item);
            return this;
        }

        public PopMenu build () {
            if (mArrowHeight == 0) {
                mArrowHeight = mContext.getResources().getDimension(R.dimen.dp_6);
            }
            if (mArrowWidth == 0) {
                mArrowWidth = mContext.getResources().getDimension(R.dimen.dp_14);
            }
            return new PopMenu(this);
        }
    }
}
