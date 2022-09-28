package com.maishuo.tingshuohenhaowan.gift.giftbean;


import androidx.annotation.NonNull;

import com.maishuo.tingshuohenhaowan.gift.sideslipgift.GiftIdentify;

/**
 * author ：yh
 * date : 2021/2/1 18:22
 * description :
 */
public abstract class BaseGiftBean implements GiftIdentify, Cloneable {

    /**
     * 礼物计数
     */
    private int  giftCount;
    /**
     * 礼物刷新时间
     */
    private long latestRefreshTime;
    /**
     * 当前index
     */
    private int  currentIndex;

    @Override
    public int getTheGiftCount () {
        return giftCount;
    }

    @Override
    public long getTheLatestRefreshTime () {
        return latestRefreshTime;
    }

    @Override
    public int getTheCurrentIndex () {
        return currentIndex;
    }

    @Override
    public void setTheGiftCount (int count) {
        giftCount = count;
    }

    @Override
    public void setTheLatestRefreshTime (long time) {
        latestRefreshTime = time;
    }

    @Override
    public void setTheCurrentIndex (int index) {
        currentIndex = index;
    }

    @Override
    public int compareTo (@NonNull GiftIdentify o) {
        return (int) (this.getTheLatestRefreshTime() - o.getTheLatestRefreshTime());
    }

    @Override
    public Object clone () throws CloneNotSupportedException {
        return super.clone();
    }
}
