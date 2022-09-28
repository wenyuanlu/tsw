package com.maishuo.tingshuohenhaowan.api.response;

/**
 * 获取阿里认证token
 */
public class AliRuthTokenBean {
    private String token;
    private String ticketId;

    public String getToken () {
        return token;
    }

    public void setToken (String token) {
        this.token = token;
    }

    public String getTicketId () {
        return ticketId;
    }

    public void setTicketId (String ticketId) {
        this.ticketId = ticketId;
    }
}