package com.maishuo.tingshuohenhaowan.api.response;

/**
 * 获赞的兴趣设置的数据
 */
public class InterestBean {

    private int     typeId;
    private String  name;
    private boolean isChoice;

    public int getTypeId () {
        return typeId;
    }

    public void setTypeId (int typeId) {
        this.typeId = typeId;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public boolean isChoice () {
        return isChoice;
    }

    public void setChoice (boolean choice) {
        isChoice = choice;
    }
}