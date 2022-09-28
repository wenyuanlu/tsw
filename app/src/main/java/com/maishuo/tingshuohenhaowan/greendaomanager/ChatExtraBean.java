package com.maishuo.tingshuohenhaowan.greendaomanager;

import java.io.Serializable;
import java.util.Map;

/**
 * 聊天,附加的数据,作为content传递
 */
public class ChatExtraBean implements Serializable {
    private String subType = ""; //1,显示时间(数据库没有，UI临时生成).2,admin模拟系统消息。3,礼物 ,4匹配成功弹出界面,5.关注，6：回撤 7.打招呼
    private String voiceDuration;
    private String giftName;
    private String versions;
    private String sendTime;//时间戳
    private String giftAnimate; //1有礼物动画  2无礼物动画
    private String isLikeMe; //1我喜欢的  2喜欢我的
    private String extMap; //额外的map对象的String格式(deleteSendTime:回撤)
    private String userID; //用户id
    private String userName; //用户昵称
    private String avatarUrl; //用户头像
    private String timestamp; //时间戳 配合ios需要

    public ChatExtraBean () {
    }

    public ChatExtraBean (Map map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        this.subType = (String) map.get("subType");
        this.voiceDuration = (String) map.get("voiceDuration");
        this.giftName = (String) map.get("giftName");
        this.versions = (String) map.get("versions");
        this.sendTime = (String) map.get("sendTime");
        this.giftAnimate = (String) map.get("giftAnimate");
        this.isLikeMe = (String) map.get("isLikeMe");
        this.extMap = (String) map.get("extMap");
        this.userID = (String) map.get("userID");
        this.userName = (String) map.get("userName");
        this.avatarUrl = (String) map.get("avatarUrl");
        this.timestamp = (String) map.get("timestamp");
    }


    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getVoiceDuration() {
        return voiceDuration;
    }

    public void setVoiceDuration(String voiceDuration) {
        this.voiceDuration = voiceDuration;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getVersions() {
        return versions;
    }

    public void setVersions(String versions) {
        this.versions = versions;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getGiftAnimate() {
        return giftAnimate;
    }

    public void setGiftAnimate(String giftAnimate) {
        this.giftAnimate = giftAnimate;
    }

    public String getIsLikeMe() {
        return isLikeMe;
    }

    public void setIsLikeMe(String isLikeMe) {
        this.isLikeMe = isLikeMe;
    }

    public String getExtMap() {
        return extMap;
    }

    public void setExtMap(String extMap) {
        this.extMap = extMap;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
