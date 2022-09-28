package com.maishuo.tingshuohenhaowan.gift.sideslipgift;

/**
 * author ：yh
 * date : 2021/2/1 18:08
 * description : 礼物标识
 */
public interface GiftIdentify extends Comparable<GiftIdentify> {

    /**
     * 礼物Id
     */
    int getTheGiftId ();

    void setTheGiftId (int gid);

    /**
     * 礼物的name
     */
    String getTheGiftName ();

    void setTheGiftName (String gname);

    /**
     * 用户Id
     */
    String getTheUserId ();

    void setTheUserId (String uid);

    /**
     * 礼物累计数
     */
    int getTheGiftCount ();

    void setTheGiftCount (int count);

    /**
     * 单次礼物赠送数目
     */
    int getTheSendGiftSize ();

    void setTheSendGiftSize (int size);

    /**
     * 礼物停留时间
     */
    long getTheGiftStay ();

    void setTheGiftStay (long stay);

    /**
     * 礼物最新一次刷新时间戳
     */
    long getTheLatestRefreshTime ();

    void setTheLatestRefreshTime (long time);

    /**
     * 礼物索引
     */
    int getTheCurrentIndex ();

    void setTheCurrentIndex (int index);
}
