package com.maishuo.tingshuohenhaowan.api.response;

/**
 * 设置界面的数据
 */
public class SettingBean {
    private String  title;
    private String  hint;
    private boolean haveHint;

    public SettingBean (String title) {
        this.title = title;
        this.hint = "";
        this.haveHint = false;
    }

    public SettingBean (String title, String hint) {
        this.title = title;
        this.hint = hint;
        this.haveHint = true;
    }

    public SettingBean (String title, String hint, boolean haveHint) {
        this.title = title;
        this.hint = hint;
        this.haveHint = haveHint;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getHint () {
        return hint;
    }

    public void setHint (String hint) {
        this.hint = hint;
    }

    public boolean isHaveHint () {
        return haveHint;
    }

    public void setHaveHint (boolean haveHint) {
        this.haveHint = haveHint;
    }
}