package com.maishuo.tingshuohenhaowan.widget.barrage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.widget.AppCompatTextView;

import com.maishuo.tingshuohenhaowan.widget.barrage.utils.DimensionUtil;
import com.maishuo.tingshuohenhaowan.widget.barrage.utils.BarrageScreenUtil;

/**
 * author ：Seven
 * date : 2021/6/3
 * description : 自定义弹幕View
 */
public class BarrageView extends AppCompatTextView {

    private BarrageModel barrageModel;
    private int          mDuration;
    private long         mCurrentDuration;

    private OnExitListener listener;

    //因为BarrageModel是单例，变化的属性要重新定义，不能用getBarrageModel获取
    private String userId;
    private String userAvatar;
    private String textColor;
    private int    textSize;

    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public String getUserAvatar () {
        return userAvatar;
    }

    public void setUserAvatar (String userAva) {
        this.userAvatar = userAva;
    }

    public void setListener (OnExitListener listener) {
        this.listener = listener;
    }

    public int getTextColor () {
        return Color.parseColor(textColor);

    }

    public void setTextColor (String textColor) {
        setTextColor(Color.parseColor(barrageModel.color));
        this.textColor = textColor;
    }

    public int getTextSizes () {
        return textSize == 0 ? barrageModel.size : textSize;
    }

    public void setTextSizes (int textSize) {
        setTextSize(textSize);
        this.textSize = textSize;
    }

    /**
     * 弹幕离场后的监听
     */
    public interface OnExitListener {
        void onExit (BarrageView view);
    }

    public BarrageView (Context context) {
        super(context);
    }

    public BarrageView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarrageView (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 获取弹幕样式
     */
    public BarrageModel getBarrageModel () {
        return barrageModel;
    }

    /**
     * 获取文字的长度
     */
    public int getTextLength () {
        return (int) getPaint().measureText(getText().toString()) + DimensionUtil.dpToPx(getContext(), 28);
    }

    /**
     * 获取剩余移动时间
     */
    public int getCurrentDuration () {
        return (int) mCurrentDuration;
    }

    /**
     * 获取移动时间
     */
    public int getDuration () {
        return mDuration;
    }

    /**
     * 设置弹幕内容
     */
    public void setBarrageModel (BarrageModel barrageModel) {
        this.barrageModel = barrageModel;
        switch (barrageModel.mode) {
            case top:
            case bottom:
                setGravity(Gravity.CENTER);
                break;
            case scroll:
            case scrollRandom:
            default:
                setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                break;
        }
    }

    /**
     * 显示弹幕
     */
    public void show (final ViewGroup parent, int duration) {
        mDuration = duration;
        switch (barrageModel.mode) {
            case top:
            case bottom:
                showFixedBarrage(parent);
                break;
            case scroll:
            case scrollRandom:
            default:
                start(parent, duration);
                break;
        }
    }

    /**
     * 上下弹幕
     */
    private void showFixedBarrage (ViewGroup parent) {
        setGravity(Gravity.CENTER);
        parent.addView(this);
    }

    /**
     * 滚动弹幕
     */
    private ObjectAnimator objectAnimator;

    private void start (ViewGroup parent, int duration) {
        int screenWidth = BarrageScreenUtil.getScreenWidth();
        int textLength  = getTextLength();
        parent.addView(this);
        objectAnimator = ObjectAnimator.ofFloat(this, "translationX", screenWidth, -textLength);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new LinearInterpolator());//动画时间线性渐变
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd (Animator animation) {
                super.onAnimationEnd(animation);
                if (listener != null) {
                    listener.onExit(BarrageView.this);
                }
                parent.removeView(BarrageView.this);
            }
        });
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate (ValueAnimator valueAnimator) {
                mCurrentDuration = valueAnimator.getDuration() - valueAnimator.getCurrentPlayTime();
            }
        });
        objectAnimator.start();
    }

    /**
     * 暂停
     */
    public void pause () {
        if (objectAnimator != null) {
            objectAnimator.pause();
        }
    }

    /**
     * 继续
     */
    public void resume () {
        if (objectAnimator != null && objectAnimator.isPaused()) {
            objectAnimator.resume();
        }
    }

    /**
     * 上下滑释放内存
     */
    public void release () {
        if (objectAnimator != null) {
            objectAnimator.cancel();
            objectAnimator = null;
        }
        listener = null;
    }

}
