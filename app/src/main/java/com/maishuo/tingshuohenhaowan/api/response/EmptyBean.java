package com.maishuo.tingshuohenhaowan.api.response;

/**
 * author ：yh
 * date : 2021/1/18 11:32
 * description :
 */
public final class EmptyBean {

    private int status;

    public String getPayMessage() {
        return status == 1 ? "支付成功" : "支付失败";
    }

}