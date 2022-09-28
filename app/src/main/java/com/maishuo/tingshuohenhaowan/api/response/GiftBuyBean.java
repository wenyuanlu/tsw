package com.maishuo.tingshuohenhaowan.api.response;

/**
 * author ：yh
 * date : 2021/1/18 11:32
 * description :
 */
public final class GiftBuyBean {
    /**
     * diamodIos : 490
     * diamodAndroid : 20016
     * now_ntegral_num : 9994
     */
    private long diamodIos;
    private long diamodAndroid;
    private long now_ntegral_num;

    public long getDiamodIos () {
        return diamodIos;
    }

    public void setDiamodIos (long diamodIos) {
        this.diamodIos = diamodIos;
    }

    public long getDiamodAndroid () {
        return diamodAndroid;
    }

    public void setDiamodAndroid (long diamodAndroid) {
        this.diamodAndroid = diamodAndroid;
    }

    public long getNow_ntegral_num () {
        return now_ntegral_num;
    }

    public void setNow_ntegral_num (long now_ntegral_num) {
        this.now_ntegral_num = now_ntegral_num;
    }
}