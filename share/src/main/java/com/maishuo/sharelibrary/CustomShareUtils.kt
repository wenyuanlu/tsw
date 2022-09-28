package com.maishuo.sharelibrary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import com.qichuang.commonlibs.utils.FileUtils
import com.qichuang.commonlibs.utils.LogUtils
import com.sina.weibo.sdk.api.WebpageObject
import com.sina.weibo.sdk.api.WeiboMultiMessage
import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.openapi.IWBAPI
import com.sina.weibo.sdk.openapi.WBAPIFactory
import com.tencent.connect.common.Constants
import com.tencent.connect.share.QQShare
import com.tencent.connect.share.QzoneShare
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.tauth.Tencent
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * author : xpSun
 * date : 9/17/21
 * description :
 */
class CustomShareUtils private constructor() {

    private var iwxApi: IWXAPI? = null
    private var mTencent: Tencent? = null
    private var mWBAPI: IWBAPI? = null
    private var mTargetScene: Int? = null

    private var onQQShareListener: OnQQShareListener? = null
    private var onQZoneShareListener: OnQZoneShareListener? = null
    private var onWeiBoShareCallBack: OnWeiBoShareCallBack? = null

    var onShareRequestListener: OnShareRequestListener? = null

    companion object {
        private var instance: CustomShareUtils? = null

        const val REQUEST_CODE_WECHAT_TAG: Int = 0x1001

        fun getInstance(): CustomShareUtils {
            if (null == instance) {
                instance = CustomShareUtils()
            }

            return instance!!
        }
    }

    fun builder(activity: Activity) {
        iwxApi = WXAPIFactory.createWXAPI(activity, Constant.WX_APP_ID, false)

        if (null == iwxApi) {
            LogUtils.LOGE("初始化失败===微信分享")
        }

        mTencent = Tencent.createInstance(
                Constant.QQ_APP_ID,
                activity,
                Constant.QQ_APP_AUTHORITIES)

        val authInfo = AuthInfo(
                activity,
                Constant.WB_APP_KY,
                Constant.WB_REDIRECT_URL,
                Constant.WB_SCOPE
        )

        mWBAPI = WBAPIFactory.createWBAPI(activity)
        if (null == mWBAPI) {
            LogUtils.LOGE("初始化失败===微博分享")
        } else {
            mWBAPI?.registerApp(activity, authInfo)
            mWBAPI?.setLoggerEnable(true)
        }
    }

    fun sendWeChatMessage(
            shareUrl: String?,
            title: String?,
            desc: String?,
            bitmap: Bitmap?,
            mTargetScene: Int?
    ) {
        this.mTargetScene = mTargetScene
        val wxObject = WXWebpageObject()
        wxObject.webpageUrl = shareUrl
        val msg = WXMediaMessage(wxObject)
        msg.title = title
        msg.description = desc
        msg.thumbData = FileUtils.bmpToByteArray(bitmap, false)
        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("webpage")
        req.message = msg
        req.scene = mTargetScene ?: 0

        iwxApi?.sendReq(req)
    }

    fun sendQQMessage(activity: Activity?,
                      shareUrl: String?,
                      desc: String?,
                      thumbImageUrl: String?,
                      title: String?,
                      appName: String?,
                      qqShareListener: OnQQShareListener?) {
        val params = Bundle()
        val shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl)
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, desc)

        if (!TextUtils.isEmpty(thumbImageUrl)) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, thumbImageUrl)
        }

        params.putString(QQShare.SHARE_TO_QQ_TITLE, title)
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName)
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, shareType)
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE)

        this.onQQShareListener = qqShareListener
        mTencent?.shareToQQ(activity, params, onQQShareListener)
    }

    fun sendQQZoneMessage(activity: Activity?,
                          title: String?,
                          desc: String?,
                          shareUrl: String?,
                          thumbImageUrl: String?,
                          qZoneShareListener: OnQZoneShareListener?) {
        val params = Bundle()
        val shareType = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType)
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title) //必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, desc) //选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareUrl) //必填
        params.putString(QzoneShare.SHARE_TO_QQ_IMAGE_URL, thumbImageUrl)

        this.onQZoneShareListener = qZoneShareListener
        mTencent?.shareToQzone(activity, params, onQZoneShareListener)
    }

    fun sendWeiBoMessage(activity: Activity?,
                         title: String?,
                         desc: String?,
                         bitmap: Bitmap?,
                         shareUrl: String?,
                         defaultText: String?,
                         listener: () -> Unit?) {

        if (!isAppInstalled(activity, Constant.WEI_BO_PACKAGE_NAME)) {
            listener.invoke()
            return
        }

        val message = WeiboMultiMessage()
        val webObject = WebpageObject()
        webObject.identify = UUID.randomUUID().toString()
        webObject.title = title
        webObject.description = desc

        val os = ByteArrayOutputStream()
        os.use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 85, it)
            webObject.thumbData = it.toByteArray()
        }

        webObject.actionUrl = shareUrl
        webObject.defaultText = defaultText

        message.mediaObject = webObject

        mWBAPI?.shareMessage(message, false)
    }

    fun onShareResponse(onResponse: OnActivityResultBean?) {
        if (null == onResponse) {
            return
        }

        val data: Intent? = onResponse.data
        val requestCode: Int? = onResponse.requestCode
        val resultCode: Int? = onResponse.resultCode

        if (null == onWeiBoShareCallBack) {
            onWeiBoShareCallBack = OnWeiBoShareCallBack(onShareRequestListener)
        }

        mWBAPI?.doResultIntent(data, onWeiBoShareCallBack)

        when (requestCode) {
            REQUEST_CODE_WECHAT_TAG -> {
                onShareRequestListener?.onComplete(SHARE_MEDIA.WEIXIN)
            }
            Constants.REQUEST_QQ_SHARE -> {
                Tencent.onActivityResultData(requestCode, resultCode ?: 0, data, onQQShareListener)
            }
            Constants.REQUEST_QZONE_SHARE -> {
                Tencent.onActivityResultData(requestCode, resultCode
                        ?: 0, data, onQZoneShareListener)
            }
            else -> {

            }
        }
    }

    private fun buildTransaction(type: String?): String {
        return if (type == null) {
            System.currentTimeMillis().toString()
        } else {
            type + System.currentTimeMillis()
        }
    }

    /**
     * 对应包名的App是否安装
     */
    private fun isAppInstalled(context: Context?, packageName: String?): Boolean {
        if (TextUtils.isEmpty(packageName)) {
            return false
        }

        val pm = context?.packageManager
        return try {
            pm?.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun onWechatShareResponse(baseResp: BaseResp) {
        when (baseResp.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                LogUtils.LOGE(this.javaClass.simpleName, "分享成功")

                if (mTargetScene == SendMessageToWX.Req.WXSceneSession) {
                    onShareRequestListener?.onComplete(SHARE_MEDIA.WEIXIN)
                } else {
                    onShareRequestListener?.onComplete(SHARE_MEDIA.WEIXIN_CIRCLE)
                }
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                LogUtils.LOGE(this.javaClass.simpleName, "分享取消")

                if (mTargetScene == SendMessageToWX.Req.WXSceneSession) {
                    onShareRequestListener?.onCancel(SHARE_MEDIA.WEIXIN)
                } else {
                    onShareRequestListener?.onCancel(SHARE_MEDIA.WEIXIN_CIRCLE)
                }
            }
            else -> {
                LogUtils.LOGE(this.javaClass.simpleName, "分享失败")

                if (mTargetScene == SendMessageToWX.Req.WXSceneSession) {
                    onShareRequestListener?.onError(SHARE_MEDIA.WEIXIN, Throwable(baseResp.errCode.toString()))
                } else {
                    onShareRequestListener?.onError(SHARE_MEDIA.WEIXIN_CIRCLE, Throwable(baseResp.errCode.toString()))
                }
            }
        }
    }

    fun destroy() {
        iwxApi = null
        mTencent = null
        mWBAPI = null
    }
}