package com.maishuo.tingshuohenhaowan.wallet.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.qichuang.commonlibs.utils.LogUtils;

import com.alipay.sdk.app.PayTask;
import com.maishuo.tingshuohenhaowan.common.CustomApplication;

import java.lang.ref.WeakReference;
import java.util.Map;

public class AliPayBuilder {

    private Activity   mActivity;
    private PayHandler mPayHandler;

    public static AliPayBuilder getInstance(Activity activity,PayCallback callback){
        return new AliPayBuilder(activity,callback);
    }

    private AliPayBuilder(Activity activity,PayCallback callback) {
        mActivity = new WeakReference<>(activity).get();
        mPayHandler = new PayHandler(callback);
    }

    /**
     * 调用支付宝sdk
     */
    public void pay(String orderParams) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CustomApplication.getApp().setOpen(true);//标记是跳转到第三方
                PayTask aliPay = new PayTask(mActivity);
                //执行支付，这是一个耗时操作，最后返回支付的结果，用handler发送到主线程
                LogUtils.LOGE(this.getClass().getSimpleName(), "调起支付宝参数----->" + orderParams);
                Map<String, String> result = aliPay.payV2(orderParams, true);
                LogUtils.LOGE(this.getClass().getSimpleName(), "支付宝返回结果----->" + result);
                if (mPayHandler != null) {
                    Message msg = Message.obtain();
                    msg.obj = result;
                    mPayHandler.sendMessage(msg);
                }
            }
        }).start();
    }


    private static class PayHandler extends Handler {

        private PayCallback mPayCallback;

        public PayHandler(PayCallback payCallback) {
            mPayCallback = new WeakReference<>(payCallback).get();
        }

        @Override
        public void handleMessage(Message msg) {
            if (mPayCallback != null) {
                @SuppressWarnings("unchecked")
                Map<String, String> result = (Map<String, String>) msg.obj;
                AliPayResult aliPayResult = new AliPayResult(result);
                String       resultStatus = aliPayResult.getResultStatus();
                if (TextUtils.equals(resultStatus, "9000")) {
                    LogUtils.LOGE(this.getClass().getSimpleName(), aliPayResult.getMemo());
                    mPayCallback.onSuccess("支付成功");
                } else {
                    LogUtils.LOGE(this.getClass().getSimpleName(), "状态码" + resultStatus + " " + aliPayResult.getMemo());
                    mPayCallback.onFailed(aliPayResult.getMemo());
                }
            }
            mPayCallback = null;
        }
    }
}
