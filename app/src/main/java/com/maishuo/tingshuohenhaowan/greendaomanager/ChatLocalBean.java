package com.maishuo.tingshuohenhaowan.greendaomanager;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

import java.io.Serializable;

/**
 * 聊天本地数据库
 */
@Entity
public class ChatLocalBean implements Serializable {
    private static final long   serialVersionUID = 4L;
    @Id (autoincrement = true)
    private              Long   id;  //自增id 不传自增
    private              String messageId;  //消息id
    private              Long   time;       //保存的时间戳
    private              String text;       //
    private              String type;       //1文本，2，image  3音频
    private              String subType;    //用户文本消息 1,显示时间(数据库没有，UI临时生成).2,admin模拟系统消息。3，礼物,4匹配成功弹出界面, 5.关注 6:回撤 7.打招呼
    private              String imagePath;  //
    private              String voicePath;  //
    private              String voiceDuration    = "0";//录音的时间
    private              String giftName;   //
    private              String versions;   //礼物呀的版本
    private              String uid;        //
    private              String toUid;      //
    private              String isSelf;     //1自己，2别人
    private              String mediaId;    //
    private              String reqTime;    //录音的时间
    private              String thumbImagePath; //
    private              String imageWidth;     //
    private              String imageHeight;    //
    private              String isRead;         //1未读  2已读
    private              String sendTime;       //消息发送时间戳/区别接收唯一性
    private              String sendStatus;     //消息发送状态 1,发送中。2,发送成功。3,发送失败
    private              String giftAnimate;    //礼物是否有动画 1,有礼物动画。2，无礼物动画
    private              String customeKey1;    //侧滑礼物的图片
    private              String customeKey2;    //1我喜欢的  2喜欢我的
    private              String customeKey3;    //
    private              String customeKey4;    //
    private              String customeKey5;    //
    private              String customeKey6;    //

    //@Generated (hash = 1816542857)
    @Keep
    public ChatLocalBean (Long id, String messageId, Long time, String text, String type, String subType, String imagePath,
                          String voicePath, String voiceDuration, String giftName, String versions, String uid, String toUid,
                          String isSelf, String mediaId, String reqTime, String thumbImagePath, String imageWidth, String imageHeight,
                          String isRead, String sendTime, String sendStatus, String giftAnimate, String customeKey1, String customeKey2,
                          String customeKey3, String customeKey4, String customeKey5, String customeKey6) {
        this.id = id;
        this.messageId = messageId;
        this.time = time;
        this.text = text;
        this.type = type;
        this.subType = subType;
        this.imagePath = imagePath;
        this.voicePath = voicePath;
        this.voiceDuration = voiceDuration;
        this.giftName = giftName;
        this.versions = versions;
        this.uid = uid;
        this.toUid = toUid;
        this.isSelf = isSelf;
        this.mediaId = mediaId;
        this.reqTime = reqTime;
        this.thumbImagePath = thumbImagePath;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.isRead = isRead;
        this.sendTime = sendTime;
        this.sendStatus = sendStatus;
        this.giftAnimate = giftAnimate;
        this.customeKey1 = customeKey1;
        this.customeKey2 = customeKey2;
        this.customeKey3 = customeKey3;
        this.customeKey4 = customeKey4;
        this.customeKey5 = customeKey5;
        this.customeKey6 = customeKey6;
    }

    //@Generated (hash = 222009260)
    @Keep
    public ChatLocalBean () {
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getMessageId () {
        return messageId;
    }

    public void setMessageId (String messageId) {
        this.messageId = messageId;
    }

    public Long getTime () {
        return time;
    }

    public void setTime (Long time) {
        this.time = time;
    }

    public String getText () {
        return text;
    }

    public void setText (String text) {
        this.text = text;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public String getSubType () {
        return subType;
    }

    public void setSubType (String subType) {
        this.subType = subType;
    }

    public String getImagePath () {
        return imagePath;
    }

    public void setImagePath (String imagePath) {
        this.imagePath = imagePath;
    }

    public String getVoicePath () {
        return voicePath;
    }

    public void setVoicePath (String voicePath) {
        this.voicePath = voicePath;
    }

    public String getVoiceDuration () {
        if (TextUtils.isEmpty(voiceDuration)) {
            return "0";
        }
        return voiceDuration;
    }

    public void setVoiceDuration (String voiceDuration) {
        this.voiceDuration = voiceDuration;
    }

    public String getGiftName () {
        return giftName;
    }

    public void setGiftName (String giftName) {
        this.giftName = giftName;
    }

    public String getVersions () {
        return versions;
    }

    public void setVersions (String versions) {
        this.versions = versions;
    }

    public String getUid () {
        return uid;
    }

    public void setUid (String uid) {
        this.uid = uid;
    }

    public String getToUid () {
        return toUid;
    }

    public void setToUid (String toUid) {
        this.toUid = toUid;
    }

    public String getIsSelf () {
        return isSelf;
    }

    public void setIsSelf (String isSelf) {
        this.isSelf = isSelf;
    }

    public String getMediaId () {
        return mediaId;
    }

    public void setMediaId (String mediaId) {
        this.mediaId = mediaId;
    }

    public String getReqTime () {
        return reqTime;
    }

    public void setReqTime (String reqTime) {
        this.reqTime = reqTime;
    }

    public String getThumbImagePath () {
        return thumbImagePath;
    }

    public void setThumbImagePath (String thumbImagePath) {
        this.thumbImagePath = thumbImagePath;
    }

    public String getImageWidth () {
        return imageWidth;
    }

    public void setImageWidth (String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageHeight () {
        return imageHeight;
    }

    public void setImageHeight (String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getIsRead () {
        return isRead;
    }

    public void setIsRead (String isRead) {
        this.isRead = isRead;
    }

    public String getSendTime () {
        return sendTime;
    }

    public void setSendTime (String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendStatus () {
        return sendStatus;
    }

    public void setSendStatus (String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getGiftAnimate () {
        return giftAnimate;
    }

    public void setGiftAnimate (String giftAnimate) {
        this.giftAnimate = giftAnimate;
    }

    public String getCustomeKey1 () {
        return customeKey1;
    }

    public void setCustomeKey1 (String customeKey1) {
        this.customeKey1 = customeKey1;
    }

    public String getCustomeKey2 () {
        return customeKey2;
    }

    public void setCustomeKey2 (String customeKey2) {
        this.customeKey2 = customeKey2;
    }

    public String getCustomeKey3 () {
        return customeKey3;
    }

    public void setCustomeKey3 (String customeKey3) {
        this.customeKey3 = customeKey3;
    }

    public String getCustomeKey4 () {
        return customeKey4;
    }

    public void setCustomeKey4 (String customeKey4) {
        this.customeKey4 = customeKey4;
    }

    public String getCustomeKey5 () {
        return customeKey5;
    }

    public void setCustomeKey5 (String customeKey5) {
        this.customeKey5 = customeKey5;
    }

    public String getCustomeKey6 () {
        return customeKey6;
    }

    public void setCustomeKey6 (String customeKey6) {
        this.customeKey6 = customeKey6;
    }
}
