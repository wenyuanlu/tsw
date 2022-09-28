package com.maishuo.tingshuohenhaowan.wxapi

import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import com.maishuo.tingshuohenhaowan.common.Constant
import com.qichuang.commonlibs.utils.ToastUtil
import com.qichuang.commonlibs.basic.IBasicActivity
import com.qichuang.commonlibs.utils.LogUtils
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.greenrobot.eventbus.EventBus

class WXEntryActivity : IBasicActivity(), IWXAPIEventHandler {

    private var api: IWXAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, false)
        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，
        //需要判断handleIntent的返回值，如果返回值为false，
        // 则说明入参不合法未被SDK处理，应finish当前透明界面，
        // 避免外部通过传递非法参数的Intent导致停留在透明界面，
        // 引起用户的疑惑
        try {
            api?.handleIntent(intent, this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api?.handleIntent(intent, this)
    }

    /**
     * 微信发送的请求将回调到 onReq 方法
     *
     * @param req
     */
    override fun onReq(req: BaseReq) {}

    /**
     * 发送到微信请求的响应结果将回调到 onResp 方法
     *
     * @param baseResp
     */
    override fun onResp(baseResp: BaseResp) {

        //发送分享回调
        EventBus.getDefault().post(baseResp)

        //微信登录为getType为1，分享为0
        if (baseResp.type == 1) {
            val resp = baseResp as SendAuth.Resp
            when (resp.errCode) {
                BaseResp.ErrCode.ERR_OK -> LogUtils.LOGI(this.javaClass.simpleName, "微信授权结果" + Gson().toJson(resp))
                BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                    LogUtils.LOGI(this.javaClass.simpleName, "用户拒绝授权")
                    ToastUtil.showToast("用户拒绝授权")
                }
                BaseResp.ErrCode.ERR_USER_CANCEL -> {
                    LogUtils.LOGI(this.javaClass.simpleName, "用户取消登录")
                    ToastUtil.showToast("用户取消登录")
                }
                else -> {
                }
            }
        } else {
            //分享成功回调
            when (baseResp.errCode) {
                BaseResp.ErrCode.ERR_OK -> {
                    LogUtils.LOGI(this.javaClass.simpleName, "分享成功")
                }
                BaseResp.ErrCode.ERR_USER_CANCEL -> {
                    LogUtils.LOGI(this.javaClass.simpleName, "分享取消")
                }
                BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                    LogUtils.LOGI(this.javaClass.simpleName, "分享拒绝")
                }
            }
        }
        finish()
    }
}