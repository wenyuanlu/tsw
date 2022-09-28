package com.maishuo.tingshuohenhaowan.api.response;

import java.util.List;

/**
 * author ：Seven
 * date : 4/10/21
 * description :活动提现
 */
public class H5WithdrawMoneyBean {


    /**
     * EstimatedArrival_cn : 提醒好友每天登录为你解锁红包
     * NoWithdrawal_cn : 任务奖金需满10元提现
     * can_list : [10,20,30,40]
     * NoWithdrawal : 0
     * AvailableBalance : 0
     * EstimatedArrival : 0
     * WithdrawalRrecord : http://livetest.tingshuowan.com/live/activityrecruit/withdrawlist?&os=2&version=2.0.0&versionCode=200&_lib_version=1.7.1&rand=161804085973531&loginUserId=ed186a77c2dfa17cf37ab570087972e7
     */

    private String EstimatedArrival_cn;
    private String        NoWithdrawal_cn;
    private List<Integer> can_list;
    private Integer       NoWithdrawal;
    private Integer       AvailableBalance;
    private Integer       EstimatedArrival;
    private String        WithdrawalRrecord;
    private String        color;

    public String getEstimatedArrival_cn() {
        return EstimatedArrival_cn;
    }

    public void setEstimatedArrival_cn(String estimatedArrival_cn) {
        EstimatedArrival_cn = estimatedArrival_cn;
    }

    public String getNoWithdrawal_cn() {
        return NoWithdrawal_cn;
    }

    public void setNoWithdrawal_cn(String noWithdrawal_cn) {
        NoWithdrawal_cn = noWithdrawal_cn;
    }

    public List<Integer> getCan_list() {
        return can_list;
    }

    public void setCan_list(List<Integer> can_list) {
        this.can_list = can_list;
    }

    public Integer getNoWithdrawal() {
        return NoWithdrawal;
    }

    public void setNoWithdrawal(Integer noWithdrawal) {
        NoWithdrawal = noWithdrawal;
    }

    public Integer getAvailableBalance() {
        return AvailableBalance;
    }

    public void setAvailableBalance(Integer availableBalance) {
        AvailableBalance = availableBalance;
    }

    public Integer getEstimatedArrival() {
        return EstimatedArrival;
    }

    public void setEstimatedArrival(Integer estimatedArrival) {
        EstimatedArrival = estimatedArrival;
    }

    public String getWithdrawalRrecord() {
        return WithdrawalRrecord;
    }

    public void setWithdrawalRrecord(String withdrawalRrecord) {
        WithdrawalRrecord = withdrawalRrecord;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
