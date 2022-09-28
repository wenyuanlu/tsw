package com.maishuo.tingshuohenhaowan.rtmchat;

/**
 * author ：yh
 * date : 2021/1/18 14:49
 * description :回调
 */
public interface RtmChatListener {
    void successBack (String file);

    void failBack ();//"0"是失败,"1"是成功
}
