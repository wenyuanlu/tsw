package com.maishuo.tingshuohenhaowan.api.response;

/**
 * Create by yun on 2021/3/12
 * EXPLAIN:
 */
public class HeadImageMakingBean {
    String avatar;
    int status;  //0 制作中 1制作完成

    public String getAvatar () {
        return avatar;
    }

    public void setAvatar (String avatar) {
        this.avatar = avatar;
    }

    public int getStatus () {
        return status;
    }

    public void setStatus (int status) {
        this.status = status;
    }
}
