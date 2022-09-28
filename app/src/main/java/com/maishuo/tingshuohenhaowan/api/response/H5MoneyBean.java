package com.maishuo.tingshuohenhaowan.api.response;

/**
 * author ：Seven
 * date : 4/10/21
 * description :活动提现
 */
public class H5MoneyBean {

    private String  key;
    private String  view;
    private String  money;
    private boolean isSelect;//是否选中

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

}
