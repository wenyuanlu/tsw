package com.maishuo.tingshuohenhaowan.wallet.pay;

import android.text.TextUtils;

import com.maishuo.tingshuohenhaowan.common.CustomApplication;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WxApiWrapper {

    private static WxApiWrapper sInstance;
    private        IWXAPI                             mApi;
    private        String                             mAppID;

    private WxApiWrapper() {

    }

    public static WxApiWrapper getInstance() {
        if (sInstance == null) {
            synchronized (WxApiWrapper.class) {
                if (sInstance == null) {
                    sInstance = new WxApiWrapper();
                }
            }
        }
        return sInstance;
    }

    public void setAppID(String appID) {
        if (!TextUtils.isEmpty(appID)) {
            if (!appID.equals(mAppID) || mApi == null) {
                if (mApi != null) {
                    mApi.unregisterApp();
                }
                mAppID = appID;
                mApi = WXAPIFactory.createWXAPI(CustomApplication.getApp(), appID);
                mApi.registerApp(appID);
            }
        }
    }

    public String getAppID() {
        return mAppID;
    }

    public IWXAPI getWxApi() {
        return mApi;
    }

}
