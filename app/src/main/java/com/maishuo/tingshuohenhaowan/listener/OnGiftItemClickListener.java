package com.maishuo.tingshuohenhaowan.listener;


import com.maishuo.tingshuohenhaowan.gift.giftbean.GiftBackBean;

/**
 * author: yh
 * date: 2021/1/20 10:14
 * description:
 */
public interface OnGiftItemClickListener {
    void onClickPosition (GiftBackBean giftBean, String isSelf);

    void onGiftBack ();
}
