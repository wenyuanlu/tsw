package com.maishuo.tingshuohenhaowan.api.response;

/**
 * 获取验证码返回
 */
public class VerifyCodeBean {
    private int status;
    private int isNewUser;

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }

    public int getIsNewUser () {
        return isNewUser;
    }

    public void setIsNewUser (int isNewUser) {
        this.isNewUser = isNewUser;
    }
}