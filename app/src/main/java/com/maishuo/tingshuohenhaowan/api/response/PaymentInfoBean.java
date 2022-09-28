package com.maishuo.tingshuohenhaowan.api.response;

/**
 * @author yun on 2021/1/28
 * EXPLAIN:
 */
public class PaymentInfoBean {

    /**
     * appid : string
     * noncestr : string
     * partnerid : string
     * prepayid : string
     * sign : string
     * timestamp : string
     * wxpackage : string
     */

    private String appid;
    private String noncestr;
    private String partnerid;
    private String prepayid;
    private String sign;
    private Long timestamp;
    private String wxpackage;
    private String orderId;

    public String getOrderId () {
        return orderId;
    }

    public void setOrderId (String orderId) {
        this.orderId = orderId;
    }

    public String getAppid () {
        return appid;
    }

    public void setAppid (String appid) {
        this.appid = appid;
    }

    public String getNoncestr () {
        return noncestr;
    }

    public void setNoncestr (String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPartnerid () {
        return partnerid;
    }

    public void setPartnerid (String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid () {
        return prepayid;
    }

    public void setPrepayid (String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign () {
        return sign;
    }

    public void setSign (String sign) {
        this.sign = sign;
    }

    public Long getTimestamp () {
        return timestamp;
    }

    public void setTimestamp (Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getWxpackage () {
        return wxpackage;
    }

    public void setWxpackage (String wxpackage) {
        this.wxpackage = wxpackage;
    }
}
