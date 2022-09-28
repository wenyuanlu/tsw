package com.maishuo.tingshuohenhaowan.api.response;

/**
 * 获赞的消息的数据
 */
public class PraiseMessageBean {

    /**
     * userId : bfa1e68e13d2f0665118da6d584de09d
     * praiseTime : 2021-02-26
     * userAvatar : http://test.tingshuowan.com/listen/path?url=/users/bfa1e68e13d2f0665118da6d584de09d/images/2020/1de803ed476f05a159f0a132597dab42_animation.jpg
     * userName : 小嘴巴红尾伯劳
     * praiseType : 赞了我的作品
     * praiseContent : 我的作品：第一次上传
     * worksCoverImage : http://test.tingshuowan.com/listen/path?url=/users/e78d9b7d1b70e2766eb5342dce1ae0e1/stayvoice/images/2020/3811e9cf62beb7ffd5e86f5196ea7586.jpg
     * voice_id : 11652
     * status : 0
     */

    private String userId;
    private String praiseTime;
    private String userAvatar;
    private String userName;
    private String praiseType;
    private String praiseContent;
    private String worksCoverImage;
    private int    voice_id;
    private int    status;
    private int    stayvoice_is_del;//0-正常，1-已删除

    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public String getPraiseTime () {
        return praiseTime;
    }

    public void setPraiseTime (String praiseTime) {
        this.praiseTime = praiseTime;
    }

    public String getUserAvatar () {
        return userAvatar;
    }

    public void setUserAvatar (String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserName () {
        return userName;
    }

    public void setUserName (String userName) {
        this.userName = userName;
    }

    public String getPraiseType () {
        return praiseType;
    }

    public void setPraiseType (String praiseType) {
        this.praiseType = praiseType;
    }

    public String getPraiseContent () {
        return praiseContent;
    }

    public void setPraiseContent (String praiseContent) {
        this.praiseContent = praiseContent;
    }

    public String getWorksCoverImage () {
        return worksCoverImage;
    }

    public void setWorksCoverImage (String worksCoverImage) {
        this.worksCoverImage = worksCoverImage;
    }

    public int getVoice_id () {
        return voice_id;
    }

    public void setVoice_id (int voice_id) {
        this.voice_id = voice_id;
    }

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }

    public int getStayvoice_is_del () {
        return stayvoice_is_del;
    }

    public void setStayvoice_is_del (int stayvoice_is_del) {
        this.stayvoice_is_del = stayvoice_is_del;
    }
}