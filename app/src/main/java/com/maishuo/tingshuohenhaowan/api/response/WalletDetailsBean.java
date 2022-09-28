package com.maishuo.tingshuohenhaowan.api.response;


public class WalletDetailsBean {


    /**
     * title : 充值1元
     * payDate : 07-19 12:20
     * diamond : +77钻
     */

    private String title;
    private String payDate;
    private String diamond;


    /**
     * name : 魅力提现
     * playCoinsOrMoney : +8元
     * moneyStatusDesc :
     * time : 10-17 10:33
     */

    private String name;
    private String playCoinsOrMoney;
    private String moneyStatusDesc;
    private String time;

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getPayDate () {
        return payDate;
    }

    public void setPayDate (String payDate) {
        this.payDate = payDate;
    }

    public String getDiamond () {
        return diamond;
    }

    public void setDiamond (String diamond) {
        this.diamond = diamond;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getPlayCoinsOrMoney () {
        return playCoinsOrMoney;
    }

    public void setPlayCoinsOrMoney (String playCoinsOrMoney) {
        this.playCoinsOrMoney = playCoinsOrMoney;
    }

    public String getMoneyStatusDesc () {
        return moneyStatusDesc;
    }

    public void setMoneyStatusDesc (String moneyStatusDesc) {
        this.moneyStatusDesc = moneyStatusDesc;
    }

    public String getTime () {
        return time;
    }

    public void setTime (String time) {
        this.time = time;
    }
}
