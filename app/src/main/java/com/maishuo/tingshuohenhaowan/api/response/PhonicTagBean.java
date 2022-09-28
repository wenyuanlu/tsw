package com.maishuo.tingshuohenhaowan.api.response;

/**
 * Create by yun on 2020/12/30
 * EXPLAIN:
 */
public class PhonicTagBean {

    /**
     * id : 0
     * name : 全部
     */

    private int     id;
    private String  name;
    private boolean select;
    private int     tabPosition;

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public boolean isSelect () {
        return select;
    }

    public void setSelect (boolean select) {
        this.select = select;
    }

    public int getTabPosition () {
        return tabPosition;
    }

    public void setTabPosition (int tabPosition) {
        this.tabPosition = tabPosition;
    }
}
