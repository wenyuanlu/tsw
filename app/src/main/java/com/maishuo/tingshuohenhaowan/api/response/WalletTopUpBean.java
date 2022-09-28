package com.maishuo.tingshuohenhaowan.api.response;


import com.google.gson.annotations.SerializedName;

public class WalletTopUpBean {


    /**
     * appid : wxd93e78461c72d692
     * partnerid : 1554558241
     * prepayid : wx101836268673427022402ec11535006100
     * package : Sign=WXPay
     * noncestr : 0FF7BA2B3528D3FEFF7F40
     * timestamp : 134309238233
     * sign : 0FF7BA2B398C0C5CE82528D3FEFF7F40
     * orderId : e987945
     */

    private String appid;
    private String partnerid;
    private String prepayid;
    @SerializedName("package")
    private String packageX;
    private String noncestr;
    private Long timestamp;
    private String sign;
    private String orderId;
    private String order;//支付宝

    public String getAppid () {
        return appid;
    }

    public void setAppid (String appid) {
        this.appid = appid;
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

    public String getPackageX () {
        return packageX;
    }

    public void setPackageX (String packageX) {
        this.packageX = packageX;
    }

    public String getNoncestr () {
        return noncestr;
    }

    public void setNoncestr (String noncestr) {
        this.noncestr = noncestr;
    }

    public Long getTimestamp () {
        return timestamp;
    }

    public void setTimestamp (Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign () {
        return sign;
    }

    public void setSign (String sign) {
        this.sign = sign;
    }

    public String getOrderId () {
        return orderId;
    }

    public void setOrderId (String orderId) {
        this.orderId = orderId;
    }

    public String getOrder () {
        return order;
    }

    public void setOrder (String order) {
        this.order = order;
    }
}
