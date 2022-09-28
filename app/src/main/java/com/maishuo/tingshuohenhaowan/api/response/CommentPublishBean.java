package com.maishuo.tingshuohenhaowan.api.response;

public class CommentPublishBean {

    /**
     * code : 200
     * data :
     * msg : 成功插入弹幕
     */
    public Integer code;
    public String data;
    public String msg;

    public Integer getCode () {
        return code;
    }

    public void setCode (Integer code) {
        this.code = code;
    }

    public String getData () {
        return data;
    }

    public void setData (String data) {
        this.data = data;
    }

    public String getMsg () {
        return msg;
    }

    public void setMsg (String msg) {
        this.msg = msg;
    }
}
