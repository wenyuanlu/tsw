package com.maishuo.tingshuohenhaowan.api.response;

import com.maishuo.tingshuohenhaowan.api.response.indexinit.Ad20210409;

import java.io.Serializable;

/**
 * author ：yh
 * date : 2021/1/18 11:32
 * description :
 */
public final class FirstInitBean {


    /**
     * layerAd : {"index":5,"name":"外网投放","desc":"","type":0,"deeplinkType":1,"image":"http://test.tingshuowan.com/listen/path?url=/resources/20210106/small6f1a60d530c7124e55be3d44348c1869.jpeg","ldp":"https://v.qq.com/x/cover/mzc00200x9fxrc9.html?&os=2&version=1.0&versionCode=10&_lib_version=1.7.1&rand=161094223723158&loginUserId=e78d9b7d1b70e2766eb5342dce1ae0e1","icon":"http://test.tingshuowan.com/listen/path?url=/resources/20210106/small6f1a60d530c7124e55be3d44348c1869.jpeg","color":"0xFF4322"}
     * latestVersion : {"versionCode":63,"versionName":"1.6.3","packagename":"com.maishuo.tingshuohenhaowan","size":"60876","title":"版本更新","content":"检测到新版本1.6.3","apkUrl":"itms-apps://itunes.apple.com/us/app/id1481432696","isForce":0,"playAd":true,"store_id":1}
     * extToken : {"loginStatus":1,"token":"Jmr+CT2WUX/O8Bwb1/mo5HZxSMAnoW3/uoICtBFvPlOVIB4Q0I3W2HcvGabNYcJ1gntgQ8ZEAI0tMhRUbvXItivle0bDizhaE+gXjH4AujiNc2lwEaVCJxgLxMIE2UIAaPKZUC+UrfkSknXMn8iZ8o0NBJl5rNzrw9oSFKzQ0qzgN2aQssqMX4G5HWzxBao46oYRDe86kJN8aJiNRrm50bXb4eL1ozMN30pHKTtBQXAVVo0Si38GlYKqlaLuAczTSm5/P5Lv/0B2yxGIY/qumw=="}
     * sysEnv : {"isCheck":0,"scanType":1,"pc_upload_introduce":"http://live.tingshuowan.com/HTML/pc_upload_introduce.html","mobile_upload_introduce":"http://live.tingshuowan.com/HTML/mobile_upload_introduce.html"}
     * initConfig : {"StayVoicePraiseUnread":0,"VoiceUnReadNum":0}
     */

    private LayerAdBean       layerAd;
    private LatestVersionBean latestVersion;
    private ExtTokenBean      extToken;
    private SysEnvBean        sysEnv;
    private InitConfigBean    initConfig;

    public LayerAdBean getLayerAd () {
        return layerAd;
    }

    public void setLayerAd (LayerAdBean layerAd) {
        this.layerAd = layerAd;
    }

    public LatestVersionBean getLatestVersion () {
        return latestVersion;
    }

    public void setLatestVersion (LatestVersionBean latestVersion) {
        this.latestVersion = latestVersion;
    }

    public ExtTokenBean getExtToken () {
        return extToken;
    }

    public void setExtToken (ExtTokenBean extToken) {
        this.extToken = extToken;
    }

    public SysEnvBean getSysEnv () {
        return sysEnv;
    }

    public void setSysEnv (SysEnvBean sysEnv) {
        this.sysEnv = sysEnv;
    }

    public InitConfigBean getInitConfig () {
        return initConfig;
    }

    public void setInitConfig (InitConfigBean initConfig) {
        this.initConfig = initConfig;
    }

    public static class LayerAdBean implements Serializable {
        /**
         * index : 5
         * name : 外网投放
         * desc :
         * type : 0
         * deeplinkType : 1
         * image : http://test.tingshuowan.com/listen/path?url=/resources/20210106/small6f1a60d530c7124e55be3d44348c1869.jpeg
         * ldp : https://v.qq.com/x/cover/mzc00200x9fxrc9.html?&os=2&version=1.0&versionCode=10&_lib_version=1.7.1&rand=161094223723158&loginUserId=e78d9b7d1b70e2766eb5342dce1ae0e1
         * icon : http://test.tingshuowan.com/listen/path?url=/resources/20210106/small6f1a60d530c7124e55be3d44348c1869.jpeg
         * color : 0xFF4322
         */

        private int    index;
        private String name;
        private String desc;
        private int    type;
        private int    deeplinkType;
        private String image;
        private String ldp;
        private String icon;
        private String color;

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

        public String getDesc () {
            return desc;
        }

        public void setDesc (String desc) {
            this.desc = desc;
        }

        public int getType () {
            return type;
        }

        public void setType (int type) {
            this.type = type;
        }

        public int getDeeplinkType () {
            return deeplinkType;
        }

        public void setDeeplinkType (int deeplinkType) {
            this.deeplinkType = deeplinkType;
        }

        public String getImage () {
            return image;
        }

        public void setImage (String image) {
            this.image = image;
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
    }

    public static class LatestVersionBean implements Serializable {
        /**
         * versionCode : 63
         * versionName : 1.6.3
         * packagename : com.maishuo.tingshuohenhaowan
         * size : 60876
         * title : 版本更新
         * content : 检测到新版本1.6.3
         * apkUrl : itms-apps://itunes.apple.com/us/app/id1481432696
         * isForce : 0
         * playAd : true
         * store_id : 1
         */

        private int     versionCode;
        private String  versionName;
        private String  packagename;
        private String  size;
        private String  title;
        private String  content;
        private String  apkUrl;
        private int     isForce;
        private Boolean playAd;
        private int     store_id;

        public int getVersionCode () {
            return versionCode;
        }

        public void setVersionCode (int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName () {
            return versionName;
        }

        public void setVersionName (String versionName) {
            this.versionName = versionName;
        }

        public String getPackagename () {
            return packagename;
        }

        public void setPackagename (String packagename) {
            this.packagename = packagename;
        }

        public String getSize () {
            return size;
        }

        public void setSize (String size) {
            this.size = size;
        }

        public String getTitle () {
            return title;
        }

        public void setTitle (String title) {
            this.title = title;
        }

        public String getContent () {
            return content;
        }

        public void setContent (String content) {
            this.content = content;
        }

        public String getApkUrl () {
            return apkUrl;
        }

        public void setApkUrl (String apkUrl) {
            this.apkUrl = apkUrl;
        }

        public int getIsForce () {
            return isForce;
        }

        public void setIsForce (int isForce) {
            this.isForce = isForce;
        }

        public Boolean isPlayAd () {
            return playAd;
        }

        public void setPlayAd (Boolean playAd) {
            this.playAd = playAd;
        }

        public int getStore_id () {
            return store_id;
        }

        public void setStore_id (int store_id) {
            this.store_id = store_id;
        }
    }

    public static class ExtTokenBean implements Serializable {
        /**
         * loginStatus : 1
         * token : Jmr+CT2WUX/O8Bwb1/mo5HZxSMAnoW3/uoICtBFvPlOVIB4Q0I3W2HcvGabNYcJ1gntgQ8ZEAI0tMhRUbvXItivle0bDizhaE+gXjH4AujiNc2lwEaVCJxgLxMIE2UIAaPKZUC+UrfkSknXMn8iZ8o0NBJl5rNzrw9oSFKzQ0qzgN2aQssqMX4G5HWzxBao46oYRDe86kJN8aJiNRrm50bXb4eL1ozMN30pHKTtBQXAVVo0Si38GlYKqlaLuAczTSm5/P5Lv/0B2yxGIY/qumw==
         */

        private int    loginStatus;
        private String token;

        public int getLoginStatus () {
            return loginStatus;
        }

        public void setLoginStatus (int loginStatus) {
            this.loginStatus = loginStatus;
        }

        public String getToken () {
            return token;
        }

        public void setToken (String token) {
            this.token = token;
        }
    }

    public static class SysEnvBean implements Serializable {
        /**
         * isCheck : 0
         * scanType : 1
         * pc_upload_introduce : http://live.tingshuowan.com/HTML/pc_upload_introduce.html
         * mobile_upload_introduce : http://live.tingshuowan.com/HTML/mobile_upload_introduce.html
         */

        private int    isCheck;
        private int    scanType;
        private String pc_upload_introduce;
        private String mobile_upload_introduce;

        public int getIsCheck () {
            return isCheck;
        }

        public void setIsCheck (int isCheck) {
            this.isCheck = isCheck;
        }

        public int getScanType () {
            return scanType;
        }

        public void setScanType (int scanType) {
            this.scanType = scanType;
        }

        public String getPc_upload_introduce () {
            return pc_upload_introduce;
        }

        public void setPc_upload_introduce (String pc_upload_introduce) {
            this.pc_upload_introduce = pc_upload_introduce;
        }

        public String getMobile_upload_introduce () {
            return mobile_upload_introduce;
        }

        public void setMobile_upload_introduce (String mobile_upload_introduce) {
            this.mobile_upload_introduce = mobile_upload_introduce;
        }
    }

    public static class InitConfigBean implements Serializable {
        /**
         * StayVoicePraiseUnread : 0
         * VoiceUnReadNum : 0
         */

        private int StayVoicePraiseUnread;
        private int VoiceUnReadNum;
        private int systemUnRedNum;
        private int        attentionNum;
        private Ad20210409 ad20210409;
        private String complaint_phone;

        public String getComplaint_phone () {
            return complaint_phone;
        }

        public void setComplaint_phone (String complaint_phone) {
            this.complaint_phone = complaint_phone;
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

        public Ad20210409 getAd20210409() {
            return ad20210409;
        }

        public void setAd20210409(Ad20210409 ad20210409) {
            this.ad20210409 = ad20210409;
        }
    }
}