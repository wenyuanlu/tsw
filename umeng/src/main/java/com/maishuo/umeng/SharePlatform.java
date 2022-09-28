package com.maishuo.umeng;

import android.app.Application;
import android.util.Log;

import com.maishuo.umeng.push.PushHelper;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.utils.UMUtils;

/**
 * Create by yun on 2019-08-01
 * EXPLAIN:
 */
public class SharePlatform {

    public static void init (Application application, String channelName) {
        try {
            boolean isMainProcess = UMUtils.isMainProgress(application);
            if (isMainProcess) {
                new Thread(() -> {
                    PushHelper.init(application, channelName);
                }).start();
            } else {
                PushHelper.init(application, channelName);
            }

            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

            UmLoginUtils.getInstance().initVerify(application);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(SharePlatform.class.getName(), e.toString());
        }
    }
}
