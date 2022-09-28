package com.maishuo.tingshuohenhaowan.bean;

/**
 * author ：Seven
 * date : 2021/8/4
 * description :用户信息回调
 */
public class UserInfoEvent {
    private boolean isVip;
    private String  nickName;
    private String  userAvatar;

    public UserInfoEvent (boolean isVip, String nickName, String userAvatar) {
        this.isVip = isVip;
        this.nickName = nickName;
        this.userAvatar = userAvatar;
    }

    public boolean isVip () {
        return isVip;
    }

    public void setVip (boolean vip) {
        isVip = vip;
    }

    public String getNickName () {
        return nickName;
    }

    public void setNickName (String nickName) {
        this.nickName = nickName;
    }

    public String getUserAvatar () {
        return userAvatar;
    }

    public void setUserAvatar (String userAvatar) {
        this.userAvatar = userAvatar;
    }
}
