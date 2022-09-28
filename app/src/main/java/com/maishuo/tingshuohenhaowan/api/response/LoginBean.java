package com.maishuo.tingshuohenhaowan.api.response;

import java.io.Serializable;

/**
 * author ：yh
 * date : 2021/1/15 15:40
 * description :
 */

public class LoginBean {
    /**
     * token : fEDK4vKmqT8kXKthKvHnLemo6eL576fSrdbOimbzNs4Ya2tD2B10Dtoex2tg+mpcsm5/GWJE9X1LrpMqWTG3SbksBzz7stHA3OlFHlPIAFOcOo51IWj1Qr7NubAH07X/yGRZ/p3eW91dtkmjljzgKvsHE8xuHX8+QJTwVtB0VPskDeWwpOfxzeV/VGyeqHYnnIlEH09HgGShONSdK59AmpAc7wilZJlSLg40i1VNDi1A/ZFGaVU3My9vidMQyobHMjwUKLEat5cfKU5Psky6ZA==
     * userBasic : {"userId":"e78d9b7d1b70e2766eb5342dce1ae0e1","name":"遇见谁","avatar":"http://test.tingshuowan.com/listen/path?url=/users/e78d9b7d1b70e2766eb5342dce1ae0e1/images/2020/93f1d977c317cb63009d43717d5967e8_animation.jpg","sex":"男","vip":0,"birth":"2020-05-13","personSign":"","lastLoginTime":"2021-01-15 15:59:08","loginStatus":1,"realStatus":1,"isFirstLogin":2}
     * isEquipmentLogin : 0
     * isCheck : 0
     */

    private String        token;
    private UserBasicBean userBasic;
    private int           isEquipmentLogin;
    private int           isCheck;
    private int           isNewUser;

    public String getToken () {
        return token;
    }

    public void setToken (String token) {
        this.token = token;
    }

    public UserBasicBean getUserBasic () {
        return userBasic;
    }

    public void setUserBasic (UserBasicBean userBasic) {
        this.userBasic = userBasic;
    }

    public int getIsEquipmentLogin () {
        return isEquipmentLogin;
    }

    public void setIsEquipmentLogin (int isEquipmentLogin) {
        this.isEquipmentLogin = isEquipmentLogin;
    }

    public int getIsCheck () {
        return isCheck;
    }

    public void setIsCheck (int isCheck) {
        this.isCheck = isCheck;
    }

    public int getIsNewUser () {
        return isNewUser;
    }

    public void setIsNewUser (int isNewUser) {
        this.isNewUser = isNewUser;
    }

    public static class UserBasicBean implements Serializable {
        /**
         * userId : e78d9b7d1b70e2766eb5342dce1ae0e1
         * name : 遇见谁
         * avatar : http://test.tingshuowan.com/listen/path?url=/users/e78d9b7d1b70e2766eb5342dce1ae0e1/images/2020/93f1d977c317cb63009d43717d5967e8_animation.jpg
         * sex : 男
         * vip : 0
         * birth : 2020-05-13
         * personSign :
         * lastLoginTime : 2021-01-15 15:59:08
         * loginStatus : 1
         * realStatus : 1
         * isFirstLogin : 2
         */

        private String userId;
        private String name;
        private String avatar;
        private String sex;
        private int    vip;
        private String birth;
        private String personSign;
        private String lastLoginTime;
        private int    loginStatus;
        private int    realStatus;
        private int    isFirstLogin;

        public String getUserId () {
            return userId;
        }

        public void setUserId (String userId) {
            this.userId = userId;
        }

        public String getName () {
            return name;
        }

        public void setName (String name) {
            this.name = name;
        }

        public String getAvatar () {
            return avatar;
        }

        public void setAvatar (String avatar) {
            this.avatar = avatar;
        }

        public String getSex () {
            return sex;
        }

        public void setSex (String sex) {
            this.sex = sex;
        }

        public int getVip () {
            return vip;
        }

        public void setVip (int vip) {
            this.vip = vip;
        }

        public String getBirth () {
            return birth;
        }

        public void setBirth (String birth) {
            this.birth = birth;
        }

        public String getPersonSign () {
            return personSign;
        }

        public void setPersonSign (String personSign) {
            this.personSign = personSign;
        }

        public String getLastLoginTime () {
            return lastLoginTime;
        }

        public void setLastLoginTime (String lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
        }

        public int getLoginStatus () {
            return loginStatus;
        }

        public void setLoginStatus (int loginStatus) {
            this.loginStatus = loginStatus;
        }

        public int getRealStatus () {
            return realStatus;
        }

        public void setRealStatus (int realStatus) {
            this.realStatus = realStatus;
        }

        public int getIsFirstLogin () {
            return isFirstLogin;
        }

        public void setIsFirstLogin (int isFirstLogin) {
            this.isFirstLogin = isFirstLogin;
        }
    }

}
