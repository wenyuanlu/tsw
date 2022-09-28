package com.maishuo.tingshuohenhaowan.api.response;

/**
 * 系统消息的消息的数据
 */
public class SystemMessageBean {

    /**
     * systemMessageId : 65795
     * systemMessageAvatar : http://test.tingshuowan.com/listen/path?url=/default/system_v2.png
     * systemMessageTitle : VIP成功开通
     * systemMessageContent : 恭喜！您已成功开通VIP，快去听书吧！
     * isUnread : false
     * systemMessageTime : 2021-02-23
     */

    private int     systemMessageId;
    private String  systemMessageAvatar;
    private String  systemMessageTitle;
    private String  systemMessageContent;
    private boolean isUnread;
    private String  systemMessageTime;

    public int getSystemMessageId () {
        return systemMessageId;
    }

    public void setSystemMessageId (int systemMessageId) {
        this.systemMessageId = systemMessageId;
    }

    public String getSystemMessageAvatar () {
        return systemMessageAvatar;
    }

    public void setSystemMessageAvatar (String systemMessageAvatar) {
        this.systemMessageAvatar = systemMessageAvatar;
    }

    public String getSystemMessageTitle () {
        return systemMessageTitle;
    }

    public void setSystemMessageTitle (String systemMessageTitle) {
        this.systemMessageTitle = systemMessageTitle;
    }

    public String getSystemMessageContent () {
        return systemMessageContent;
    }

    public void setSystemMessageContent (String systemMessageContent) {
        this.systemMessageContent = systemMessageContent;
    }

    public boolean isIsUnread () {
        return isUnread;
    }

    public void setIsUnread (boolean isUnread) {
        this.isUnread = isUnread;
    }

    public String getSystemMessageTime () {
        return systemMessageTime;
    }

    public void setSystemMessageTime (String systemMessageTime) {
        this.systemMessageTime = systemMessageTime;
    }
}