package com.qichuang.retrofit

import com.qichuang.bean.CommonRetrofitBaseJsonResponse
import com.qichuang.commonlibs.utils.GsonUtils
import com.qichuang.commonlibs.utils.LoggerUtils
import okhttp3.logging.HttpLoggingInterceptor

/**
 * 自定义网络请求打印日志
 */
class CommonHttpLogger : HttpLoggingInterceptor.Logger {

    override fun log(message: String) {
        if (BuildConfig.DEBUG) {
            try {
                val gson = GsonUtils.fetchGson()
                val responseBean: CommonRetrofitBaseJsonResponse = gson.fromJson(message, CommonRetrofitBaseJsonResponse::class.java)
                LoggerUtils.e(gson.toJson(responseBean))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}