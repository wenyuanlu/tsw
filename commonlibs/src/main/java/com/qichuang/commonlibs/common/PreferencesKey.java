package com.qichuang.commonlibs.common;

/**
 * Create by yun on 2020/12/10
 * EXPLAIN:
 */
public class PreferencesKey {
    public static final String UUID             = "ts_uuid";
    public static final String TOKEN            = "ts_token";
    public final static String USER_ID          = "ts_user_id";//用户userId
    public final static String USER_UID         = "ts_user_uid";//用户聊天登录的uid
    public final static String ONLINE           = "ts_online";//是否登录
    public final static String USER_NAME        = "ts_user_name";//用户名
    public final static String USER_AVATOR      = "ts_user_avator";//用户头像
    public final static String USER_TEMP_AVATOR = "user_temp_avator";//用户头像
    public final static String USER_PHONE       = "ts_user_phone";//用户手机号
    public final static String AUTH_STATUS      = "ts_auth_status";//实名
    public final static String VIP              = "ts_vip";//vip
    public final static String PHONE            = "ts_phone";//手机
    public final static String BIRTH_DAY        = "ts_birth_day";//生日
    public final static String SEX              = "ts_sex";//性别
    public final static String CITY             = "ts_name_city";//城市
    public final static String PROVINCE         = "ts_name_province";//省份
    public final static String IS_SHOW_GUIDE    = "ts_is_show_guide";
    public final static String PHONIC_GUIDE     = "ts_phonic_guide";
    public final static String ACTIVITY_DIALOG  = "ts_activity_dialog";
    public static final String OAID             = "ts_oaid";//oaid
    public static final String PLAY_TYPE        = "ts_play_type";//播放模式,0是随机播放,1是循环播放,默认随机播放0
    public static final String TIMING           = "ts_timing";//定时播放的时间
    public static final String LOCK_BG_IMAGE    = "lock_bg_image";//锁屏页背景
    public static final String LOCK_IMAGE       = "lock_image";//锁屏页头像
    public static final String LOCK_DESC        = "lock_desc";//锁屏页描述
    public static final String ENABLE_AD        = "enable_ad";//0-不启用广告，1-启用广告
    public static final String PRIVACY_SETTING_AD        = "privacy_setting_ad";//个性化广告推荐
    public static final String PRIVACY_SETTING_CONTENT        = "privacy_setting_content";//个性化内容推荐

    //定位权限申请
    public static final String REQUEST_PERMISSION_LOCATION = "request_permission_location";
}
