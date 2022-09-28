package com.maishuo.tingshuohenhaowan.widget.barrage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.maishuo.tingshuohenhaowan.widget.barrage.utils.BarragePopupUtil;
import com.maishuo.tingshuohenhaowan.widget.barrage.utils.BarrageScreenUtil;
import com.qichuang.commonlibs.utils.LogUtils;

import java.lang.ref.WeakReference;


/**
 * author ：Seven
 * date : 2021/6/3
 * description : 弹幕管理类
 */
@SuppressWarnings("unused")
public class BarrageManager {
    private static final String TAG                   = BarrageManager.class.getSimpleName();
    private static final int    RESULT_OK             = 0;
    private static final int    RESULT_NULL_ROOT_VIEW = 1;
    private static final int    RESULT_FULL_POOL      = 2;
    private static final int    TOO_MANY_BARRAGE      = 2;

    private static BarrageManager sInstance;
    private        Context        context;
    private       boolean                      isHandClick;//是否是手动暂停

    public void setHandClick (boolean handClick) {
        isHandClick = handClick;
    }

    private BarrageCallBack barrageCallBack;

    public interface BarrageCallBack {
        void clickBarrageView ();
        void clickBarrageHeadView (String userId);
    }

    public BarrageCallBack getBarrageCallBack () {
        return barrageCallBack;
    }

    public void setBarrageCallBack (BarrageCallBack barrageCallBack) {
        this.barrageCallBack = barrageCallBack;
    }

    /**
     * 弹幕容器
     */
    public  WeakReference<FrameLayout> mBarrageContainer;
    /**
     * 弹幕池
     */
    private Pool<BarrageView>          mBarrageViewPool;

    private Config mConfig;

    private BarrageLayoutCalculator barrageLayoutCalculator;

    public BarrageManager () {
    }

    public static BarrageManager getInstance () {
        if (sInstance == null) {
            sInstance = new BarrageManager();
        }
        return sInstance;
    }

    /**
     * 初始化。在使用之前必须调用该方法。
     */
    public void init (Context context, FrameLayout container) {
        this.context = context;

        if (mBarrageViewPool == null) {
            mBarrageViewPool = new BarrageViewPool(
                    60000, // 缓存存活时间：60秒
                    100, // 最大弹幕数：100
                    () -> BarrageViewFactory.createBarrageView(context, container));
        } else {
            mBarrageViewPool.release();
            releaseAnimator();
        }

        setBarrageContainer(container);

        if (mConfig == null) {
            BarrageScreenUtil.init(context);
            mConfig = new Config();
        }

        if (barrageLayoutCalculator == null) {
            barrageLayoutCalculator = BarrageLayoutCalculator.getInstance(this);
        }
    }

    public Config getConfig () {
        if (mConfig == null) {
            mConfig = new Config();
        }
        return mConfig;
    }

    public void setBarrageViewPool (Pool<BarrageView> pool) {
        if (mBarrageViewPool != null) {
            mBarrageViewPool.release();
        }
        mBarrageViewPool = pool;
    }

    /**
     * 设置允许同时出现最多的弹幕数，如果屏幕上显示的弹幕数超过该数量，那么新出现的弹幕将被丢弃，
     * 直到有旧的弹幕消失。
     *
     * @param max 同时出现的最多弹幕数，-1无限制
     */
    public void setMaxBarrageSize (int max) {
        if (mBarrageViewPool == null) {
            return;
        }
        mBarrageViewPool.setMaxSize(max);
    }

    /**
     * 设置弹幕的容器，所有的弹幕都在这里面显示
     */
    public void setBarrageContainer (final FrameLayout root) {
        if (root == null) {
            throw new NullPointerException("BarrageView container cannot be null!");
        }
        mBarrageContainer = new WeakReference<>(root);
    }

    /**
     * 发送一条弹幕
     */
    public int send (BarrageModel barrageModel) {
        if (mBarrageViewPool == null) {
            throw new NullPointerException("BarrageView view pool is null. Did you call init() first?");
        }

        BarrageView view = mBarrageViewPool.get();

        if (view == null) {
            LogUtils.LOGE(TAG + "show: Too many BarrageView, discard");
            return RESULT_FULL_POOL;
        }
        if (mBarrageContainer == null || mBarrageContainer.get() == null) {
            LogUtils.LOGE(TAG + "show: Root view is null.");
            return RESULT_NULL_ROOT_VIEW;
        }

        // 配置信息
        view.setBarrageModel(barrageModel);
        // 文字内容
        view.setText(barrageModel.text);
        //设置用户信息，用于点击
        view.setUserId(barrageModel.userId);
        view.setUserAvatar(barrageModel.avatar);
        // 字体颜色
        view.setTextColor(barrageModel.color);
        // 字体背景
        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        drawable.setStroke(1, Color.parseColor(barrageModel.color));
        view.setBackground(drawable);


        // 计算弹幕距离顶部的位置
        int marginTop = barrageLayoutCalculator.getMarginTop(view);
        if (marginTop == -1) return RESULT_OK;
        FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) view.getLayoutParams();
        if (p == null) {
            p = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        p.topMargin = marginTop;
        view.setLayoutParams(p);
        view.setMinHeight((int) (getConfig().getLineHeight() * 1.25));
        view.show(mBarrageContainer.get(), getDisplayDuration(barrageModel));
        view.setOnClickListener(a -> {
            view.pause();
            view.setVisibility(View.GONE);
            barrageCallBack.clickBarrageView();
            BarragePopupUtil.showBarrageDialog((Activity) context, view,
                    new BarragePopupUtil.OnBarragePopupCallBack() {
                        @Override
                        public void onDismiss () {
                            view.setVisibility(View.VISIBLE);
                            if(!isHandClick){//非手动暂停
                                view.resume();
                            }
                        }

                        @Override
                        public void onClick () {
                            barrageCallBack.clickBarrageHeadView(view.getUserId());
                        }
                    });
        });
        return RESULT_OK;
    }

    /**
     * 暂停弹幕
     */
    public void pause () {
        for (int i = 0; i < mBarrageContainer.get().getChildCount(); i++) {
            BarrageView view = (BarrageView) mBarrageContainer.get().getChildAt(i);
            view.pause();
        }
    }

    /**
     * 继续弹幕
     */
    public void resume () {
        for (int i = 0; i < mBarrageContainer.get().getChildCount(); i++) {
            BarrageView view = (BarrageView) mBarrageContainer.get().getChildAt(i);
            view.resume();
        }
    }

    /**
     * 释放内存
     */
    public void releaseAnimator () {
        if (mBarrageContainer != null) {
            for (int i = 0; i < mBarrageContainer.get().getChildCount(); i++) {
                BarrageView view = (BarrageView) mBarrageContainer.get().getChildAt(i);
                view.release();
            }
            mBarrageContainer.get().removeAllViews();
        }
    }

    /**
     * 关弹幕Popup
     */
    public void releasePopup () {
        BarragePopupUtil.doDismiss();
    }


    /**
     * @return 返回这个弹幕显示时长
     */
    int getDisplayDuration (BarrageModel barrageModel) {
        Config config = getConfig();
        int    duration;
        switch (barrageModel.mode) {
            case top:
                duration = config.getDurationTop();
                break;
            case bottom:
                duration = config.getDurationBottom();
                break;
            case scroll:
            case scrollRandom:
            default:
                duration = config.getDurationScroll();
                break;
        }
        return duration;
    }

    /**
     * 一些配置
     */
    public static class Config {

        /**
         * 行高，单位px
         */
        private int lineHeight;

        /**
         * 滚动弹幕显示时长
         */
        private int durationScroll;
        /**
         * 顶部弹幕显示时长
         */
        private int durationTop;
        /**
         * 底部弹幕的显示时长
         */
        private int durationBottom;

        /**
         * 滚动弹幕的最大行数
         */
        private int maxScrollLine;

        public int getLineHeight () {
            if (lineHeight == 0) {
                lineHeight = BarrageScreenUtil.autoSize(60);
            }
            return lineHeight;
        }

        public void setLineHeight (int lineHeight) {
            this.lineHeight = lineHeight;
        }

        public int getMaxScrollLine () {
            return maxScrollLine;
        }

        public int getDurationScroll () {
            if (durationScroll == 0) {
                durationScroll = 8000;
            }
            return durationScroll;
        }

        public void setDurationScroll (int durationScroll) {
            this.durationScroll = durationScroll;
        }

        public int getDurationTop () {
            if (durationTop == 0) {
                durationTop = 5000;
            }
            return durationTop;
        }

        public void setDurationTop (int durationTop) {
            this.durationTop = durationTop;
        }

        public int getDurationBottom () {
            if (durationBottom == 0) {
                durationBottom = 5000;
            }
            return durationBottom;
        }

        public void setDurationBottom (int durationBottom) {
            this.durationBottom = durationBottom;
        }

        public int getMaxBarrageLine () {
            if (maxScrollLine == 0) {
                maxScrollLine = 12;
            }
            return maxScrollLine;
        }

        public void setMaxScrollLine (int maxScrollLine) {
            this.maxScrollLine = maxScrollLine;
        }
    }

}
