package com.maishuo.umeng;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.umeng.umverify.UMVerifyHelper;
import com.umeng.umverify.listener.UMPreLoginResultListener;
import com.umeng.umverify.listener.UMTokenResultListener;
import com.umeng.umverify.view.UMAuthRegisterViewConfig;
import com.umeng.umverify.view.UMAuthUIConfig;

import java.util.List;
import java.util.Map;

/**
 * author : xpSun
 * date : 2021/4/15
 * description : 友盟一键登录
 */
public class UmLoginUtils {

    private static       UmLoginUtils             instance;
    private              UMVerifyHelper           umVerifyHelper;
    private              ImageView                switchTV;
    private              UMTokenResultListener    umTokenResultListener;
    private              UMPreLoginResultListener umPreLoginResultListener;
    private static final String                   AUTH_SDK_INFO = "gIj6li0Z6cDQqhq475uxs3VjXuaEyHzy10FFxG/PBQRllh/JfC+GyCZjNh49BlMF13e7OJ/R35cpD3HDJNKRO9Gx7X8IOVEvsd8JTtx337AzZpjwgZYPRjsk4lXNoUFIlDycpQxlyZxIKNC/kq+aUBl0034V65Dlj2pxH1n3OIIcyISUi1xQF2ZiC8xQbGZmP2t1rRcoVHxB2k4TzPdM4FrGems6mECJWG8gNd4XahiHucku7/+s02EkJOKN6ymKbPvC06s0+ypFgin0q6SEQFL2ocJeV3FDbMLIPDoMzSk=";

    public static UmLoginUtils getInstance () {
        if (null == instance) {
            instance = new UmLoginUtils();
        }
        return instance;
    }

    public void setUmTokenResultListener (UMTokenResultListener umTokenResultListener) {
        this.umTokenResultListener = umTokenResultListener;
    }

    public void setUmPreLoginResultListener (UMPreLoginResultListener umPreLoginResultListener) {
        this.umPreLoginResultListener = umPreLoginResultListener;
    }

    public void initVerify (Context context) {
        //1.获取认证实例
        umVerifyHelper = UMVerifyHelper.getInstance(context, new UMTokenResultListener() {
            @Override
            public void onTokenSuccess (String s) {
                if (null != umTokenResultListener) {
                    umTokenResultListener.onTokenSuccess(s);
                }
            }

            @Override
            public void onTokenFailed (String s) {
                if (null != umTokenResultListener) {
                    umTokenResultListener.onTokenFailed(s);
                }
            }
        });

        //2.设置SDK秘钥信息
        umVerifyHelper.setAuthSDKInfo(AUTH_SDK_INFO);

        //3.检查终端是否支持号码认证1:本机号码校验 2：一键登录
        umVerifyHelper.checkEnvAvailable(2);

        //4.关键日志开关
        umVerifyHelper.setLoggerEnable(false);

        //5.本机号码认证获取token
        umVerifyHelper.getVerifyToken(5 * 1000);

        //6.一键登录预取号
        umVerifyHelper.accelerateLoginPage(5 * 1000,
                new UMPreLoginResultListener() {
                    @Override
                    public void onTokenSuccess (String s) {
                        if (null != umPreLoginResultListener) {
                            umPreLoginResultListener.onTokenSuccess(s);
                        }
                    }

                    @Override
                    public void onTokenFailed (String s, String s1) {
                        if (null != umPreLoginResultListener) {
                            umPreLoginResultListener.onTokenFailed(s, s1);
                        }
                    }
                });
    }

    public void openUMLogin (Context context, List<Map<String, String>> lists) {
        //7.一键登录唤起授权页
        umVerifyHelper.getLoginToken(context, 5 * 1000);
        //处理ui
        configLoginTokenPort(context, lists);
    }

    public void doDismiss () {
        //8.退出授权页
        umVerifyHelper.quitLoginPage();

        //9.退出授权时关闭加载状态
        umVerifyHelper.hideLoginLoading();
    }

    private void configLoginTokenPort (Context application, List<Map<String, String>> lists) {
        initDynamicView(application);
        umVerifyHelper.addAuthRegistViewConfig("switch_acc_tv", new UMAuthRegisterViewConfig.Builder()
                .setView(switchTV)
                .setRootViewId(UMAuthRegisterViewConfig.RootViewId.ROOT_VIEW_ID_BODY)
                .setCustomInterface(context -> umVerifyHelper.quitLoginPage()).build());
        int authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
        if (Build.VERSION.SDK_INT == 26) {
            authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_BEHIND;
        }

        int dialogWidth  = AppUtils.px2dp(application, AppUtils.getPhoneWidthPixels(application));
        int dialogHeight = AppUtils.px2dp(application, AppUtils.getRealyScreenHeight(application));
        umVerifyHelper.setAuthUIConfig(new UMAuthUIConfig.Builder()
                .setAppPrivacyOne("《" + lists.get(0).get("title") + "》", lists.get(0).get("url"))
                .setAppPrivacyTwo("《" + lists.get(1).get("title") + "》", lists.get(1).get("url"))
                .setAppPrivacyColor(Color.GRAY, Color.GRAY)
                .setPrivacyState(false)
                .setNavColor(Color.WHITE)
                .setNavText("")
                .setCheckboxHidden(false)
                .setStatusBarColor(Color.WHITE)
                .setStatusBarUIFlag(View.SYSTEM_UI_FLAG_IMMERSIVE)
                .setLightColor(true)
                .setAuthPageActIn("in_activity", "out_activity")
                .setAuthPageActOut("in_activity", "out_activity")
                .setVendorPrivacyPrefix("《")
                .setVendorPrivacySuffix("》")
                .setScreenOrientation(authPageOrientation)
                .setDialogWidth(dialogWidth)
                .setDialogHeight(dialogHeight)
                .setPrivacyOffsetY_B(100)
                .create());
    }

    private void initDynamicView (Context application) {
        switchTV = new ImageView(application);
        RelativeLayout.LayoutParams mLayoutParams2 =
                new RelativeLayout.LayoutParams(200, 200);
        mLayoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        mLayoutParams2.setMargins(0, 200, 0, 0);
        switchTV.setImageResource(R.mipmap.ic_launcher);
        switchTV.setScaleType(ImageView.ScaleType.FIT_XY);
        switchTV.setLayoutParams(mLayoutParams2);
    }

}
