package com.maishuo.tingshuohenhaowan.api.response;

/**
 * author ：yh
 * date : 2021/1/15 15:40
 * description :
 */
public class GoToOnlineBean {
    private String rtm_token;
    private int    uid_int;

    public String getRtmToken () {
        return rtm_token;
    }

    public void setRtmToken (String rtmToken) {
        this.rtm_token = rtmToken;
    }

    public int getUidInt () {
        return uid_int;
    }

    public void setUidInt (int uidInt) {
        this.uid_int = uidInt;
    }
}
