package com.maishuo.tingshuohenhaowan.api.response;

import java.io.Serializable;
import java.util.List;

/**
 * author ：yh
 * date : 2021/1/15 15:40
 * description :
 */
public class MessageListBean {

    /**
     * system : [{"name":"系统消息","iamge":"http://test.tingshuowan.com/listen/path?url=/default/message_system.png","number":"0","type":"system"}]
     * friend : [{"userId":"2ac59e91a5bf78a8bf70e2c36aed8c62","userIntId":10000028,"userName":"测试","userAvatar":"http://test.tingshuowan.com/listen/path?url=/users/avatar/2ac59e91a5bf78a8bf70e2c36aed8c62.jpg","sex":2,"personalSign":"犹叹当年小蛮腰，看今朝，空余恨，一身五花膘","isAttention":false,"time":"1月前","onlineTime":"30天前在线","attentionStatus":1},{"userId":"bfa1e68e13d2f0665118da6d584de09d","userIntId":10000321,"userName":"小嘴巴红尾伯劳","userAvatar":"http://test.tingshuowan.com/listen/path?url=/users/bfa1e68e13d2f0665118da6d584de09d/images/2020/1de803ed476f05a159f0a132597dab42_animation.jpg","sex":1,"personalSign":"不要对我说对不起，因为我们没关系","isAttention":true,"time":"1月前","onlineTime":"15天前在线","attentionStatus":2},{"userId":"a71ac409674771957849be392d3d8589","userIntId":10001140,"userName":"温和的西帕基犬","userAvatar":"http://test.tingshuowan.com/listen/path?url=/default/me_pic_boy.png","sex":0,"personalSign":"","isAttention":false,"time":"1月前","onlineTime":"30天前在线","attentionStatus":1},{"userId":"f28885714efff99c8ee00b7196308e78","userIntId":10000035,"userName":"刘备","userAvatar":"http://test.tingshuowan.com/listen/path?url=/users/f28885714efff99c8ee00b7196308e78/images/2020/25e34fb0c3e5d7ed63aae6afecc77c4b.jpg","sex":2,"personalSign":"孔子曰：中午不睡，下午崩溃。孟子曰：孔子说得对","isAttention":false,"time":"2月前","onlineTime":"30天前在线","attentionStatus":1},{"userId":"9b8ee6d48f5aed00d97de0d0016d5078","userIntId":10001112,"userName":"小眼睛羚羊","userAvatar":"http://test.tingshuowan.com/listen/path?url=/users/9b8ee6d48f5aed00d97de0d0016d5078/images/2020/0622410f36e32b9b91a31cd8cbc876ba.jpg","sex":2,"personalSign":"每当我吃饱喝足的时候，我就会想起来减肥这件正经事","isAttention":true,"time":"2月前","onlineTime":"2天前在线","attentionStatus":3},{"userId":"64e1d20def243350351e96f669304da6","userIntId":10000138,"userName":"12","userAvatar":"http://test.tingshuowan.com/listen/path?url=/default/me_pic_boy.png","sex":1,"personalSign":"人家有的是背景，咱有的是背影","isAttention":true,"time":"3月前","onlineTime":"30天前在线","attentionStatus":2},{"userId":"f1ad072793db41dc3ea1466e7e8cffef","userIntId":10000134,"userName":"施主请自重","userAvatar":"http://test.tingshuowan.com/listen/path?url=/users/f1ad072793db41dc3ea1466e7e8cffef/images/2020/5141b188a7528099a0a0c838168cb668_animation.jpg","sex":1,"personalSign":"唯男神与美食不可辜负","isAttention":false,"time":"3月前","onlineTime":"4天前在线","attentionStatus":1}]
     * fans_num : 22
     * follow_num : 14
     * friend_num : 32
     * StayVoicePraiseUnread : 0
     * VoiceUnReadNum : 0
     */

    private int              fans_num;
    private int              follow_num;
    private int              friend_num;
    private int              StayVoicePraiseUnread;
    private int              VoiceUnReadNum;
    private int              systemUnRedNum;
    private int              attentionNum;
    private List<SystemBean> system;
    private List<FriendBean> friend;

    public int getFans_num () {
        return fans_num;
    }

    public void setFans_num (int fans_num) {
        this.fans_num = fans_num;
    }

    public int getFollow_num () {
        return follow_num;
    }

    public void setFollow_num (int follow_num) {
        this.follow_num = follow_num;
    }

    public int getFriend_num () {
        return friend_num;
    }

    public void setFriend_num (int friend_num) {
        this.friend_num = friend_num;
    }

    public int getStayVoicePraiseUnread () {
        return StayVoicePraiseUnread;
    }

    public void setStayVoicePraiseUnread (int StayVoicePraiseUnread) {
        this.StayVoicePraiseUnread = StayVoicePraiseUnread;
    }

    public int getVoiceUnReadNum () {
        return VoiceUnReadNum;
    }

    public void setVoiceUnReadNum (int VoiceUnReadNum) {
        this.VoiceUnReadNum = VoiceUnReadNum;
    }

    public int getSystemUnRedNum () {
        return systemUnRedNum;
    }

    public void setSystemUnRedNum (int systemUnRedNum) {
        this.systemUnRedNum = systemUnRedNum;
    }

    public int getAttentionNum () {
        return attentionNum;
    }

    public void setAttentionNum (int attentionNum) {
        this.attentionNum = attentionNum;
    }

    public List<SystemBean> getSystem () {
        return system;
    }

    public void setSystem (List<SystemBean> system) {
        this.system = system;
    }

    public List<FriendBean> getFriend () {
        return friend;
    }

    public void setFriend (List<FriendBean> friend) {
        this.friend = friend;
    }

    public static class SystemBean implements Serializable {
        /**
         * name : 系统消息
         * iamge : http://test.tingshuowan.com/listen/path?url=/default/message_system.png
         * number : 0
         * type : system
         */

        private String name;
        private String iamge;
        private String number;
        private String type;

        public String getName () {
            return name;
        }

        public void setName (String name) {
            this.name = name;
        }

        public String getIamge () {
            return iamge;
        }

        public void setIamge (String iamge) {
            this.iamge = iamge;
        }

        public String getNumber () {
            return number;
        }

        public void setNumber (String number) {
            this.number = number;
        }

        public String getType () {
            return type;
        }

        public void setType (String type) {
            this.type = type;
        }
    }

    public static class FriendBean implements Serializable {
        /**
         * userId : 2ac59e91a5bf78a8bf70e2c36aed8c62
         * userIntId : 10000028
         * userName : 测试
         * userAvatar : http://test.tingshuowan.com/listen/path?url=/users/avatar/2ac59e91a5bf78a8bf70e2c36aed8c62.jpg
         * sex : 2
         * personalSign : 犹叹当年小蛮腰，看今朝，空余恨，一身五花膘
         * isAttention : false
         * time : 1月前
         * onlineTime : 30天前在线
         * attentionStatus : 1
         */

        private String  userId;
        private int     userIntId;
        private String  userName;
        private String  userAvatar;
        private int     sex;
        private String  personalSign;
        private String  time;
        private String  onlineTime;
        private Boolean isAttention;
        private int     attentionStatus = 0;    //关注状态 1 未关注, 2 已关注, 3 已互粉
        private int     unReadNum       = 0;    //未读消息的数量 本地拼接
        private String  lastReadMessage = "";   //最后一条消息 本地拼接
        private String  type            = "";   //1文本，2，image  3音频


        public String getUserId () {
            return userId;
        }

        public void setUserId (String userId) {
            this.userId = userId;
        }

        public int getUserIntId () {
            return userIntId;
        }

        public void setUserIntId (int userIntId) {
            this.userIntId = userIntId;
        }

        public String getUserName () {
            return userName;
        }

        public void setUserName (String userName) {
            this.userName = userName;
        }

        public String getUserAvatar () {
            return userAvatar;
        }

        public void setUserAvatar (String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public int getSex () {
            return sex;
        }

        public void setSex (int sex) {
            this.sex = sex;
        }

        public String getPersonalSign () {
            return personalSign;
        }

        public void setPersonalSign (String personalSign) {
            this.personalSign = personalSign;
        }

        public Boolean isIsAttention () {
            return isAttention;
        }

        public void setIsAttention (Boolean isAttention) {
            this.isAttention = isAttention;
        }

        public String getTime () {
            return time;
        }

        public void setTime (String time) {
            this.time = time;
        }

        public String getOnlineTime () {
            return onlineTime;
        }

        public void setOnlineTime (String onlineTime) {
            this.onlineTime = onlineTime;
        }

        public int getAttentionStatus () {
            return attentionStatus;
        }

        public void setAttentionStatus (int attentionStatus) {
            this.attentionStatus = attentionStatus;
        }

        public int getUnReadNum () {
            return unReadNum;
        }

        public void setUnReadNum (int unReadNum) {
            this.unReadNum = unReadNum;
        }

        public String getLastReadMessage () {
            return lastReadMessage;
        }

        public void setLastReadMessage (String lastReadMessage) {
            this.lastReadMessage = lastReadMessage;
        }

        public String getType () {
            return type;
        }

        public void setType (String type) {
            this.type = type;
        }
    }
}
