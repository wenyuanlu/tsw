package com.maishuo.tingshuohenhaowan.service

import android.app.IntentService
import android.content.Intent
import com.maishuo.tingshuohenhaowan.common.Constant
import com.maishuo.tingshuohenhaowan.common.CustomApplication
import com.maishuo.tingshuohenhaowan.utils.CustomPreferencesUtils
import com.maishuo.umeng.SharePlatform
import com.qichuang.commonlibs.utils.ChannelUtil
import com.qichuang.commonlibs.utils.PreferencesUtils
import com.qichuang.retrofit.ApiConstants
import com.reyun.tracking.sdk.Tracking
import com.tencent.bugly.crashreport.CrashReport

/**
 * author : xpSun
 * date : 9/6/21
 *description :
 */
class InitService : IntentService("") {

    override fun onHandleIntent(intent: Intent?) {
        try {
            PreferencesUtils.putString(
                CustomPreferencesUtils.APP_INIT_UM_KEY,
                CustomPreferencesUtils.APP_INIT_UM_KEY
            )

            //获取渠道名称
            val channelName = ChannelUtil.getInstance().channelName

            //初始化热云
            Tracking.setDebugMode(ApiConstants.isDebug)

            //热云平台是这样定义的
            Tracking.initWithKeyAndChannelId(
                CustomApplication.getApp(),
                Constant.HOT_CLOUD_KEY, String.format("_%s_", channelName)
            )

            //区分正式和debug环境，引用不同bugly
            if (ApiConstants.isDebug) {
                CrashReport.initCrashReport(applicationContext, Constant.BUGLY_TEST_KEY, false)
            } else {
                CrashReport.initCrashReport(applicationContext, Constant.BUGLY_RELEASE_KEY, false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}