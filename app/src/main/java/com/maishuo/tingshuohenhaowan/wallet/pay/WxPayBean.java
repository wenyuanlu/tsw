package com.maishuo.tingshuohenhaowan.wallet.pay;

import com.google.gson.annotations.SerializedName;

public class WxPayBean {


    /**
     * pay_info : {"appid":"wx258ba8a5a5ee163b","partnerid":"1486905462","prepayid":"wx08185316548198b0909b5321e9f1870000","timestamp":"1610103196","noncestr":"9sfYGlZxwrrE7gdQ","package":"Sign=WXPay","sign":"A04B9EA085FB2BE4A47017286EDE0991"}
     * channel : 10
     */

    private PayInfoBean pay_info;
    private int channel;

    public PayInfoBean getPay_info() {
        return pay_info;
    }

    public void setPay_info(PayInfoBean pay_info) {
        this.pay_info = pay_info;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public static class PayInfoBean {
        /**
         * appid : wx258ba8a5a5ee163b
         * partnerid : 1486905462
         * prepayid : wx08185316548198b0909b5321e9f1870000
         * timestamp : 1610103196
         * noncestr : 9sfYGlZxwrrE7gdQ
         * package : Sign=WXPay
         * sign : A04B9EA085FB2BE4A47017286EDE0991
         */

        private String appid;
        private String partnerid;
        private String prepayid;
        private Long timestamp;
        private String noncestr;
        @SerializedName("package")
        private String packageX;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
