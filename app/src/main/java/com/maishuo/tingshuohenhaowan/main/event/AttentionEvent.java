package com.maishuo.tingshuohenhaowan.main.event;

/**
 * author ：Seven
 * date : 2021/7/1
 * description :关注逻辑：
 * 首页用userId，因为一个用户可能在列表存在多条留声
 * 其它关注地方用position，更新指定item的状态
 */
public class AttentionEvent {
    /**
     * 关注状态
     * 1 未关注, 2 已关注, 3 已互粉
     */
    public int    statues;
    /**
     * 列表下标
     */
    public int    position;
    /**
     * 用户id
     */
    public String userId;

    public AttentionEvent(int statues, int position, String userId) {
        this.statues = statues;
        this.position = position;
        this.userId = userId;
    }
}
