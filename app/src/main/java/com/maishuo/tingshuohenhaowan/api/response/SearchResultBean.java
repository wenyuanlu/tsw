package com.maishuo.tingshuohenhaowan.api.response;

import java.util.List;

/**
 * author ：yh
 * date : 2021/2/7 17:09
 * description : 搜索结果
 * type=0的时候返回的数据 和 type为其他的时候返回的数据结构不一样
 */
public final class SearchResultBean {

    /**
     * type : 3
     * title : 用户
     * lists : [{"userId":"7ce140dadedf781915bb0beaf6f2bccb","userName":"狂奔的哈哈","sex":0,"userAvatar":"http://test.tingshuowan.com/listen/path?url=/default/me_pic_boy.png","fansNumber":3,"userAttentionsNumber":5,"fm":"","liveStatus":2},{"userId":"4237a920a14ceec37e5a62e42d1c52f5","userName":"臭哈哈的丝毛梗","sex":0,"userAvatar":"http://test.tingshuowan.com/listen/path?url=/default/me_pic_boy.png","fansNumber":2,"userAttentionsNumber":0,"fm":"","liveStatus":2},{"userId":"c2c7b90295faed9a55239ba2482f928b","userName":"笑哈哈的犹他盗龙","sex":0,"userAvatar":"http://test.tingshuowan.com/listen/path?url=/default/me_pic_boy.png","fansNumber":1,"userAttentionsNumber":0,"fm":"","liveStatus":2},{"userId":"4b38943265307004ab17da9166691ca1","userName":"笑哈哈的红尾伯劳","sex":0,"userAvatar":"http://test.tingshuowan.com/listen/path?url=/default/me_pic_boy.png","fansNumber":0,"userAttentionsNumber":0,"fm":"","liveStatus":2}]
     */

    private int                  type;
    private String               title;
    private List<ResultListBean> lists;

    //用户相关
    private String userId;
    private String userName;
    private int    sex;
    private String userAvatar;
    private int    fansNumber;
    private int    userAttentionsNumber;
    private String fm;
    private int    liveStatus;

    //作品相关
    private int    id;
    //private String title;
    private String desc;
    private int    praise_num;
    private int    played_num;
    private int    comment_num;
    private int    share_num;
    private int    user_id;//聊天的id
    private String created_at;
    private String image_path;
    private String voice_path;
    private String create_at_time;
    private String uname;
    private String avatar;

    public int getType () {
        return type;
    }

    public void setType (int type) {
        this.type = type;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public List<ResultListBean> getLists () {
        return lists;
    }

    public void setLists (List<ResultListBean> lists) {
        this.lists = lists;
    }

    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public String getUserName () {
        return userName;
    }

    public void setUserName (String userName) {
        this.userName = userName;
    }

    public int getSex () {
        return sex;
    }

    public void setSex (int sex) {
        this.sex = sex;
    }

    public String getUserAvatar () {
        return userAvatar;
    }

    public void setUserAvatar (String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public int getFansNumber () {
        return fansNumber;
    }

    public void setFansNumber (int fansNumber) {
        this.fansNumber = fansNumber;
    }

    public int getUserAttentionsNumber () {
        return userAttentionsNumber;
    }

    public void setUserAttentionsNumber (int userAttentionsNumber) {
        this.userAttentionsNumber = userAttentionsNumber;
    }

    public String getFm () {
        return fm;
    }

    public void setFm (String fm) {
        this.fm = fm;
    }

    public int getLiveStatus () {
        return liveStatus;
    }

    public void setLiveStatus (int liveStatus) {
        this.liveStatus = liveStatus;
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getDesc () {
        return desc;
    }

    public void setDesc (String desc) {
        this.desc = desc;
    }

    public int getPraise_num () {
        return praise_num;
    }

    public void setPraise_num (int praise_num) {
        this.praise_num = praise_num;
    }

    public int getPlayed_num () {
        return played_num;
    }

    public void setPlayed_num (int played_num) {
        this.played_num = played_num;
    }

    public int getComment_num () {
        return comment_num;
    }

    public void setComment_num (int comment_num) {
        this.comment_num = comment_num;
    }

    public int getShare_num () {
        return share_num;
    }

    public void setShare_num (int share_num) {
        this.share_num = share_num;
    }

    public int getUser_id () {
        return user_id;
    }

    public void setUser_id (int user_id) {
        this.user_id = user_id;
    }

    public String getCreated_at () {
        return created_at;
    }

    public void setCreated_at (String created_at) {
        this.created_at = created_at;
    }

    public String getImage_path () {
        return image_path;
    }

    public void setImage_path (String image_path) {
        this.image_path = image_path;
    }

    public String getVoice_path () {
        return voice_path;
    }

    public void setVoice_path (String voice_path) {
        this.voice_path = voice_path;
    }

    public String getCreate_at_time () {
        return create_at_time;
    }

    public void setCreate_at_time (String create_at_time) {
        this.create_at_time = create_at_time;
    }

    public String getUname () {
        return uname;
    }

    public void setUname (String uname) {
        this.uname = uname;
    }

    public String getAvatar () {
        return avatar;
    }

    public void setAvatar (String avatar) {
        this.avatar = avatar;
    }

    public static class ResultListBean {
        /**
         * userId : 7ce140dadedf781915bb0beaf6f2bccb
         * userName : 狂奔的哈哈
         * sex : 0
         * userAvatar : http://test.tingshuowan.com/listen/path?url=/default/me_pic_boy.png
         * fansNumber : 3
         * userAttentionsNumber : 5
         * fm :
         * liveStatus : 2
         */

        private String userId;
        private String userName;
        private int    sex;
        private String userAvatar;
        private int    fansNumber;
        private int    userAttentionsNumber;
        private String fm;
        private int    liveStatus;

        //作品相关
        private int    id;
        private String title;
        private String desc;
        private int    praise_num;
        private int    played_num;
        private int    comment_num;
        private int    share_num;
        private int    user_id;//聊天的id
        private String created_at;
        private String image_path;
        private String voice_path;
        private String create_at_time;
        private String uname;
        private String avatar;

        public String getUserId () {
            return userId;
        }

        public void setUserId (String userId) {
            this.userId = userId;
        }

        public String getUserName () {
            return userName;
        }

        public void setUserName (String userName) {
            this.userName = userName;
        }

        public int getSex () {
            return sex;
        }

        public void setSex (int sex) {
            this.sex = sex;
        }

        public String getUserAvatar () {
            return userAvatar;
        }

        public void setUserAvatar (String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public int getFansNumber () {
            return fansNumber;
        }

        public void setFansNumber (int fansNumber) {
            this.fansNumber = fansNumber;
        }

        public int getUserAttentionsNumber () {
            return userAttentionsNumber;
        }

        public void setUserAttentionsNumber (int userAttentionsNumber) {
            this.userAttentionsNumber = userAttentionsNumber;
        }

        public String getFm () {
            return fm;
        }

        public void setFm (String fm) {
            this.fm = fm;
        }

        public int getLiveStatus () {
            return liveStatus;
        }

        public void setLiveStatus (int liveStatus) {
            this.liveStatus = liveStatus;
        }

        public int getId () {
            return id;
        }

        public void setId (int id) {
            this.id = id;
        }

        public String getTitle () {
            return title;
        }

        public void setTitle (String title) {
            this.title = title;
        }

        public String getDesc () {
            return desc;
        }

        public void setDesc (String desc) {
            this.desc = desc;
        }

        public int getPraise_num () {
            return praise_num;
        }

        public void setPraise_num (int praise_num) {
            this.praise_num = praise_num;
        }

        public int getPlayed_num () {
            return played_num;
        }

        public void setPlayed_num (int played_num) {
            this.played_num = played_num;
        }

        public int getComment_num () {
            return comment_num;
        }

        public void setComment_num (int comment_num) {
            this.comment_num = comment_num;
        }

        public int getShare_num () {
            return share_num;
        }

        public void setShare_num (int share_num) {
            this.share_num = share_num;
        }

        public int getUser_id () {
            return user_id;
        }

        public void setUser_id (int user_id) {
            this.user_id = user_id;
        }

        public String getCreated_at () {
            return created_at;
        }

        public void setCreated_at (String created_at) {
            this.created_at = created_at;
        }

        public String getImage_path () {
            return image_path;
        }

        public void setImage_path (String image_path) {
            this.image_path = image_path;
        }

        public String getVoice_path () {
            return voice_path;
        }

        public void setVoice_path (String voice_path) {
            this.voice_path = voice_path;
        }

        public String getCreate_at_time () {
            return create_at_time;
        }

        public void setCreate_at_time (String create_at_time) {
            this.create_at_time = create_at_time;
        }

        public String getUname () {
            return uname;
        }

        public void setUname (String uname) {
            this.uname = uname;
        }

        public String getAvatar () {
            return avatar;
        }

        public void setAvatar (String avatar) {
            this.avatar = avatar;
        }
    }
}