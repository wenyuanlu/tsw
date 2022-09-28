package com.maishuo.tingshuohenhaowan.bean;

/**
 * author ：yh
 * date : 2021/3/1 13:07
 * description :
 */
public class SystemMessageEvent {
    private int position;

    public SystemMessageEvent (int position) {
        this.position = position;
    }

    public int getPosition () {
        return position;
    }

    public void setPosition (int position) {
        this.position = position;
    }
}
