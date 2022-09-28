package com.maishuo.tingshuohenhaowan.api.response

//获取token
data class GetTokenResponse(
        var loginStatus: Int? = null,
        var token: String? = null
)

//验证扫一扫
data class CheckAuthResponse(
        var authLogin: Int? = null
)