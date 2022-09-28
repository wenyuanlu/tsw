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
class OnQQShareListener
constructor(private val onShareRequestListener: OnShareRequestListener?) :
        DefaultUiListener() {

    override fun onComplete(p0: Any?) {
        super.onComplete(p0)

        onShareRequestListener?.onComplete(SHARE_MEDIA.QQ)
    }

    override fun onError(e: UiError?) {
        super.onError(e)

        onShareRequestListener?.onError(SHARE_MEDIA.QQ, Throwable(e?.errorCode.toString()))
    }

    override fun onCancel() {
        super.onCancel()

        onShareRequestListener?.onCancel(SHARE_MEDIA.QQ)
    }

    override fun onWarning(code: Int) {
        super.onWarning(code)

        if (code == Constants.ERROR_NO_AUTHORITY) {
            LogUtils.LOGE("onWarning: 请授权手Q访问分享的文件的读取权限!")
        }
    }
}