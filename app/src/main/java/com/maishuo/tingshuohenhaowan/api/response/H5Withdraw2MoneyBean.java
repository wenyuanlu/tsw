package com.maishuo.tingshuohenhaowan.api.response;

import java.util.List;

/**
 * author ：Seven
 * date : 6/1/21
 * description :活动统一提现
 */
public class H5Withdraw2MoneyBean {

    private String        can_withdrawal;//可提现金额
    private String        can_withdrawal_cn;//可提现金额说明
    private int           estimate_flag;//预计到账状态0-不展示，1-展示
    private String        estimate_name;//预计到账文案
    private String        estimate_withdrawal_cn;//预估到账说明
    private String        estimate_Withdrawal;//预计提现金额
    private int           no_flag;//暂不可提现0-不展示，1-展示
    private String        no_name;//不可提现文案
    private String        no_withdrawal_cn;//不可提现说明
    private String        no_withdrawal;//不可提现金额
    private List<H5MoneyBean> can_list;//选择提现金额列表
    private String        color;//主题色

    public String getCan_withdrawal() {
        return can_withdrawal;
    }

    public void setCan_withdrawal(String can_withdrawal) {
        this.can_withdrawal = can_withdrawal;
    }

    public String getCan_withdrawal_cn() {
        return can_withdrawal_cn;
    }

    public void setCan_withdrawal_cn(String can_withdrawal_cn) {
        this.can_withdrawal_cn = can_withdrawal_cn;
    }

    public int getEstimate_flag() {
        return estimate_flag;
    }

    public void setEstimate_flag(int estimate_flag) {
        this.estimate_flag = estimate_flag;
    }

    public String getEstimate_name() {
        return estimate_name;
    }

    public void setEstimate_name(String estimate_name) {
        this.estimate_name = estimate_name;
    }

    public String getEstimate_withdrawal_cn() {
        return estimate_withdrawal_cn;
    }

    public void setEstimate_withdrawal_cn(String estimate_withdrawal_cn) {
        this.estimate_withdrawal_cn = estimate_withdrawal_cn;
    }

    public String getEstimate_Withdrawal() {
        return estimate_Withdrawal;
    }

    public void setEstimate_Withdrawal(String estimate_Withdrawal) {
        this.estimate_Withdrawal = estimate_Withdrawal;
    }

    public int getNo_flag() {
        return no_flag;
    }

    public void setNo_flag(int no_flag) {
        this.no_flag = no_flag;
    }

    public String getNo_name() {
        return no_name;
    }

    public void setNo_name(String no_name) {
        this.no_name = no_name;
    }

    public String getNo_withdrawal_cn() {
        return no_withdrawal_cn;
    }

    public void setNo_withdrawal_cn(String no_withdrawal_cn) {
        this.no_withdrawal_cn = no_withdrawal_cn;
    }

    public String getNo_withdrawal() {
        return no_withdrawal;
    }

    public void setNo_withdrawal(String no_withdrawal) {
        this.no_withdrawal = no_withdrawal;
    }

    public List<H5MoneyBean> getCan_list() {
        return can_list;
    }

    public void setCan_list(List<H5MoneyBean> can_list) {
        this.can_list = can_list;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
