package com.maishuo.tingshuohenhaowan.bean;

/**
 * author ：Seven
 * date : 2021/4/13
 * description :
 */
public class ChatVoiceClickEvent {
    public int toUid;
    public int position;

    public ChatVoiceClickEvent(int toUid, int position) {
        this.toUid = toUid;
        this.position = position;
    }

}
