package com.maishuo.tingshuohenhaowan.api.response;

/**
 * author ：yh
 * date : 2021/1/18 11:32
 * description :
 */
public final class GetVipMoneyBean {

    private String  vipData;
    private int     moneyShow;
    private int     moneyYuan;
    private boolean isSelect;
    private boolean isFirst;

    public GetVipMoneyBean (String vipData, int moneyYuan, int moneyShow, boolean isSelect, boolean isFirst) {
        this.vipData = vipData;
        this.moneyYuan = moneyYuan;
        this.moneyShow = moneyShow;
        this.isSelect = isSelect;
        this.isFirst = isFirst;
    }

    public String getVipData () {
        return vipData;
    }

    public void setVipData (String vipData) {
        this.vipData = vipData;
    }

    public int getMoneyShow () {
        return moneyShow;
    }

    public void setMoneyShow (int moneyShow) {
        this.moneyShow = moneyShow;
    }

    public int getMoneyYuan () {
        return moneyYuan;
    }

    public void setMoneyYuan (int moneyYuan) {
        this.moneyYuan = moneyYuan;
    }

    public boolean isSelect () {
        return isSelect;
    }

    public void setSelect (boolean select) {
        isSelect = select;
    }

    public boolean isFirst () {
        return isFirst;
    }

    public void setFirst (boolean first) {
        isFirst = first;
    }
}