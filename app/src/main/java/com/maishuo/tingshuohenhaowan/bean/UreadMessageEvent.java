package com.maishuo.tingshuohenhaowan.bean;

/**
 * author ：yh
 * date : 2021/3/1 13:07
 * description :未读消息的更新
 */
public class UreadMessageEvent {
    private int type;

    public UreadMessageEvent () {
    }

    public UreadMessageEvent (int type) {
        this.type = type;
    }

    public int getType () {
        return type;
    }

    public void setType (int type) {
        this.type = type;
    }
}
