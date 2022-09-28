package com.maishuo.tingshuohenhaowan.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.common.Constant;
import com.qichuang.commonlibs.basic.IBasicActivity;
import com.qichuang.commonlibs.utils.LogUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends IBasicActivity implements IWXAPIEventHandler {

    private static final String TAG = WXPayEntryActivity.class.getSimpleName();

    private IWXAPI api;

    private static int payCode = BaseResp.ErrCode.ERR_COMM;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wechat_pay_result_activity);

        api = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent (Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq (BaseReq req) {
        LogUtils.D(TAG, req.getType() + "");
    }

    @Override
    public void onResp (BaseResp resp) {
        EventBus.getDefault().post(resp);

        LogUtils.D(TAG, "payCode=" + payCode + " errStr=" + resp.errStr);
        switch (resp.errCode) {
            case 0:
                LogUtils.D(TAG, "成功");
                break;

            case -1:
                LogUtils.D(TAG, "失败");
                //失败
                break;
            case -2:
                LogUtils.D(TAG, "取消");
                break;
        }
        finish();
    }
}