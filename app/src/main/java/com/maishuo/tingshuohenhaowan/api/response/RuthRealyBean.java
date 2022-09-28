package com.maishuo.tingshuohenhaowan.api.response;

/**
 * 获取认证转态
 */
public class RuthRealyBean {
    private int    status;//0百度认证 1认证提示 2,阿里认证
    private String reason;

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }

    public String getReason () {
        return reason;
    }

    public void setReason (String reason) {
        this.reason = reason;
    }
}