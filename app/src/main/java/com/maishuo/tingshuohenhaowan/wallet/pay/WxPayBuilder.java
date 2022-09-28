package com.maishuo.tingshuohenhaowan.wallet.pay;

import com.qichuang.commonlibs.utils.LogUtils;

import com.google.gson.Gson;
import com.maishuo.tingshuohenhaowan.common.CustomApplication;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

public class WxPayBuilder {

    private PayCallback mPayCallback;

    public static WxPayBuilder getInstance(String appId,PayCallback callback){
        return new WxPayBuilder(appId,callback);
    }

    private WxPayBuilder(String appId,PayCallback callback) {
        mPayCallback = new WeakReference<>(callback).get();
        WxApiWrapper.getInstance().setAppID(appId);
        EventBus.getDefault().register(this);
    }

    /**
     * 去微信授权
     */
    public void auth() {
        CustomApplication.getApp().setOpen(true);//标记是跳转到第三方
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";//应用授权作用域，如获取用户个人信息则填写snsapi_userinfo
        req.state = "tingshuowan";//用于保持请求和回调的状态
        IWXAPI wxApi = WxApiWrapper.getInstance().getWxApi();
        wxApi.sendReq(req);
    }

    /**
     * 调起微信支付
     */
    public void pay(WxPayBean.PayInfoBean bean) {
        CustomApplication.getApp().setOpen(true);//标记是跳转到第三方
        PayReq req = new PayReq();
        req.appId = bean.getAppid();
        req.partnerId = bean.getPartnerid();
        req.prepayId = bean.getPrepayid();
        req.packageValue = bean.getPackageX();
        req.nonceStr = bean.getNoncestr();
        req.timeStamp = String.valueOf(bean.getTimestamp());
        req.sign = bean.getSign();
        IWXAPI wxApi = WxApiWrapper.getInstance().getWxApi();
        wxApi.sendReq(req);
    }

    /**
     * 微信回调
     * baseResp.getType():0-分享，1-授权，5-支付
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPayResponse(BaseResp baseResp) {
        switch (baseResp.getType()) {
            case 0:
                switch (baseResp.errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        LogUtils.LOGI(this.getClass().getSimpleName(), "分享成功");
                        mPayCallback.onSuccess("分享成功");
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        LogUtils.LOGI(this.getClass().getSimpleName(), "分享取消");
                        mPayCallback.onFailed("分享取消");
                        break;
                    case BaseResp.ErrCode.ERR_AUTH_DENIED:
                        LogUtils.LOGI(this.getClass().getSimpleName(), "分享失败");
                        mPayCallback.onFailed("分享失败");
                        break;
                }
                break;
            case 1:
                SendAuth.Resp resp = (SendAuth.Resp) baseResp;
                switch (resp.errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        LogUtils.LOGI(this.getClass().getSimpleName(), "微信授权结果" + new Gson().toJson(resp));
                        mPayCallback.onSuccess(String.valueOf(resp.code));
                        break;
                    case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                        LogUtils.LOGI(this.getClass().getSimpleName(), "用户拒绝授权");
                        mPayCallback.onFailed("用户拒绝授权");
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                        LogUtils.LOGI(this.getClass().getSimpleName(), "用户取消授权");
                        mPayCallback.onFailed("用户取消授权");
                        break;
                    default:
                        LogUtils.LOGI(this.getClass().getSimpleName(), "授权失败");
                        mPayCallback.onFailed("授权失败");
                        break;
                }
                break;
            case 5:
                switch (baseResp.errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        LogUtils.D(this.getClass().getSimpleName(), "支付成功");
                        mPayCallback.onSuccess("支付成功");
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                        LogUtils.D(this.getClass().getSimpleName(), "支付取消");
                        mPayCallback.onFailed("支付取消");
                        break;
                    default:
                        LogUtils.D(this.getClass().getSimpleName(), "支付失败");
                        mPayCallback.onFailed("支付失败");
                        break;
                }
                break;

        }
        mPayCallback = null;
        EventBus.getDefault().unregister(this);
    }
}
