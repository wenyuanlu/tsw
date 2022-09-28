package com.maishuo.tingshuohenhaowan.api.response;

/**
 * 弹幕的消息的数据
 */
public class BarrageMessageBean {

    /**
     * userId : f3291e84b48ff4003431c7ea984206ca
     * userName : 嘿嘿
     * userAvatat : http://test.tingshuowan.com/listen/path?url=/users/f3291e84b48ff4003431c7ea984206ca/images/2021/6e9abca3fe0a8ad19deaf719317f20fa.jpg
     * content : 5
     * contentType : 评论了我的作品
     * time : 1月22日
     * voice_id : 10416
     * voiceTitle : 我的作品：有感情的朗读《三字经》——《最美的遇见》，第八集#10月9日声优戴超行直播间
     * status : 1
     */

    private String userId;
    private String userName;
    private String userAvatat;
    private String image_path;
    private String content;
    private String contentType;
    private String time;
    private int    voice_id;
    private String voiceTitle;
    private int    status;
    private int    stayvoice_is_del;//0-正常，1-已删除

    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public String getUserName () {
        return userName;
    }

    public void setUserName (String userName) {
        this.userName = userName;
    }

    public String getUserAvatat () {
        return userAvatat;
    }

    public void setUserAvatat (String userAvatat) {
        this.userAvatat = userAvatat;
    }

    public String getContent () {
        return content;
    }

    public void setContent (String content) {
        this.content = content;
    }

    public String getContentType () {
        return contentType;
    }

    public void setContentType (String contentType) {
        this.contentType = contentType;
    }

    public String getTime () {
        return time;
    }

    public void setTime (String time) {
        this.time = time;
    }

    public int getVoice_id () {
        return voice_id;
    }

    public void setVoice_id (int voice_id) {
        this.voice_id = voice_id;
    }

    public String getVoiceTitle () {
        return voiceTitle;
    }

    public void setVoiceTitle (String voiceTitle) {
        this.voiceTitle = voiceTitle;
    }

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }

    public String getImage_path () {
        return image_path;
    }

    public void setImage_path (String image_path) {
        this.image_path = image_path;
    }

    public int getStayvoice_is_del () {
        return stayvoice_is_del;
    }

    public void setStayvoice_is_del (int stayvoice_is_del) {
        this.stayvoice_is_del = stayvoice_is_del;
    }
}