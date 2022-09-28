package com.maishuo.tingshuohenhaowan.listener;


import com.maishuo.tingshuohenhaowan.api.response.GetGfitBean;

/**
 * author: yh
 * date: 2021/1/20 10:14
 * description: 条目点击的回调
 */
public interface OnViewPageItemClickListener {
    void onClick (int position, int pagePosition, GetGfitBean.GiftsBean item);
}
