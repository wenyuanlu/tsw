package com.maishuo.tingshuohenhaowan.main.event;

/**
 * author ：Seven
 * date : 2021/8/17
 * description :
 *
 */
public class UpdatePersonalInfoEvent {
    /**
     * 用户id
     */
    public String userId;

    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public UpdatePersonalInfoEvent (String userId) {
        this.userId = userId;
    }
}
