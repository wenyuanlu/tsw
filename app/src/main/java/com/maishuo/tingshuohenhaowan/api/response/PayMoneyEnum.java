package com.maishuo.tingshuohenhaowan.api.response;

import com.maishuo.umeng.ConstantEventId;

/**
 * author ：Seven
 * date : 3/23/21
 * description :充值金额埋点类型
 */
public enum PayMoneyEnum {

    //打赏充值
    Recharge_0(ConstantEventId.NEWvoice_Recharge_1),
    Recharge_1(ConstantEventId.NEWvoice_Recharge_30),
    Recharge_2(ConstantEventId.NEWvoice_Recharge_98),
    Recharge_3(ConstantEventId.NEWvoice_Recharge_298),
    Recharge_4(ConstantEventId.NEWvoice_Recharge_518),
    Recharge_5(ConstantEventId.NEWvoice_Recharge_1598),

    //钱包充值
    Recharge_Wallet_0(ConstantEventId.NEWvoice_mine_wallet_wanzuan_1),
    Recharge_Wallet_1(ConstantEventId.NEWvoice_mine_wallet_wanzuan_30),
    Recharge_Wallet_2(ConstantEventId.NEWvoice_mine_wallet_wanzuan_98),
    Recharge_Wallet_3(ConstantEventId.NEWvoice_mine_wallet_wanzuan_298),
    Recharge_Wallet_4(ConstantEventId.NEWvoice_mine_wallet_wanzuan_518),
    Recharge_Wallet_5(ConstantEventId.NEWvoice_mine_wallet_wanzuan_1598);

    PayMoneyEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
