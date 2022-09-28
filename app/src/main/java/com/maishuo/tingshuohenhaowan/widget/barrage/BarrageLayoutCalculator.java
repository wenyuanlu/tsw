package com.maishuo.tingshuohenhaowan.widget.barrage;

import android.view.ViewGroup;

import com.qichuang.commonlibs.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * author ：Seven
 * date : 2021/6/3
 * description : 用于计算弹幕位置，来保证弹幕不重叠又不浪费空间
 */
class BarrageLayoutCalculator {
    public static BarrageLayoutCalculator instance;
    private       BarrageManager          mBarrageManager;
    private       List<BarrageView>       mLastBarrageViewList = new ArrayList<>();// 保存每一行最后一个弹幕消失的时间
    private       boolean[]               mTops;
    private       boolean[]               mBottoms;

    private int statusBarHeight;
    private int barrageContainerHeight;
    private int heightCount;

    public static BarrageLayoutCalculator getInstance (BarrageManager barrageManager) {
        if (instance == null) {
            instance = new BarrageLayoutCalculator(barrageManager);
        }
        return instance;
    }

    public BarrageLayoutCalculator (BarrageManager barrageManager) {
        mBarrageManager = barrageManager;
        int maxLine = barrageManager.getConfig().getMaxBarrageLine();
        mTops = new boolean[maxLine];
        mBottoms = new boolean[maxLine];
    }

    private int getLineHeightWithPadding () {
        return (int) (1.7f * mBarrageManager.getConfig().getLineHeight());
    }

    int getMarginTop (BarrageView view) {
        switch (view.getBarrageModel().mode) {
            case scrollRandom:
                return getRandomScrollY(view);
            case scroll:
                return getScrollY(view);
            case top:
                return getTopY(view);
            case bottom:
                return getBottomY(view);
        }
        return -1;
    }

    private int getMainTitleAndStatusBarHeight () {
        if (statusBarHeight == 0) {
            statusBarHeight = PreferencesUtils.getInt("MainTitleAndStatusBarHeight", 0);
        }
        return statusBarHeight;
    }

    private int getBarrageContainerHeight () {
        if (barrageContainerHeight == 0) {
            barrageContainerHeight = PreferencesUtils.getInt("BarrageContainerHeight", 0);
        }
        return barrageContainerHeight;
    }

    private int getHeightCount () {
        if (heightCount == 0) {
            heightCount = getBarrageContainerHeight() / getLineHeightWithPadding();
        }
        return heightCount;
    }

    //每秒弹出手机屏幕最大条数不重叠计算
    private List<Integer> indexList = new ArrayList();

    private int getIndex (boolean isClear) {
        if (isClear) indexList.clear();
        int index;
        do {
            index = new Random().nextInt(getHeightCount());
        } while (indexList.contains(index) && indexList.size() < getHeightCount());
        indexList.add(index);
        return index;
    }

    public int getRandomScrollY (BarrageView view) {
        if (mLastBarrageViewList.size() == 0) {
            int index = getIndex(view.getBarrageModel().isClear);
            for (int i = 0; i < getHeightCount(); i++) {
                mLastBarrageViewList.add(i, null);
            }
            mLastBarrageViewList.set(index, view);
            return index * getLineHeightWithPadding() + getMainTitleAndStatusBarHeight();
        }

        for (int i = 0; i < mLastBarrageViewList.size(); i++) {
            int         index = getIndex(view.getBarrageModel().isClear);
            BarrageView last  = mLastBarrageViewList.get(index);
            if (last == null) {
                mLastBarrageViewList.set(index, view);
                return index * getLineHeightWithPadding() + getMainTitleAndStatusBarHeight();
            }
            int     timeDisappear = last.getCurrentDuration(); // 最后一条弹幕还需多久消失
            int     timeArrive    = calTimeArrive(view); // 这条弹幕需要多久到达屏幕边缘
            boolean isFullyShown  = isFullyShown(last);
            if (timeDisappear <= timeArrive && isFullyShown) {
                // 如果最后一个弹幕在这个弹幕到达之前消失，并且最后一个字已经显示完毕，
                // 那么新的弹幕就可以在这一行显示
                mLastBarrageViewList.set(index, view);
                return index * getLineHeightWithPadding() + getMainTitleAndStatusBarHeight();
            }
        }

        return -1;
    }

    private int getScrollY (BarrageView view) {
        if (mLastBarrageViewList.size() == 0) {
            mLastBarrageViewList.add(view);
            return 0;
        }

        int i;
        for (i = 0; i < mLastBarrageViewList.size(); i++) {
            BarrageView last          = mLastBarrageViewList.get(i);
            int         timeDisappear = calTimeDisappear(last); // 最后一条弹幕还需多久消失
            int         timeArrive    = calTimeArrive(view); // 这条弹幕需要多久到达屏幕边缘
            boolean     isFullyShown  = isFullyShown(last);
            if (timeDisappear <= timeArrive && isFullyShown) {
                // 如果最后一个弹幕在这个弹幕到达之前消失，并且最后一个字已经显示完毕，
                // 那么新的弹幕就可以在这一行显示
                mLastBarrageViewList.set(i, view);
                return i * getLineHeightWithPadding();
            }
        }
        int maxLine = mBarrageManager.getConfig().getMaxBarrageLine();
        if (maxLine == 0 || i < maxLine) {
            mLastBarrageViewList.add(view);
            return i * getLineHeightWithPadding();
        }

        return -1;
    }

    private int getTopY (BarrageView view) {
        for (int i = 0; i < mTops.length; i++) {
            boolean isShowing = mTops[i];
            if (!isShowing) {
                final int finalI = i;
                mTops[finalI] = true;
                view.setListener(view1 -> mTops[finalI] = false);
                return i * getLineHeightWithPadding();
            }
        }
        return -1;
    }

    private int getBottomY (BarrageView view) {
        for (int i = 0; i < mBottoms.length; i++) {
            boolean isShowing = mBottoms[i];
            if (!isShowing) {
                final int finalI = i;
                mBottoms[finalI] = true;
                view.setListener(view1 -> mBottoms[finalI] = false);
                return getParentHeight() - (i + 1) * getLineHeightWithPadding();
            }
        }
        return -1;
    }

    /**
     * 这条弹幕是否已经全部出来了。如果没有的话，
     * 后面的弹幕不能出来，否则就重叠了。
     */
    private boolean isFullyShown (BarrageView view) {
        if (view == null) {
            return true;
        }
        float speed            = calSpeed(view);
        int   mCurrentDuration = view.getCurrentDuration();
        return Math.floor(speed * mCurrentDuration) < getParentWidth();
    }

    /**
     * 这条弹幕还有多少毫秒彻底消失。
     */
    private int calTimeDisappear (BarrageView view) {
        if (view == null) {
            return 0;
        }
        float speed      = calSpeed(view);
        int   scrollX    = view.getScrollX();
        int   textLength = view.getTextLength();
        int   wayToGo    = textLength - scrollX;
        return (int) (wayToGo / speed);
    }

    /**
     * 这条弹幕还要多少毫秒抵达屏幕边缘。
     */
    private int calTimeArrive (BarrageView view) {
        float speed   = calSpeed(view);
        int   wayToGo = getParentWidth();
        return (int) (wayToGo / speed);
    }

    /**
     * 这条弹幕的速度。单位：px/ms
     */
    private float calSpeed (BarrageView view) {
        int   textLength = view.getTextLength();
        int   width      = getParentWidth();
        float s          = textLength + width + 0.0f;
        int   t          = mBarrageManager.getDisplayDuration(view.getBarrageModel());
        return s / t;
    }

    private int getParentHeight () {
        ViewGroup parent = mBarrageManager.mBarrageContainer.get();
        if (parent == null || parent.getHeight() == 0) {
            return 1080;
        }
        return parent.getHeight();
    }

    private int getParentWidth () {
        ViewGroup parent = mBarrageManager.mBarrageContainer.get();
        if (parent == null || parent.getWidth() == 0) {
            return 1920;
        }
        return parent.getWidth();
    }


}
