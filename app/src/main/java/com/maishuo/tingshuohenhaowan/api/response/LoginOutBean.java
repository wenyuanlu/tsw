package com.maishuo.tingshuohenhaowan.api.response;

/**
 * author ：yh
 * date : 2021/1/15 15:40
 * description :
 */
public class LoginOutBean {
    private String loginStatus;
    private String token;

    public String getLoginStatus () {
        return loginStatus;
    }

    public void setLoginStatus (String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getToken () {
        return token;
    }

    public void setToken (String token) {
        this.token = token;
    }
}
