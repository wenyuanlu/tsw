package com.maishuo.tingshuohenhaowan.common;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils;
import com.qichuang.commonlibs.utils.PreferencesUtils;

/**
 * author ：Seven
 * date : 4/1/21
 * description :热启动埋点
 */
public class HotActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private int mFinalCount;

    @Override
    public void onActivityCreated (Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted (Activity activity) {
        mFinalCount++;
        //如果mFinalCount ==1，说明是从后台到前台
        if (mFinalCount == 1) {
            //是否跳转到了第三方应用
            if (CustomApplication.getApp().isOpen()) {
                CustomApplication.getApp().setOpen(false);
                return;
            }
            TrackingAgentUtils.setEvent("event_1");//统计激活次数
            PreferencesUtils.putLong("AppDuration", System.currentTimeMillis());//用于计算统计时长
        }
    }

    @Override
    public void onActivityResumed (Activity activity) {

    }

    @Override
    public void onActivityPaused (Activity activity) {

    }

    @Override
    public void onActivityStopped (Activity activity) {
        mFinalCount--;
        //如果mFinalCount ==0，说明是前台到后台
        if (mFinalCount == 0) {
            //是否跳转到了第三方应用
            if (CustomApplication.getApp().isOpen()) {
                return;
            }

            long duration = PreferencesUtils.getLong("AppDuration", 0);
            long value    = System.currentTimeMillis() - duration;
            TrackingAgentUtils.setAppDuration(value);
        }
    }

    @Override
    public void onActivitySaveInstanceState (Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed (Activity activity) {

    }
}
