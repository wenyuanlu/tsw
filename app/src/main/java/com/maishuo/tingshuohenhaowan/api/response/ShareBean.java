package com.maishuo.tingshuohenhaowan.api.response;

/**
 * 分享
 */
public class ShareBean {
    private int webTyp;
    private String title;
    private String desc;
    private String url;
    private String image;
    private String wechatTitle;
    private String layerSlogan;
    private String userId;
    private String userName;
    private String deeplinkurl;
    private String activity_name;//活动提现名称
    private int category_id;//分享成功上报
    private String obj_id;//分享成功上报

    public int getWebTyp() {
        return webTyp;
    }

    public void setWebTyp(int webTyp) {
        this.webTyp = webTyp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWechatTitle() {
        return wechatTitle;
    }

    public void setWechatTitle(String wechatTitle) {
        this.wechatTitle = wechatTitle;
    }

    public String getLayerSlogan() {
        return layerSlogan;
    }

    public void setLayerSlogan(String layerSlogan) {
        this.layerSlogan = layerSlogan;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeeplinkurl() {
        return deeplinkurl;
    }

    public void setDeeplinkurl(String deeplinkurl) {
        this.deeplinkurl = deeplinkurl;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getObj_id() {
        return obj_id;
    }

    public void setObj_id(String obj_id) {
        this.obj_id = obj_id;
    }
}