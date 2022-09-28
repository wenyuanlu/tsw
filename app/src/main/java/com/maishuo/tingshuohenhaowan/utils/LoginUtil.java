package com.maishuo.tingshuohenhaowan.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.maishuo.tingshuohenhaowan.api.param.UMengLoginParam;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.bean.LoginEvent;
import com.maishuo.tingshuohenhaowan.api.response.LoginBean;
import com.maishuo.tingshuohenhaowan.listener.OnUmLoginInterface;
import com.maishuo.tingshuohenhaowan.login.ui.LoginActivity;
import com.maishuo.tingshuohenhaowan.main.event.MainConfigEvent;
import com.maishuo.umeng.UmLoginUtils;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.LogUtils;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.retrofit.ApiConstants;
import com.qichuang.retrofit.CommonObserver;
import com.umeng.umverify.listener.UMPreLoginResultListener;
import com.umeng.umverify.listener.UMTokenResultListener;
import com.umeng.umverify.model.UMTokenRet;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yun on 2021/1/27
 * EXPLAIN:
 */
public class LoginUtil {

    private static final int REQUEST_CODE_TAG = 0x1001;

    public static void toLogin (Activity activity, int requestCode, OnUmLoginInterface umLoginInterface) {
        umLogin(activity, requestCode, umLoginInterface);
    }

    private static List<Map<String, String>> fetchList () {
        List<Map<String, String>> lists = new ArrayList<>();
        Map<String, String>       maps  = new HashMap();
        maps.put("title", "用户协议");
        maps.put("url", "https://live.tingshuowan.com/listen/agreement?type=1");
        lists.add(maps);
        Map<String, String> maps2 = new HashMap();
        maps2.put("title", "隐私信息保护政策");
        maps2.put("url", "https://live.tingshuowan.com/listen/agreement?type=2");
        lists.add(maps2);
        return lists;
    }

    private static void openLoginActivity (Activity activity, int requestCode) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    private static void umLogin (Activity activity, int requestCode, OnUmLoginInterface umLoginInterface) {
        try {
            UmLoginUtils umLoginUtils = UmLoginUtils.getInstance();
            umLoginUtils.openUMLogin(activity, fetchList());
            umLoginUtils.setUmTokenResultListener(new UMTokenResultListener() {
                @Override
                public void onTokenSuccess (String s) {
                    try {
                        UMTokenRet tokenRet = new Gson().fromJson(s, UMTokenRet.class);
                        if (tokenRet != null) {
                            if ("600000".equals(tokenRet.getCode())) {
                                umLoginUtils.doDismiss();
                                if (!TextUtils.isEmpty(tokenRet.getToken())) {
                                    umLoginApi(activity, tokenRet.getToken(), umLoginInterface);
                                }
                            } else if (!"600001".equals(tokenRet.getCode())) {
                                openLoginActivity(activity, requestCode);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onTokenFailed (String s) {
                    try {
                        umLoginUtils.doDismiss();
                        LogUtils.LOGE("onTokenFailed", s);
                        UMTokenRet tokenRet = new Gson().fromJson(s, UMTokenRet.class);
                        if (null != tokenRet) {
                            //700001-其它登录方式，700000-取消登录, 600007-SIM卡无法检测, 600005-手机终端不安全
                            if (!TextUtils.equals("700000", tokenRet.getCode())) {
                                openLoginActivity(activity, requestCode);
                            }
                            if (null != umLoginInterface) {
                                umLoginInterface.umLoginOther();
                            }
                        } else {
                            if (null != umLoginInterface) {
                                umLoginInterface.umLoginError();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            umLoginUtils.setUmPreLoginResultListener(new UMPreLoginResultListener() {
                @Override
                public void onTokenSuccess (String s) {
                    LogUtils.LOGE("doDismiss", "123");
                    umLoginUtils.doDismiss();
                }

                @Override
                public void onTokenFailed (String s, String s1) {
                    LogUtils.LOGE("doDismiss", "129");
                    umLoginUtils.doDismiss();
                    openLoginActivity(activity, requestCode);
                }
            });

            if (null != umLoginInterface) {
                umLoginInterface.umLoginIsShowDialog();
            }
        } catch (Exception e) {
            LogUtils.LOGE("tshhw", "toLogin，e：" + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void umLoginApi (Activity activity, String token, OnUmLoginInterface umLoginInterface) {
        UMengLoginParam param = new UMengLoginParam();
        param.setUmengToken(token);
        param.setUserId("");
        ApiService.Companion.getInstance()
                .uMengLogin(param)
                .subscribe(new CommonObserver<LoginBean>() {
                    @Override
                    public void onResponseSuccess (@Nullable LoginBean response) {
                        umLoginApiResponse(response);

                        if (null != umLoginInterface) {
                            umLoginInterface.umLoginSuccess();
                        }
                    }

                    @Override
                    public void onResponseError (@Nullable String message, @Nullable Throwable e, @Nullable String code) {
                        super.onResponseError(message, e, code);

                        if (!TextUtils.isEmpty(code) && ApiConstants.UMENG_LOGIN_ERROR_CODE.equals(code)) {
                            openLoginActivity(activity, REQUEST_CODE_TAG);
                        }
                    }
                });
    }

    private static void umLoginApiResponse (LoginBean bean) {
        if (null == bean) {
            return;
        }

        LoginBean.UserBasicBean userBean = bean.getUserBasic();
        PreferencesUtils.putString(PreferencesKey.TOKEN, bean.getToken());
        PreferencesUtils.putString(PreferencesKey.USER_ID, userBean.getUserId());
        PreferencesUtils.putString(PreferencesKey.USER_NAME, userBean.getName());
        PreferencesUtils.putString(PreferencesKey.USER_AVATOR, userBean.getAvatar());
        PreferencesUtils.putBoolean(PreferencesKey.ONLINE, true);
        PreferencesUtils.putInt(PreferencesKey.AUTH_STATUS, userBean.getRealStatus());
        PreferencesUtils.putInt(PreferencesKey.VIP, userBean.getVip());
        //通知登录成功
        EventBus.getDefault().post(new LoginEvent());

        //热云登录
        TrackingAgentUtils.setLoginSuccessBusiness(TrackingAgentUtils.getDeviceId());
        //新用户热云注册
        if (bean.getIsNewUser() == 1) {
            TrackingAgentUtils.setRegisterWithAccountID(TrackingAgentUtils.getDeviceId());
        }
    }

    public static boolean checkLogin () {
        return PreferencesUtils.getBoolean(PreferencesKey.ONLINE, false);
    }

    public static boolean isLogin (Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            boolean  isLogin  = checkLogin();

            if (!isLogin) {
                //跳转登录界面暂停当前播放
                EventBus.getDefault().post(new MainConfigEvent().setType(2).setPlay(false));

                String agreement = CustomPreferencesUtils.fetchAgreement();
                if (TextUtils.isEmpty(agreement)) {
                    openLoginActivity(activity, REQUEST_CODE_TAG);
                    return isLogin;
                }

                LoginUtil.toLogin(activity, REQUEST_CODE_TAG, new OnUmLoginInterface() {
                    @Override
                    public void umLoginSuccess () {
                        LogUtils.LOGE("umLoginSuccess");
                    }

                    @Override
                    public void umLoginError () {
                        LogUtils.LOGE("umLoginError");
                    }

                    @Override
                    public void umLoginOther () {
                        LogUtils.LOGE("umLoginOther");
                    }

                    @Override
                    public void umLoginIsShowDialog () {
                        LogUtils.LOGE("umLoginIsShowDialog");
                    }

                    @Override
                    public void commonLogin () {
                        LogUtils.LOGE("commonLogin");
                    }
                });
            }
            return isLogin;
        }
        return false;
    }
}
