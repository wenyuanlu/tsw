package com.maishuo.tingshuohenhaowan.common;


/**
 * author ：yh
 * date : 2021/1/18 13:13
 * description : 保存在内存中的用户信息等
 */
public final class UserConfig {

    private static volatile UserConfig singleton           = null;
    private                 int        messageUnreadDan    = 0;//弹幕未读数量
    private                 int        messageUnreadZan    = 0;//点赞未读数量
    private                 int        messageUnreadSystem = 0;//系统消息未读数量
    private                 long        uid                 = 0;//点对点登录的uid
    private                 boolean    isPointChatKickAway = false; //点对点是否被踢掉
    private                 boolean    isPointChatLogin    = false;//点对点是否登录
    private                 boolean    isInChat            = false;//是否在聊天页面
    private                 String     inChatUserIntId     = "";//当前聊天页面

    private UserConfig () {
    }

    public static UserConfig getInstance () {
        if (singleton == null) {
            synchronized (UserConfig.class) {
                if (singleton == null) {
                    singleton = new UserConfig();
                }
            }
        }
        return singleton;
    }

    public int getMessageUnreadDan () {
        return messageUnreadDan;
    }

    public void setMessageUnreadDan (int messageUnreadDan) {
        this.messageUnreadDan = messageUnreadDan;
    }

    public int getMessageUnreadZan () {
        return messageUnreadZan;
    }

    public void setMessageUnreadZan (int messageUnreadZan) {
        this.messageUnreadZan = messageUnreadZan;
    }

    public int getMessageUnreadSystem () {
        return messageUnreadSystem;
    }

    public void setMessageUnreadSystem (int messageUnreadSystem) {
        this.messageUnreadSystem = messageUnreadSystem;
    }

    public long getUid () {
        return uid;
    }

    public void setUid (long uid) {
        this.uid = uid;
    }

    public boolean isPointChatKickAway () {
        return isPointChatKickAway;
    }

    public void setPointChatKickAway (boolean pointChatKickAway) {
        isPointChatKickAway = pointChatKickAway;
    }

    public boolean isPointChatLogin () {
        return isPointChatLogin;
    }

    public void setPointChatLogin (boolean pointChatLogin) {
        isPointChatLogin = pointChatLogin;
    }

    public boolean isInChat () {
        return isInChat;
    }

    public void setInChat (boolean inChat) {
        isInChat = inChat;
    }

    public String getInChatUserIntId () {
        return inChatUserIntId;
    }

    public void setInChatUserIntId (String inChatUserIntId) {
        this.inChatUserIntId = inChatUserIntId;
    }

}