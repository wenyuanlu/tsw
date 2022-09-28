package com.qichuang.bean

import com.qichuang.commonlibs.basic.CustomBasicApplication
import com.qichuang.commonlibs.common.PreferencesKey
import com.qichuang.commonlibs.utils.ChannelUtil
import com.qichuang.commonlibs.utils.PhoneUtils
import com.qichuang.commonlibs.utils.PreferencesUtils

open class BasicResponse<T>(
        var data: T? = null,
        var msg: String? = null,
        var code: String? = null
)

class CommonRetrofitBaseJsonResponse : BasicResponse<Any>()

open class CommonParam {
    var storeId: Int? = ChannelUtil.getInstance().channelId
    var oaid: String? = PreferencesUtils.getString(PreferencesKey.OAID, "")
    var imei: String? = PhoneUtils.getIMEI(CustomBasicApplication.instance)
    var mac: String? = PhoneUtils.getMac(CustomBasicApplication.instance)
    var token: String? = PreferencesUtils.getString(PreferencesKey.TOKEN, "")
    var androidId: String? = PhoneUtils.getDeviceToken(CustomBasicApplication.instance)
    var longitude: Double? = 0.0
    var latitude: Double? = 0.0
}

abstract class CustomBasicParam : CommonParam()