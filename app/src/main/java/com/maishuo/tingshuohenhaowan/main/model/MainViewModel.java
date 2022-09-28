package com.maishuo.tingshuohenhaowan.main.model;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.maishuo.tingshuohenhaowan.api.response.GetTokenResponse;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.common.UserConfig;
import com.maishuo.tingshuohenhaowan.gift.SvgaUtil;
import com.maishuo.tingshuohenhaowan.api.response.FirstInitBean;
import com.maishuo.tingshuohenhaowan.api.response.GetGiftNoLoginBean;
import com.maishuo.tingshuohenhaowan.rtmchat.ChatLoginUtils;
import com.maishuo.tingshuohenhaowan.utils.CustomPreferencesUtils;
import com.qichuang.commonlibs.basic.BaseViewModel;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.LogUtils;
import com.qichuang.commonlibs.utils.LoggerUtils;
import com.qichuang.commonlibs.utils.PhoneUtils;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.retrofit.CommonObserver;

import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * author ：Seven
 * date : 5/20/21
 * description :数据绑定
 */
@SuppressLint("StaticFieldLeak")
public class MainViewModel extends BaseViewModel {
    public MutableLiveData<FirstInitBean>             updateLiveData = new MutableLiveData<>();
    public MutableLiveData<FirstInitBean.LayerAdBean> guideLiveData  = new MutableLiveData<>();
    public MutableLiveData<Integer>                   unRedLiveData  = new MutableLiveData<>();
    public MutableLiveData<String>                    tokenLiveData  = new MutableLiveData<>();

    private static MainViewModel     instance;
    private final  AppCompatActivity activity;

    private MainViewModel (AppCompatActivity mActivity) {
        activity = new WeakReference<>(mActivity).get();
    }

    public static MainViewModel getInstance (AppCompatActivity mActivity) {
        if (instance == null) {
            synchronized (MainViewModel.class) {
                if (instance == null) {
                    instance = new MainViewModel(mActivity);
                }
            }
        }
        return instance;
    }

    public void getToken () {
        ApiService.Companion.getInstance()
                .getToken()
                .subscribe(new CommonObserver<GetTokenResponse>() {
                    @Override
                    public void onResponseSuccess (GetTokenResponse response) {
                        if (null != response) {
                            LoggerUtils.INSTANCE.e(response.getToken());

                            PreferencesUtils.putString(PreferencesKey.TOKEN, response.getToken());

                            tokenLiveData.postValue(response.getToken());
                        } else {
                            tokenLiveData.postValue(null);
                        }
                    }

                    @Override
                    public void onResponseError (@Nullable String message, @Nullable Throwable e, @Nullable String code) {
                        super.onResponseError(message, e, code);
                        tokenLiveData.postValue(null);
                    }
                });
    }

    /**
     * 初始化数据
     */
    public void init () {
        getInitData();
        getGiftAndMusic();
    }

    /**
     * 释放监听
     */
    public void clearLiveData () {
        updateLiveData.removeObservers(activity);
        guideLiveData.removeObservers(activity);
        unRedLiveData.removeObservers(activity);
        tokenLiveData.removeObservers(activity);
    }

    /**
     * 获取初始化数据
     */
    public void getInitData () {
        ApiService.Companion.getInstance()
                .indexInit()
                .subscribe(new CommonObserver<FirstInitBean>() {
                    @Override
                    public void onResponseSuccess (@Nullable FirstInitBean response) {
                        indexInit(response);
                        //登录声网相关
                        ChatLoginUtils.gotoOnLine();
                    }
                });
    }

    private void indexInit (FirstInitBean bean) {
        try {
            if (null == bean) {
                return;
            }

            FirstInitBean.ExtTokenBean extTokenBean = bean.getExtToken();
            //先处理token
            if (extTokenBean != null) {
                String token = extTokenBean.getToken();
                PreferencesUtils.putString(PreferencesKey.TOKEN, token);
            }

            //系统配置的保存
            //是否启用广告：0-不启用广告，1-启用广告
            if (bean.getInitConfig() != null && bean.getInitConfig().getAd20210409() != null) {
                PreferencesUtils.putInt(PreferencesKey.ENABLE_AD, bean.getInitConfig().getAd20210409().getStatus());
            }

            //更新、引导页、活动弹窗逻辑处理
            try {
                //先处理更新弹窗
                FirstInitBean.LatestVersionBean latestVersionBean = bean.getLatestVersion();
                FirstInitBean.LayerAdBean       layerAdBean       = bean.getLayerAd();
                if (latestVersionBean != null && layerAdBean != null) {
                    int myCode  = PhoneUtils.getAppVersionCode(activity);
                    int newCode = latestVersionBean.getVersionCode();
                    if (myCode < newCode) {
                        updateLiveData.postValue(bean);
                    } else {
                        guideLiveData.postValue(layerAdBean);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.LOGE(this.getClass().getSimpleName(), "首页弹窗异常");
            }
            //处理消息的未读展示
            FirstInitBean.InitConfigBean initConfigBean = bean.getInitConfig();
            if (initConfigBean != null) {
                int messageUnreadDan    = initConfigBean.getVoiceUnReadNum();
                int messageUnreadZan    = initConfigBean.getStayVoicePraiseUnread();
                int messageUnreadSystem = initConfigBean.getSystemUnRedNum();
                UserConfig.getInstance().setMessageUnreadDan(messageUnreadDan);
                UserConfig.getInstance().setMessageUnreadZan(messageUnreadZan);
                UserConfig.getInstance().setMessageUnreadSystem(messageUnreadSystem);

                unRedLiveData.postValue(0);

                if (null != bean.getSysEnv()
                        && !TextUtils.isEmpty(bean.getSysEnv().getMobile_upload_introduce())) {
                    CustomPreferencesUtils.saveMobileUploadIntroduce(bean.getSysEnv().getMobile_upload_introduce());
                }

                if (null != bean.getSysEnv()
                        && !TextUtils.isEmpty(bean.getSysEnv().getPc_upload_introduce())) {
                    CustomPreferencesUtils.savePcUploadIntroduce(bean.getSysEnv().getPc_upload_introduce());
                }

                if (!TextUtils.isEmpty(initConfigBean.getComplaint_phone())) {
                    CustomPreferencesUtils.saveComplaintPhone(initConfigBean.getComplaint_phone());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取礼物和背景音乐相关缓存数据
     */
    private void getGiftAndMusic () {
        ApiService.Companion.getInstance().getGiftNoLoginApi()
                .subscribe(new CommonObserver<GetGiftNoLoginBean>() {
                    @Override
                    public void onResponseSuccess (@Nullable GetGiftNoLoginBean bean) {
                        if (bean != null) {
                            //礼物特效文件下载
                            List<GetGiftNoLoginBean.GiftsBean> giftList = bean.getGifts();
                            if (giftList != null && giftList.size() > 0) {
                                for (GetGiftNoLoginBean.GiftsBean giftsBean : giftList) {
                                    int effect = giftsBean.getEffect();
                                    if (effect == 1) {
                                        //有特效的时候
                                        String fileName   = giftsBean.getName() + giftsBean.getGift_version();
                                        String effect_img = giftsBean.getEffect_img();
                                        if (!SvgaUtil.haveLocalSvgaFile(fileName)) {
                                            SvgaUtil.downSvgaFile(activity, effect_img, fileName);
                                        }
                                    }

                                }
                            }

                            //背景音乐下载
                            List<GetGiftNoLoginBean.SoundeffectBean> soundeffect = bean.getSoundeffect();
                            if (soundeffect != null && soundeffect.size() > 0) {
                                for (GetGiftNoLoginBean.SoundeffectBean soundeffectBean : soundeffect) {
                                    String soundUrl = soundeffectBean.getSound_path();
                                    if (!SvgaUtil.haveLocalSoundFile(soundUrl)) {
                                        SvgaUtil.downSoundFile(activity, soundUrl);
                                    }
                                }
                            }
                        }
                    }
                });
    }
}
