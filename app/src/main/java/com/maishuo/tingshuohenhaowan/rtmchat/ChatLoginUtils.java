package com.maishuo.tingshuohenhaowan.rtmchat;

import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.bean.LogOutSuccessEvent;
import com.maishuo.tingshuohenhaowan.common.UserConfig;
import com.maishuo.tingshuohenhaowan.greendaomanager.LocalRepository;
import com.maishuo.tingshuohenhaowan.api.response.GoToOnlineBean;
import com.maishuo.tingshuohenhaowan.api.response.LoginOutBean;
import com.maishuo.tingshuohenhaowan.utils.LoginUtil;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.retrofit.CommonObserver;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.Nullable;

/**
 * author ：yh
 * date : 2021/1/18 15:19
 * description : 聊天sdk的登录 ,退出等
 */
public class ChatLoginUtils {

    public interface OnApiSuccessListener {

        void onSuccess ();

    }

    /**
     * 获取聊天的token和userid,并登录
     */
    public static void gotoOnLine () {
        if (!LoginUtil.checkLogin()) {
            return;
        }

        ApiService.Companion.getInstance()
                .goToOnline()
                .subscribe(new CommonObserver<GoToOnlineBean>() {
                    @Override
                    public void onResponseSuccess (GoToOnlineBean response) {
                        if (null == response) {
                            return;
                        }

                        int    uidInt   = response.getUidInt();
                        String rtmToken = response.getRtmToken();
                        PreferencesUtils.putLong(PreferencesKey.USER_UID, uidInt);
                        UserConfig.getInstance().setUid(uidInt);

                        //重新登录数据库
                        LocalRepository.getInstance().loginNew();

                        //登录操作
                        FriendChatUtil.getInstance().doLogin(String.valueOf(uidInt), rtmToken, new RtmChatListener() {
                            @Override
                            public void successBack (String file) {
                                UserConfig.getInstance().setPointChatKickAway(false);
                                UserConfig.getInstance().setPointChatLogin(true);
                            }

                            @Override
                            public void failBack () {

                            }
                        });
                    }
                });
    }

    /**
     * 统一的退出登录操作
     */
    public static void loginOut (OnApiSuccessListener onApiSuccessListener) {
        ApiService.Companion
                .getInstance()
                .loginOut()
                .subscribe(new CommonObserver<LoginOutBean>() {
                    @Override
                    public void onResponseSuccess (@Nullable LoginOutBean response) {
                        //清理用户数据
                        clearUserInfo();
                        //保存token数据
                        if (response != null) {
                            String token = response.getToken();
                            PreferencesUtils.putString(PreferencesKey.TOKEN, token);
                        }

                        if (onApiSuccessListener != null) {
                            onApiSuccessListener.onSuccess();
                        }

                        //通知登录成功
                        EventBus.getDefault().post(new LogOutSuccessEvent());
                    }
                });
    }

    /**
     * TODO:退出登录后,清理用户数据
     */
    public static void clearUserInfo () {
        FriendChatUtil.getInstance().doLogout();
        UserConfig.getInstance().setPointChatLogin(false);
        UserConfig.getInstance().setMessageUnreadDan(0);
        UserConfig.getInstance().setMessageUnreadZan(0);
        UserConfig.getInstance().setMessageUnreadSystem(0);

        PreferencesUtils.putString(PreferencesKey.TOKEN, "");
        PreferencesUtils.putString(PreferencesKey.USER_ID, "");
        PreferencesUtils.putString(PreferencesKey.USER_NAME, "");
        PreferencesUtils.putString(PreferencesKey.USER_AVATOR, "");
        PreferencesUtils.putBoolean(PreferencesKey.ONLINE, false);
    }
}
