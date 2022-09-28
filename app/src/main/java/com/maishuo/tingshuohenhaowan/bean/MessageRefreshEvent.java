package com.maishuo.tingshuohenhaowan.bean;

/**
 * author ：yh
 * date : 2021/1/28 16:36
 * description : 聊天消息列表的刷新事件
 */
public class MessageRefreshEvent {
    public Boolean isUpdateView = false;//是否更新页面,false只更新数据,不更新界面

    public MessageRefreshEvent () {
    }

    public MessageRefreshEvent (boolean isUpdateView) {
        this.isUpdateView = isUpdateView;
    }

    public Boolean getUpdateView () {
        return isUpdateView;
    }

    public void setUpdateView (Boolean updateView) {
        isUpdateView = updateView;
    }
}
