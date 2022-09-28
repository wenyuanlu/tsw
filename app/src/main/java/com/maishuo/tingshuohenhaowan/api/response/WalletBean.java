package com.maishuo.tingshuohenhaowan.api.response;

import java.io.Serializable;


/**
 * author ：Seven
 * date : 3/16/21
 * description :钱包首页实体类
 */
public class WalletBean implements Serializable {

    /**
     * playingCoins : 44
     * money : 0.00
     * aliRealName :
     * aliAccount :
     * nickName :
     * userCouponNumber : 0
     * notUserCouponNumber : 3
     * overTimeCouponNumber : 1
     * currnetCharm : 1
     * totalCharm : 1
     * currentDiamond : 4
     * changePer : 25
     * lowCharmWithdraw : 1
     * hieghtCharmWithdraw : 500
     */
    private int       playingCoins;//当前剩余玩币
    private String    money;//当前剩余钱数
    private String    aliRealName = "";//支付宝真实姓名
    private String    aliAccount  = "";//支付宝账号
    private String    nickName    = "";// 微信昵称
    private int       userCouponNumber;//已使用的优惠券
    private int       notUserCouponNumber;//未使用的优惠券
    private int       overTimeCouponNumber;//过期的优惠券
    private Long      currnetCharm;//当前账户剩余的魅力值
    private Long      totalCharm;//用户总魅力
    private Long      currentDiamond;//当前账户剩余的玩钻
    private int       changePer;//魅力提现兑换规则
    private int       lowCharmWithdraw;//魅力的最低提现额度(元)
    private int       hieghtCharmWithdraw;//魅力的最高提现额度(元)
    private AlertBean alert;//红包活动

    public AlertBean getAlert () {
        return alert;
    }

    public void setAlert (AlertBean alert) {
        this.alert = alert;
    }

    public int getPlayingCoins () {
        return playingCoins;
    }

    public void setPlayingCoins (int playingCoins) {
        this.playingCoins = playingCoins;
    }

    public String getMoney () {
        return money;
    }

    public void setMoney (String money) {
        this.money = money;
    }

    public String getAliRealName () {
        return aliRealName;
    }

    public void setAliRealName (String aliRealName) {
        this.aliRealName = aliRealName;
    }

    public String getAliAccount () {
        return aliAccount;
    }

    public void setAliAccount (String aliAccount) {
        this.aliAccount = aliAccount;
    }

    public String getNickName () {
        return nickName;
    }

    public void setNickName (String nickName) {
        this.nickName = nickName;
    }

    public int getUserCouponNumber () {
        return userCouponNumber;
    }

    public void setUserCouponNumber (int userCouponNumber) {
        this.userCouponNumber = userCouponNumber;
    }

    public int getNotUserCouponNumber () {
        return notUserCouponNumber;
    }

    public void setNotUserCouponNumber (int notUserCouponNumber) {
        this.notUserCouponNumber = notUserCouponNumber;
    }

    public int getOverTimeCouponNumber () {
        return overTimeCouponNumber;
    }

    public void setOverTimeCouponNumber (int overTimeCouponNumber) {
        this.overTimeCouponNumber = overTimeCouponNumber;
    }

    public Long getCurrnetCharm () {
        return currnetCharm == null ? 0 : currnetCharm;
    }

    public void setCurrnetCharm (Long currnetCharm) {
        this.currnetCharm = currnetCharm;
    }

    public Long getTotalCharm () {
        return totalCharm == null ? 0 : totalCharm;
    }

    public void setTotalCharm (Long totalCharm) {
        this.totalCharm = totalCharm;
    }

    public Long getCurrentDiamond () {
        return currentDiamond == null ? 0 : currentDiamond;
    }

    public void setCurrentDiamond (Long currentDiamond) {
        this.currentDiamond = currentDiamond;
    }

    public int getChangePer () {
        return changePer;
    }

    public void setChangePer (int changePer) {
        this.changePer = changePer;
    }

    public int getLowCharmWithdraw () {
        return lowCharmWithdraw;
    }

    public void setLowCharmWithdraw (int lowCharmWithdraw) {
        this.lowCharmWithdraw = lowCharmWithdraw;
    }

    public int getHieghtCharmWithdraw () {
        return hieghtCharmWithdraw;
    }

    public void setHieghtCharmWithdraw (int hieghtCharmWithdraw) {
        this.hieghtCharmWithdraw = hieghtCharmWithdraw;
    }

    public class AlertBean implements Serializable {
        private String num;
        private String unit;
        private String gif_path;
        private String mp3_path;
        private String play_time;

        public String getNum () {
            return num;
        }

        public void setNum (String num) {
            this.num = num;
        }

        public String getUnit () {
            return unit;
        }

        public void setUnit (String unit) {
            this.unit = unit;
        }

        public String getGif_path () {
            return gif_path;
        }

        public void setGif_path (String gif_path) {
            this.gif_path = gif_path;
        }

        public String getMp3_path () {
            return mp3_path;
        }

        public void setMp3_path (String mp3_path) {
            this.mp3_path = mp3_path;
        }

        public String getPlay_time () {
            return play_time;
        }

        public void setPlay_time (String play_time) {
            this.play_time = play_time;
        }
    }
}
