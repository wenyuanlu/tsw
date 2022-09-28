package com.maishuo.tingshuohenhaowan.api.response;

import java.util.List;

/**
 * author ：yh
 * date : 2021/2/5 13:32
 * description :
 */
public class MyPersonalBean {
    /**
     * userId : e78d9b7d1b70e2766eb5342dce1ae0e1
     * uid : 10000000
     * userName : 遇见谁
     * userAvatar : http://test.tingshuowan.com/listen/path?url=/users/e78d9b7d1b70e2766eb5342dce1ae0e1/images/2020/93f1d977c317cb63009d43717d5967e8_animation.jpg
     * sex : 男
     * isVip : 0
     * phone : 7xxHWTSH4hM4QLWkIBUgcQ==
     * realStatus : 1
     * birth : 2020-05-13
     * nameStatus : 0
     * province : 上海市
     * city : 上海市
     * allPraises : 30
     * allFans : 19
     * allAttentions : 16
     * myFriend : 36
     * banner : [{"index":5,"name":"首页banner","ldp":"http://livetest.tingshuowan.com/live/Activity/aMShowAttitude?&os=2&version=1.0&versionCode=10&_lib_version=1.7.1&rand=161250303487121&loginUserId=e78d9b7d1b70e2766eb5342dce1ae0e1","icon":"http://test.tingshuowan.com/listen/path?url=/resources/202025/half16062706894617.jpeg","color":"0xFF875251"},{"index":5,"name":"appjump","ldp":"http://livetest.tingshuowan.com/test/appjump?&os=2&version=1.0&versionCode=10&_lib_version=1.7.1&rand=161250303484398&loginUserId=e78d9b7d1b70e2766eb5342dce1ae0e1","icon":"http://test.tingshuowan.com/listen/path?url=/resources/202020/half159791693918863.jpeg","color":"0xFFE49316"},{"index":5,"name":"h5后端","ldp":"http://livetest.tingshuowan.com/glory/index?loginUserId=&os=2&version=1.0&versionCode=10&_lib_version=1.7.1&rand=161250303451975&loginUserId=e78d9b7d1b70e2766eb5342dce1ae0e1","icon":"http://test.tingshuowan.com/listen/path?url=/resources/202025/half16062733304618.jpeg","color":"0xFF0F1F55"},{"index":1,"name":"首页banner2","ldp":"http://test.tingshuowan.com/listen/signing","icon":"http://test.tingshuowan.com/listen/path?url=/resources/202025/half16062707034617.jpeg","color":"0xFF875251"},{"index":5,"name":"2","ldp":"http://livetest.tingshuowan.com/test/appjump?&os=2&version=1.0&versionCode=10&_lib_version=1.7.1&rand=161250303435173&loginUserId=e78d9b7d1b70e2766eb5342dce1ae0e1","icon":"http://test.tingshuowan.com/listen/path?url=/resources/20210106/half41b181e4a27c5e19cb6121c7be5afe48.jpeg","color":"0xFF0F1F55"},{"index":5,"name":"邀请有礼测试","ldp":"http://livetest.tingshuowan.com/live/activityrecruit/index?&os=2&version=1.0&versionCode=10&_lib_version=1.7.1&rand=161250303488629&loginUserId=e78d9b7d1b70e2766eb5342dce1ae0e1","icon":"http://test.tingshuowan.com/listen/path?url=/resources/202118/half161093520110731.jpeg","color":"0xFF0F1F55"}]
     */

    private String           userId;
    private long             uid;
    private String           userName;
    private String           userAvatar;
    private String           sex;//性别
    private int              isVip;//是否vip
    private String           phone;//返回的密文
    private int              realStatus;//是否实名
    private String           birth;//生日2020-05-13
    private int              nameStatus;///昵称是否允许更改
    private String           province;
    private String           city;
    private String           allPraises;//赞数量
    private String           allFans;//粉丝数量
    private String           allAttentions;//关注数量
    private String           myFriend;//朋友数量
    private List<BannerBean> banner;

    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public long getUid () {
        return uid;
    }

    public void setUid (long uid) {
        this.uid = uid;
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

    public String getSex () {
        return sex;
    }

    public void setSex (String sex) {
        this.sex = sex;
    }

    public int getIsVip () {
        return isVip;
    }

    public void setIsVip (int isVip) {
        this.isVip = isVip;
    }

    public String getPhone () {
        return phone;
    }

    public void setPhone (String phone) {
        this.phone = phone;
    }

    public int getRealStatus () {
        return realStatus;
    }

    public void setRealStatus (int realStatus) {
        this.realStatus = realStatus;
    }

    public String getBirth () {
        return birth;
    }

    public void setBirth (String birth) {
        this.birth = birth;
    }

    public int getNameStatus () {
        return nameStatus;
    }

    public void setNameStatus (int nameStatus) {
        this.nameStatus = nameStatus;
    }

    public String getProvince () {
        return province;
    }

    public void setProvince (String province) {
        this.province = province;
    }

    public String getCity () {
        return city;
    }

    public void setCity (String city) {
        this.city = city;
    }

    public String getAllPraises () {
        return allPraises;
    }

    public void setAllPraises (String allPraises) {
        this.allPraises = allPraises;
    }

    public String getAllFans () {
        return allFans;
    }

    public void setAllFans (String allFans) {
        this.allFans = allFans;
    }

    public String getAllAttentions () {
        return allAttentions;
    }

    public void setAllAttentions (String allAttentions) {
        this.allAttentions = allAttentions;
    }

    public String getMyFriend () {
        return myFriend;
    }

    public void setMyFriend (String myFriend) {
        this.myFriend = myFriend;
    }

    public List<BannerBean> getBanner () {
        return banner;
    }

    public void setBanner (List<BannerBean> banner) {
        this.banner = banner;
    }

    public static class BannerBean {
        /**
         * index : 5
         * name : 首页banner
         * ldp : http://livetest.tingshuowan.com/live/Activity/aMShowAttitude?&os=2&version=1.0&versionCode=10&_lib_version=1.7.1&rand=161250303487121&loginUserId=e78d9b7d1b70e2766eb5342dce1ae0e1
         * icon : http://test.tingshuowan.com/listen/path?url=/resources/202025/half16062706894617.jpeg
         * color : 0xFF875251
         */
        private int    index;
        private String name;
        private String ldp;
        private String icon;
        private String color;
        private String worksId;
        private String userId;
        private String userName;

        public int getIndex () {
            return index;
        }

        public void setIndex (int index) {
            this.index = index;
        }

        public String getName () {
            return name;
        }

        public void setName (String name) {
            this.name = name;
        }

        public String getLdp () {
            return ldp;
        }

        public void setLdp (String ldp) {
            this.ldp = ldp;
        }

        public String getIcon () {
            return icon;
        }

        public void setIcon (String icon) {
            this.icon = icon;
        }

        public String getColor () {
            return color;
        }

        public void setColor (String color) {
            this.color = color;
        }

        public String getWorksId () {
            return worksId;
        }

        public void setWorksId (String worksId) {
            this.worksId = worksId;
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
    }
}
