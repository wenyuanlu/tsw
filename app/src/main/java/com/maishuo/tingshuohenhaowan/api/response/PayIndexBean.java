package com.maishuo.tingshuohenhaowan.api.response;

import java.util.List;

/**
 * yun on 2021/1/28
 * EXPLAIN:
 */
public class PayIndexBean {


    /**
     * userDiamond : 514
     * userRestDiamond : 0
     * isShowAccount : 0
     * account : 0
     * userIsFirstPay : 1
     * rechargeTitle : 充值说明
     * rechargeRule : 1、充值未到账
     * 一般充值后玩钻会即刻发放至您的账户，如遇到服务器拥堵，玩钻到账可能有短时间的延迟，请耐心等待，若超出30分钟仍未到账，请您联系官方客服。
     * 2、充值是否可开具发票
     * 目前暂不支持开具发票
     * 特别提醒：未成年用户在使用充值服务时，请在监护人的同意和指导下进行。
     * vipRule : VIP用户充值后可享受相应的额外玩钻
     * payLists : [{"payId":265,"pay":1,"applePayId":"com.tingshuowan.liwu_1yuan","diamondNumber":10,"freeDiamondNumber":0,"payDesc":"首次充值玩钻"},{"payId":267,"pay":30,"applePayId":"com.tingshuowan.liwu_30yuan","diamondNumber":300,"freeDiamondNumber":0,"payDesc":"购买30元玩钻，会员赠送30个玩钻"},{"payId":268,"pay":98,"applePayId":"com.tingshuowan.liwu_98yuan","diamondNumber":980,"freeDiamondNumber":0,"payDesc":"购买98元玩钻，会员赠送98个玩钻"},{"payId":269,"pay":298,"applePayId":"com.tingshuowan.liwu_298yuan","diamondNumber":2980,"freeDiamondNumber":0,"payDesc":"购买298元玩钻，会员赠送298个玩钻"},{"payId":270,"pay":518,"applePayId":"com.tingshuowan.liwu_518yuan","diamondNumber":5180,"freeDiamondNumber":0,"payDesc":"购买518元玩钻，会员赠送518个玩钻"},{"payId":271,"pay":1598,"applePayId":"com.tingshuowan.liwu_1598yuan","diamondNumber":15980,"freeDiamondNumber":0,"payDesc":"买1598元玩钻，会员赠送1598个玩钻"}]
     */

    private int                userDiamond;
    private int                userRestDiamond;
    private int                isShowAccount;
    private int                account;
    private int                userIsFirstPay;
    private String             rechargeTitle;
    private String             rechargeRule;
    private String             vipRule;
    private List<PayListsBean> payLists;

    public int getUserDiamond () {
        return userDiamond;
    }

    public void setUserDiamond (int userDiamond) {
        this.userDiamond = userDiamond;
    }

    public int getUserRestDiamond () {
        return userRestDiamond;
    }

    public void setUserRestDiamond (int userRestDiamond) {
        this.userRestDiamond = userRestDiamond;
    }

    public int getIsShowAccount () {
        return isShowAccount;
    }

    public void setIsShowAccount (int isShowAccount) {
        this.isShowAccount = isShowAccount;
    }

    public int getAccount () {
        return account;
    }

    public void setAccount (int account) {
        this.account = account;
    }

    public int getUserIsFirstPay () {
        return userIsFirstPay;
    }

    public void setUserIsFirstPay (int userIsFirstPay) {
        this.userIsFirstPay = userIsFirstPay;
    }

    public String getRechargeTitle () {
        return rechargeTitle;
    }

    public void setRechargeTitle (String rechargeTitle) {
        this.rechargeTitle = rechargeTitle;
    }

    public String getRechargeRule () {
        return rechargeRule;
    }

    public void setRechargeRule (String rechargeRule) {
        this.rechargeRule = rechargeRule;
    }

    public String getVipRule () {
        return vipRule;
    }

    public void setVipRule (String vipRule) {
        this.vipRule = vipRule;
    }

    public List<PayListsBean> getPayLists () {
        return payLists;
    }

    public void setPayLists (List<PayListsBean> payLists) {
        this.payLists = payLists;
    }

    public static class PayListsBean {
        /**
         * payId : 265
         * pay : 1
         * applePayId : com.tingshuowan.liwu_1yuan
         * diamondNumber : 10
         * freeDiamondNumber : 0
         * payDesc : 首次充值玩钻
         */

        private int    payId;
        private int    pay;
        private String applePayId;
        private int    diamondNumber;
        private int    freeDiamondNumber;
        private String payDesc;

        public int getPayId () {
            return payId;
        }

        public void setPayId (int payId) {
            this.payId = payId;
        }

        public int getPay () {
            return pay;
        }

        public void setPay (int pay) {
            this.pay = pay;
        }

        public String getApplePayId () {
            return applePayId;
        }

        public void setApplePayId (String applePayId) {
            this.applePayId = applePayId;
        }

        public int getDiamondNumber () {
            return diamondNumber;
        }

        public void setDiamondNumber (int diamondNumber) {
            this.diamondNumber = diamondNumber;
        }

        public int getFreeDiamondNumber () {
            return freeDiamondNumber;
        }

        public void setFreeDiamondNumber (int freeDiamondNumber) {
            this.freeDiamondNumber = freeDiamondNumber;
        }

        public String getPayDesc () {
            return payDesc;
        }

        public void setPayDesc (String payDesc) {
            this.payDesc = payDesc;
        }
    }


}
