package com.maishuo.tingshuohenhaowan.api.response;

import java.util.List;

/**
 * author ：Seven
 * date : 3/16/21
 * description :充值页
 */
public class WalletTopUpIndexBean {

    /**
     * userDiamond : 0
     * isShowAccount : 0
     * account : 0
     * userRestDiamond : 0
     * userIsFirstPay : 1
     * rechargeTitle : 充值说明
     * rechargeRule : 1、充值未到账一般充值后玩钻会即刻发放至您的账户，如遇到服务器拥堵，玩钻到账可能有短时间的延迟，请耐心等待，若超出30分钟仍未到账，请您联系官方客服。 2、充值是否可开具发票目前暂不支持开具发票 特别提醒：未成年用户在使用充值服务时，请在监护人的同意和指导下进行。
     * vipRule : VIP用户充值后可享受相应的额外玩钻
     * payLists : [{"payId":265,"pay":1,"applePayId":"com.tingshuowan.zhibo_shouci","diamondNumber":70,"freeDiamondNumber":0,"payDesc":"首次充值玩钻"},{"payId":267,"pay":30,"applePayId":"com.tingshuowan.zhibo_30yuan","diamondNumber":2100,"freeDiamondNumber":0,"payDesc":"购买30元玩钻，会员赠送210个玩钻"},{"payId":268,"pay":98,"applePayId":"com.tingshuowan.zhibo_98yuan","diamondNumber":6860,"freeDiamondNumber":0,"payDesc":"购买98元玩钻，会员赠送686个玩钻"},{"payId":269,"pay":298,"applePayId":"com.tingshuowan.zhibo_298yuan","diamondNumber":20860,"freeDiamondNumber":0,"payDesc":"购买298元玩钻，会员赠送2086个玩钻"},{"payId":270,"pay":518,"applePayId":"com.tingshuowan.zhibo_518yuan","diamondNumber":36260,"freeDiamondNumber":0,"payDesc":"购买518元玩钻，会员赠送3626个玩钻"},{"payId":271,"pay":1598,"applePayId":"com.tingshuowan.zhibo_1598yuan","diamondNumber":111860,"freeDiamondNumber":0,"payDesc":"买1598元玩钻，会员赠送11186个玩钻"}]
     */

    private Integer           userDiamond;
    private Integer           isShowAccount;
    private Integer           account;
    private Integer           userRestDiamond;
    private Integer           userIsFirstPay;
    private String            rechargeTitle;
    private String            rechargeRule;
    private String            vipRule;
    private List<PayListsDTO> payLists;

    public Integer getUserDiamond () {
        return userDiamond;
    }

    public void setUserDiamond (Integer userDiamond) {
        this.userDiamond = userDiamond;
    }

    public Integer getIsShowAccount () {
        return isShowAccount;
    }

    public void setIsShowAccount (Integer isShowAccount) {
        this.isShowAccount = isShowAccount;
    }

    public Integer getAccount () {
        return account;
    }

    public void setAccount (Integer account) {
        this.account = account;
    }

    public Integer getUserRestDiamond () {
        return userRestDiamond;
    }

    public void setUserRestDiamond (Integer userRestDiamond) {
        this.userRestDiamond = userRestDiamond;
    }

    public Integer getUserIsFirstPay () {
        return userIsFirstPay;
    }

    public void setUserIsFirstPay (Integer userIsFirstPay) {
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

    public List<PayListsDTO> getPayLists () {
        return payLists;
    }

    public void setPayLists (List<PayListsDTO> payLists) {
        this.payLists = payLists;
    }

    public static class PayListsDTO {
        /**
         * payId : 265
         * pay : 1
         * applePayId : com.tingshuowan.zhibo_shouci
         * diamondNumber : 70
         * freeDiamondNumber : 0
         * payDesc : 首次充值玩钻
         */

        private Integer payId;
        private Integer pay;
        private String  applePayId;
        private Integer diamondNumber;
        private Integer freeDiamondNumber;
        private String  payDesc;
        private boolean isSelect;//是否选中

        public Integer getPayId () {
            return payId;
        }

        public void setPayId (Integer payId) {
            this.payId = payId;
        }

        public Integer getPay () {
            return pay;
        }

        public void setPay (Integer pay) {
            this.pay = pay;
        }

        public String getApplePayId () {
            return applePayId;
        }

        public void setApplePayId (String applePayId) {
            this.applePayId = applePayId;
        }

        public Integer getDiamondNumber () {
            return diamondNumber;
        }

        public void setDiamondNumber (Integer diamondNumber) {
            this.diamondNumber = diamondNumber;
        }

        public Integer getFreeDiamondNumber () {
            return freeDiamondNumber;
        }

        public void setFreeDiamondNumber (Integer freeDiamondNumber) {
            this.freeDiamondNumber = freeDiamondNumber;
        }

        public String getPayDesc () {
            return payDesc;
        }

        public void setPayDesc (String payDesc) {
            this.payDesc = payDesc;
        }

        public boolean isSelect () {
            return isSelect;
        }

        public void setSelect (boolean select) {
            isSelect = select;
        }
    }
}
