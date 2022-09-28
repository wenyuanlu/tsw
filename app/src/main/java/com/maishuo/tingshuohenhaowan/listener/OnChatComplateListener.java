package com.maishuo.tingshuohenhaowan.listener;


import com.maishuo.tingshuohenhaowan.greendaomanager.ChatLocalBean;

/**
 * author: yh
 * date: 2021/1/20 10:14
 * description: 发送完成回调
 */
public interface OnChatComplateListener {
    void onComplate (ChatLocalBean hiBean, ChatLocalBean sysBean);
}
