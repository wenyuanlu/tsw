package com.maishuo.tingshuohenhaowan.utils;

import android.content.Context;
import android.text.TextUtils;

import com.reyun.tracking.sdk.Tracking;
import com.umeng.analytics.MobclickAgent;

/**
 * author : xpSun
 * date : 9/14/21
 * description : 埋点
 */
public class TrackingAgentUtils {

    private static boolean checkAgreement(){
        String agreement = CustomPreferencesUtils.fetchAgreement();
        return TextUtils.isEmpty(agreement);
    }

    //友盟统计
    public static void onEvent (Context context, String event) {
        if (checkAgreement()) {
            return;
        }

        MobclickAgent.onEvent(context, event);
    }

    //热云统计
    public static void setEvent (String event) {
        if (checkAgreement()) {
            return;
        }

        Tracking.setEvent(event);
    }

    public static void setLoginSuccessBusiness (String event) {
        if (checkAgreement()) {
            return;
        }

        Tracking.setLoginSuccessBusiness(event);
    }

    public static void setAppDuration (long event) {
        if (checkAgreement()) {
            return;
        }

        Tracking.setAppDuration(event);
    }

    public static void setRegisterWithAccountID (String event) {
        if (checkAgreement()) {
            return;
        }

        Tracking.setRegisterWithAccountID(event);
    }

    public static void setPayment (String event,String event1,String event2,float value) {
        if (checkAgreement()) {
            return;
        }

        Tracking.setPayment(event, event1, event2, value);
    }

    public static String getDeviceId(){
        return Tracking.getDeviceId();
    }

    public static void exitSdk(){
        if (checkAgreement()) {
            return;
        }

        Tracking.exitSdk();
    }

}
