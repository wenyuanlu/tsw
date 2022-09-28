package com.maishuo.sharelibrary

import com.qichuang.commonlibs.utils.LogUtils
import com.sina.weibo.sdk.common.UiError
import com.sina.weibo.sdk.share.WbShareCallback

/**
 * author : xpSun
 * date : 9/18/21
 * description :
 */
class OnWeiBoShareCallBack constructor(private val onShareRequestListener: OnShareRequestListener?) : WbShareCallback {

    override fun onComplete() {
        onShareRequestListener?.onComplete(SHARE_MEDIA.SINA)
    }

    override fun onError(uiError: UiError?) {
        LogUtils.LOGE(this.javaClass.simpleName, "分享失败sina" + uiError?.errorCode)

        onShareRequestListener?.onError(SHARE_MEDIA.SINA, Throwable(uiError?.errorCode.toString()))
    }

    override fun onCancel() {
        LogUtils.LOGE(this.javaClass.simpleName, "分享取消sina")

        onShareRequestListener?.onCancel(SHARE_MEDIA.SINA)
    }

}