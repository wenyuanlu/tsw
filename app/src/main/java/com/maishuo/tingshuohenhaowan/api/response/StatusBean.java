package com.maishuo.tingshuohenhaowan.api.response;

/**
 * @author yun on 2021/1/26
 * EXPLAIN:
 */
public class StatusBean {

    /**
     * code : 200
     * data : {"status": 1 - 取消关注 2 - 互粉 3 - 已关注}
     * msg : 关注成功
     */


    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
