package com.maishuo.tingshuohenhaowan.setting

/**
 * author : xpSun
 * date : 9/13/21
 * description :
 */
enum class SettingEnum(
        val id: Int,
        val text: String,
        val desc: String
) {
    SCAN(1,"扫一扫",""),
    NOTICE_SETTING(2,"通知设置",""),
    INTEREST_SETTING(3,"兴趣设置",""),
    PLAYER_MODEL(4,"播放模式",""),
    TIMING_SETTING(5,"定时设置",""),
    REAL_NAME_AUTH(6,"实名认证",""),
    CLEAR_CACHE(7,"清除缓存",""),
    ACCOUNT_SECURITY(8,"账号安全",""),
    ABOUT_WE(9,"关于我们",""),
    FEED_BACK(10,"意见反馈",""),
}