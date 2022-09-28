package com.maishuo.tingshuohenhaowan.auth;

/**
 * Create by yun on 2020-03-30
 * EXPLAIN:
 */
public class BaiduAuthBean {

    public BaiduAuthBean (String username, String userid, String userpic, String errorMessage) {
        this.username = username;
        this.userid = userid;
        this.userpic = userpic;
        this.errorMessage = errorMessage;
    }

    String username;
    String userid;
    String userpic;
    String errorMessage;

    public String getErrorMessage () {
        return errorMessage;
    }

    public String getUsername () {
        return username;
    }

    public void setUsername (String username) {
        this.username = username;
    }

    public String getUserid () {
        return userid;
    }

    public void setUserid (String userid) {
        this.userid = userid;
    }

    public String getUserpic () {
        return userpic;
    }

    public void setUserpic (String userpic) {
        this.userpic = userpic;
    }
}
