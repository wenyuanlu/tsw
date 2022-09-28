package com.maishuo.sharelibrary

import com.qichuang.commonlibs.utils.LogUtils
import com.tencent.connect.common.Constants
import com.tencent.tauth.DefaultUiListener
import com.tencent.tauth.UiError

/**
 * author : xpSun
 * date : 9/18/21
 * description :
 */
class OnQZoneShareListener
constructor(private val onShareRequestListener: OnShareRequestListener?) :
        DefaultUiListener() {

    override fun onCancel() {
        onShareRequestListener?.onCancel(SHARE_MEDIA.QZONE)
    }

    override fun onError(e: UiError) {
        onShareRequestListener?.onError(SHARE_MEDIA.QZONE, Throwable(e.errorCode.toString()))
    }

    override fun onComplete(response: Any?) {
        onShareRequestListener?.onComplete(SHARE_MEDIA.QZONE)
    }

    override fun onWarning(code: Int) {
        if (code == Constants.ERROR_NO_AUTHORITY) {
            LogUtils.LOGE("onWarning: 请授权手Q访问分享的文件的读取权限!")
        }
    }
}