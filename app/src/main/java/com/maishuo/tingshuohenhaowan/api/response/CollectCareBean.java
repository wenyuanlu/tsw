package com.maishuo.tingshuohenhaowan.api.response;


public class CollectCareBean {

    /**
     * uid_int : 10000134
     * userId : f1ad072793db41dc3ea1466e7e8cffef
     * userName : 施主请自重
     * userAvatar : http://test.tingshuowan.com/listen/path?url=/users/f1ad072793db41dc3ea1466e7e8cffef/images/2020/5141b188a7528099a0a0c838168cb668_animation.jpg
     * userPersonSign : 唯男神与美食不可辜负
     * userSex : 1
     * attentionsStatus : 1
     * liveNumber : 0
     * worksNumber : 0
     * voiceNumber : 0
     * userFans : 51
     * phonicNumber : 15
     */

    private int    uid_int;
    private String userId;
    private String userName;
    private String userAvatar;
    private String userPersonSign;
    private int    userSex;
    private int    attentionsStatus;
    private int    liveNumber;
    private int    worksNumber;
    private int    voiceNumber;
    private int    userFans;
    private int    phonicNumber;
    private int    isFirstSayHi;//是否打过招呼 1未打过招呼,0是打过招呼

    public int getUid_int () {
        return uid_int;
    }

    public void setUid_int (int uid_int) {
        this.uid_int = uid_int;
    }

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

    public String getUserAvatar () {
        return userAvatar;
    }

    public void setUserAvatar (String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserPersonSign () {
        return userPersonSign;
    }

    public void setUserPersonSign (String userPersonSign) {
        this.userPersonSign = userPersonSign;
    }

    public int getUserSex () {
        return userSex;
    }

    public void setUserSex (int userSex) {
        this.userSex = userSex;
    }

    public int getAttentionsStatus () {
        return attentionsStatus;
    }

    public void setAttentionsStatus (int attentionsStatus) {
        this.attentionsStatus = attentionsStatus;
    }

    public int getLiveNumber () {
        return liveNumber;
    }

    public void setLiveNumber (int liveNumber) {
        this.liveNumber = liveNumber;
    }

    public int getWorksNumber () {
        return worksNumber;
    }

    public void setWorksNumber (int worksNumber) {
        this.worksNumber = worksNumber;
    }

    public int getVoiceNumber () {
        return voiceNumber;
    }

    public void setVoiceNumber (int voiceNumber) {
        this.voiceNumber = voiceNumber;
    }

    public int getUserFans () {
        return userFans;
    }

    public void setUserFans (int userFans) {
        this.userFans = userFans;
    }

    public int getPhonicNumber () {
        return phonicNumber;
    }

    public void setPhonicNumber (int phonicNumber) {
        this.phonicNumber = phonicNumber;
    }

    public int getIsFirstSayHi () {
        return isFirstSayHi;
    }

    public void setIsFirstSayHi (int isFirstSayHi) {
        this.isFirstSayHi = isFirstSayHi;
    }
}