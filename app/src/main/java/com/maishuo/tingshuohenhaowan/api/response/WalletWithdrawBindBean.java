package com.maishuo.tingshuohenhaowan.api.response;

import java.io.Serializable;

/**
 * author ：Seven
 * date : 3/19/21
 * description :钱包提现绑定支付宝和微信
 */
public class WalletWithdrawBindBean implements Serializable {


    /**
     * aliAccount : 287879087234
     * aliRealName : 支付宝真实姓名
     * nickName : 微信昵称
     */

    private String aliAccount;
    private String aliRealName;
    private String nickName;

    public String getAliAccount () {
        return aliAccount;
    }

    public void setAliAccount (String aliAccount) {
        this.aliAccount = aliAccount;
    }

    public String getAliRealName () {
        return aliRealName;
    }

    public void setAliRealName (String aliRealName) {
        this.aliRealName = aliRealName;
    }

    public String getNickName () {
        return nickName;
    }

    public void setNickName (String nickName) {
        this.nickName = nickName;
    }
}
