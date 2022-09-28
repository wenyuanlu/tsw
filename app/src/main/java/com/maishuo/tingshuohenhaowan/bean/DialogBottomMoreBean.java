package com.maishuo.tingshuohenhaowan.bean;

/**
 * author ：yh
 * date : 2021/1/29 09:51
 * description :
 */
public class DialogBottomMoreBean {
    private int     type     = 1;//1是举报弹窗,2是播放模式弹窗,3是定时弹窗
    private String  text;
    private int     reportType;
    private boolean isSelect = false;//是否选中
    private int     time     = 0;//定时的时间

    public DialogBottomMoreBean (int reportType, String text) {
        this.reportType = reportType;
        this.text = text;
        this.type = 1;
        this.isSelect = false;
    }

    public DialogBottomMoreBean (int reportType, String text, int type) {
        this.reportType = reportType;
        this.text = text;
        this.type = type;
        this.isSelect = false;
    }

    public DialogBottomMoreBean (int reportType, String text, boolean isSelect, int type) {
        this.reportType = reportType;
        this.text = text;
        this.isSelect = isSelect;
        this.type = type;
    }

    public DialogBottomMoreBean (int reportType, String text, boolean isSelect, int time, int type) {
        this.reportType = reportType;
        this.text = text;
        this.isSelect = isSelect;
        this.time = time;
        this.type = type;
    }

    public String getText () {
        return text;
    }

    public void setText (String text) {
        this.text = text;
    }

    public int getType () {
        return type;
    }

    public void setType (int type) {
        this.type = type;
    }

    public int getReportType () {
        return reportType;
    }

    public void setReportType (int reportType) {
        this.reportType = reportType;
    }

    public boolean isSelect () {
        return isSelect;
    }

    public void setSelect (boolean select) {
        isSelect = select;
    }
}
