package com.maishuo.tingshuohenhaowan.api.response;

/**
 * Create by yun on 2021/1/6
 * EXPLAIN:
 */
public class LiveVoiceCommentListBean {

    /**
     * id : 1399062
     * content : 听说
     * seconds : 6
     * comment_type : 1
     * gift_info : {}
     * send_user_info : {"id":1158,"uid":"27875b00908e1b554c67da6422dc1e62","uname":"小手山地犬","avatar":"http://test.tingshuowan.com/listen/path?url=/default/me_pic_boy.png","sex":0}
     */

    private int id;
    private String content;
    private int seconds;
    private int comment_type;
    private GiftInfoBean gift_info;
    private SendUserInfoBean send_user_info;

    //自己加的，用于本地处理逻辑
    private int contentCount;//计算重复内容
    private boolean isSelf;//自己发送弹幕

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getContentCount() {
        return contentCount;
    }

    public void setContentCount(int contentCount) {
        this.contentCount = contentCount;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getComment_type() {
        return comment_type;
    }

    public void setComment_type(int comment_type) {
        this.comment_type = comment_type;
    }

    public GiftInfoBean getGift_info() {
        return gift_info;
    }

    public void setGift_info(GiftInfoBean gift_info) {
        this.gift_info = gift_info;
    }

    public SendUserInfoBean getSend_user_info() {
        return send_user_info;
    }

    public void setSend_user_info(SendUserInfoBean send_user_info) {
        this.send_user_info = send_user_info;
    }

    //重写equals方法
    @Override
    public boolean equals(Object obj) {
        LiveVoiceCommentListBean bean = (LiveVoiceCommentListBean) obj;
        if(bean==null){
            return false;
        }
        return content.equals(bean.getContent());
    }

    //重写hashCode方法
    @Override
    public int hashCode() {
        String str = content;
        return str.hashCode();
    }

    public static class GiftInfoBean {
    }

    public static class SendUserInfoBean {
        /**
         * id : 1158
         * uid : 27875b00908e1b554c67da6422dc1e62
         * uname : 小手山地犬
         * avatar : http://test.tingshuowan.com/listen/path?url=/default/me_pic_boy.png
         * sex : 0
         */

        private int id;
        private String uid;
        private String uname;
        private String avatar;
        private int sex;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }
    }
}
