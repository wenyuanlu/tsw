package com.maishuo.tingshuohenhaowan.common;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import com.corpize.sdk.ivoice.QCiVoiceSdk;
import com.maishuo.tingshuohenhaowan.BuildConfig;
import com.maishuo.tingshuohenhaowan.audio.AudioPlayerManager;
import com.maishuo.tingshuohenhaowan.audio.BackgroundAudioPlayerManager;
import com.maishuo.tingshuohenhaowan.helper.OAIDHelper;
import com.maishuo.tingshuohenhaowan.rtmchat.FriendChatUtil;
import com.maishuo.tingshuohenhaowan.service.InitService;
import com.maishuo.umeng.SharePlatform;
import com.maishuo.umeng.push.PushHelper;
import com.qichuang.commonlibs.basic.CustomBasicApplication;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.ChannelUtil;
import com.qichuang.commonlibs.utils.LogUtils;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.retrofit.ApiConstants;

import java.util.HashMap;
import java.util.Map;

import update.UpdateAppUtils;

/**
 * author : yun
 * desc   : 项目中的 Application 基类
 */
public final class CustomApplication extends CustomBasicApplication implements LifecycleOwner {

    private static CustomApplication app;
    private final  LifecycleRegistry mLifecycle = new LifecycleRegistry(this);
    private        boolean           isOpen;//记录是否打开第三方应用
    public         int               channelId;
    public         String            channelName;

    private final Map<Context, AudioPlayerManager>           audioPlayerManagerMap           = new HashMap<>();
    private final Map<Context, BackgroundAudioPlayerManager> backgroundAudioPlayerManagerMap = new HashMap<>();

    public boolean isOpen () {
        return isOpen;
    }

    public void setOpen (boolean open) {
        isOpen = open;
    }

    public Map<Context, AudioPlayerManager> getAudioPlayerManagerMap () {
        return audioPlayerManagerMap;
    }

    public Map<Context, BackgroundAudioPlayerManager> getBackgroundAudioPlayerManagerMap () {
        return backgroundAudioPlayerManagerMap;
    }

    public static CustomApplication getApp () {
        return app;
    }

    @Override
    public void onCreate () {
        super.onCreate();
        app = this;

        initChannel();

        mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);

        boolean isContains = PreferencesUtils.contains(Constant.COMMON_CHANGER_SERVICE_TAG);
        if (isContains) {
            boolean isDebugService = PreferencesUtils.getBoolean(Constant.COMMON_CHANGER_SERVICE_TAG);
            ApiConstants.INSTANCE.setDebug(isDebugService);
        }

        //App前后台监听
        registerActivityLifecycleCallbacks(new HotActivityLifecycleCallbacks());

        if (BuildConfig.DEBUG) {
            CrashHandler.register(app);
        }

        //初始化app 更新
        UpdateAppUtils.init(this);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle () {
        return mLifecycle;
    }

    private void initChannel () {
        //获取渠道名称
        channelName = ChannelUtil.getInstance().getChannelName();
        //获取渠道id
        channelId = ChannelUtil.getInstance().getChannelId();

        // SDK预初始化函数
        // preInit预初始化函数耗时极少，不会影响App首次冷启动用户体验
        PushHelper.preInit(CustomApplication.getApp(),channelName);
    }

    //需要同意用户隐私协议才可以初始化的sdk
    public void initAgreementSDK () {
        try {
            Intent intent = new Intent(CustomApplication.getApp(), InitService.class);
            startService(intent);

            //获取渠道名称
            String channelName = ChannelUtil.getInstance().getChannelName();
            //注册友盟
            SharePlatform.init(CustomApplication.getApp(), channelName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //用户可以不同意隐私协议就可以初始化,但不能再开始使用前初始化
    public void initCommonSDK () {
        try {
            initOAID();
            initChatSDK();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initChatSDK () {
        //初始化聊天sdk
        FriendChatUtil.getInstance().initSDK(CustomApplication.getApp());
    }

    private void initOAID () {
        try {
            String oaid = PreferencesUtils.getString(PreferencesKey.OAID, "");
            if (!TextUtils.isEmpty(oaid)) {
                QCiVoiceSdk.get().init(CustomApplication.getApp(), oaid, Constant.COMMON_QC_MID,Constant.CUSTOM_IVOICE_DNT_VALUE);
            } else {
                //首先获取oaid
                OAIDHelper.getInstance()
                        .getDeviceIds(this)
                        .setOnFetchOAIDListener(new OAIDHelper.OnFetchOAIDListener() {

                            @Override
                            public void onOAIDSuccess (String oaid) {
                                LogUtils.LOGE("IVOICE OAID:", oaid);
                                //企创聚合音频sdk初始化,需传入oaid,
                                //如果设备系统版本大于等于android10,则oaid 必传,不可为空
                                QCiVoiceSdk.get().init(CustomApplication.getApp(), oaid, Constant.COMMON_QC_MID,Constant.CUSTOM_IVOICE_DNT_VALUE);
                                PreferencesUtils.putString(PreferencesKey.OAID, oaid);
                            }

                            @Override
                            public void onOAIDFail (int code) {
                                QCiVoiceSdk.get().init(CustomApplication.getApp(), null, Constant.COMMON_QC_MID,Constant.CUSTOM_IVOICE_DNT_VALUE);
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resources getResources () {
        Resources     res    = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
}