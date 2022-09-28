package com.maishuo.tingshuohenhaowan.api.response;

/**
 * author ：Seven
 * date : 2021/4/2
 * description :
 */

public class SendMessageBean {
    private int           status;
    private int           isNewUser;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsNewUser() {
        return isNewUser;
    }

    public void setIsNewUser(int isNewUser) {
        this.isNewUser = isNewUser;
    }
}
