package com.qichuang.retrofit

object ApiConstants {

    //正式包修改成false
    var isDebug: Boolean = false

    //是否设置代理
    var isProxy: Boolean = false

    //生产环境
    const val BASE_URL_RELEASE = "http://live.tingshuowan.com/"

    //测试环境
    const val BASE_URL_TEST = "http://livetest.tingshuowan.com/"

    //获取url
    fun fetchBaseUrl(): String {
        return if (isDebug) {
            BASE_URL_TEST
        } else {
            BASE_URL_RELEASE
        }
    }

    const val CHARSET_NAME: String = "UTF-8"

    //header
    const val USER_AGENT: String = "user-agent"
    const val ACCEPT: String = "Accept"
    const val CONTENT_TYPE: String = "Content-Type"
    const val APP_VERSION: String = "version"
    const val APP_VERSION_CODE: String = "versionCode"
    const val LIB_VERSION: String = "_lib_version"
    const val APP_OS: String = "os"
    const val TIME_STAMP: String = "timestamp"
    const val SIGNATURE: String = "signature"

    //友盟一键登录失败时,后台返回
    const val UMENG_LOGIN_ERROR_CODE = "5011"

    //第三方SDK列表
    const val THIRD_SDK_LIST = "http://live.tingshuowan.com/listen/agreement?type=7"
}
