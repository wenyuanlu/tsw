package com.maishuo.tingshuohenhaowan.main.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.corpize.sdk.ivoice.AdAttr;
import com.corpize.sdk.ivoice.QCiVoiceSdk;
import com.corpize.sdk.ivoice.admanager.QcAdManager;
import com.corpize.sdk.ivoice.listener.AudioCustomQcAdListener;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.audio.AudioPlayerManager;
import com.maishuo.tingshuohenhaowan.audio.BackgroundAudioPlayerManager;
import com.maishuo.tingshuohenhaowan.api.response.LiveVoiceCommentListBean;
import com.maishuo.tingshuohenhaowan.api.response.PhonicListBean;
import com.maishuo.tingshuohenhaowan.main.event.AttentionEvent;
import com.maishuo.tingshuohenhaowan.main.event.MainMenuEvent;
import com.maishuo.tingshuohenhaowan.main.event.PraiseEvent;
import com.maishuo.tingshuohenhaowan.main.view.VoicePlayRightView;
import com.maishuo.tingshuohenhaowan.personal.ui.PersonCenterActivity;
import com.maishuo.tingshuohenhaowan.utils.LoginUtil;
import com.maishuo.tingshuohenhaowan.widget.CircleImageView;
import com.maishuo.tingshuohenhaowan.widget.likeview.LikeView;
import com.maishuo.tingshuohenhaowan.widget.barrage.BarrageManager;
import com.maishuo.tingshuohenhaowan.widget.barrage.BarrageModel;
import com.maishuo.tingshuohenhaowan.widget.proload.PreLoadManager;
import com.maishuo.tingshuohenhaowan.widget.recyclerview.voice.PlayAdapter;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.DeviceUtil;
import com.qichuang.commonlibs.utils.GlideUtils;
import com.qichuang.commonlibs.utils.LogUtils;
import com.qichuang.commonlibs.utils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * author ：Seven
 * date : 5/25/21
 * description :音频播放适配
 */
public class VoicePlayAdapter extends PlayAdapter<VoicePlayAdapter.ViewHolder> implements AudioPlayerManager.DefaultEventListener, BarrageManager.BarrageCallBack {
    public final  String                        proBusIdAudio   = "AudioPlayer";
    public final  String                        proBusIdBgAudio = "BgAudioPlayer";
    private final Context                       mContext;
    private final int                           type;               //0：关注，1：推荐，2：个人中心作品
    public        int                           tabId           = 1;//0：关注，1：推荐
    private final String                        userId;
    private final VoicePlayAdapterListener      listener;
    private final List<PhonicListBean.ListBean> list;
    private       Handler                       mHandler;

    private ViewHolder mCurrentHolder;
    public  int        mCurrentPosition;

    public final  AudioPlayerManager           audioPlayerManager;
    private final BackgroundAudioPlayerManager backgroundAudioPlayerManager;
    private       boolean                      isHandClick;//是否是手动暂停

    private final Map<Integer, List<LiveVoiceCommentListBean>> barrageMap;
    private       BarrageManager                               barrageManager;
    private       int                                          barrageCount;//弹幕页数，30秒为一页
    private       List<Integer>                                barrageCountList;//弹幕页数集合，防止重复请求
    private       int                                          barrageContainerHeight;//弹幕显示的区域

    private PreLoadManager preLoadManager;//预加载管理类
    public  boolean        isBackground;//是否处于系统后台运行
    public  int            isSeek;//是否快进过

    public VoicePlayAdapter (Context context, VoicePlayAdapterListener listener, int type, String userId) {
        this.mContext = context;
        this.type = type;
        this.userId = userId;
        this.listener = listener;
        this.list = new ArrayList<>();
        this.barrageMap = new HashMap<>();
        this.audioPlayerManager = new AudioPlayerManager(mContext);
        this.backgroundAudioPlayerManager = new BackgroundAudioPlayerManager(mContext);
        this.audioPlayerManager.setOnDefaultEventListener(this);
        this.barrageContainerHeight = PreferencesUtils.getInt("BarrageContainerHeight", 0);
    }

    public List<PhonicListBean.ListBean> getList () {
        return list;
    }

    public PhonicListBean.ListBean getListBean (int position) {
        return list.isEmpty() || list.get(position) == null ? new PhonicListBean.ListBean() : list.get(position);
    }

    public void addList (List<PhonicListBean.ListBean> list) {
        this.list.addAll(list);
        setLocalLoginStatus();
    }

    public void clearListData () {
        if (list.size() > 0) {
            release();
            list.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount () {
        return list.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_play_voice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull ViewHolder holder, int position) {
        PhonicListBean.ListBean itemBean = getListBean(position);

        if (itemBean.isAd()) {
            //广告页
            holder.flAd.removeAllViews();
            holder.rlNormal.setVisibility(View.GONE);
            initAd(holder, position);
        } else {
            //正常留声页
            holder.flAd.removeAllViews();
            holder.rlNormal.setVisibility(View.VISIBLE);

            //大背景图
            GlideUtils.INSTANCE.loadImage(mContext, itemBean.getBg_img(), holder.ivBackground);
            //圆形封面图
            GlideUtils.INSTANCE.loadImage(mContext, itemBean.getImage_path(), holder.viewCircleHead);

            //用户名
            holder.tvNickname.setText(String.format("@%s", TextUtils.isEmpty(itemBean.getUname()) ? "" : itemBean.getUname()));
            //用户名点击
            if (type != 2) {
                holder.tvNickname.setOnClickListener(view -> clickBarrageHeadView(itemBean.getUser_id()));
            }

            //简介
            holder.tvPhonicDesc.setText(TextUtils.isEmpty(itemBean.getDesc()) ? "" : itemBean.getDesc());
            //右边按钮
            holder.voicePlayRightView.setInitData(itemBean, type, userId);
            //全屏点赞
            holder.likeView.setOnLikeListener(() -> holder.voicePlayRightView.praise(true));
            //全屏暂停或播放
            holder.likeView.setOnPlayPauseListener(this::clickPlayOrPause);
            holder.viewCircleHead.setOnClickListener(view -> clickPlayOrPause());

            //获取昵称以上高度计算弹幕空间
            measureBarrageContainerHeight(holder);
        }
    }

    /**
     * 弹幕点击事件
     */
    @Override
    public void clickBarrageView () {
        if (barrageManager != null) {
            barrageManager.setHandClick(isHandClick);
        }
    }

    /**
     * 弹幕点击事件
     */
    @Override
    public void clickBarrageHeadView (String userId) {
        voicePause();
        Intent intent = new Intent(mContext, PersonCenterActivity.class);
        intent.putExtra("userId", userId);
        ((Activity) mContext).startActivityForResult(intent, 0);
    }

    /**
     * 重置初始值
     * 释放内存
     */
    public void release () {
        isSeek = 0;
        currentTime = 0;
        isHandClick = false;
        clearCountDown();
        cancelAnimation();
        voiceStop();
        if (barrageMap != null) {
            barrageMap.clear();
        }
        if (barrageCountList != null) {
            barrageCountList.clear();
            barrageCountList = null;
        }
        if (barrageManager != null) {
            barrageManager.releasePopup();
        }
        if (mCurrentHolder != null) {
            mCurrentHolder.likeView.ClearAnimation();
            mCurrentHolder = null;
        }
        //因为删除作品getListBean(mCurrentPosition)会越界，type=2的情况不会有广告
        if (type != 2 && getListBean(mCurrentPosition).isAd() && getListBean(mCurrentPosition).getAdManager() != null) {
            getListBean(mCurrentPosition).getAdManager().skipPlayAd();
        }
    }

    @Override
    public void onPageSelected (int itemPosition, View itemView) {
        release();
        mCurrentPosition = itemPosition;
        mCurrentHolder = new ViewHolder(itemView);
        if (getListBean(itemPosition).isAd() && getListBean(itemPosition).getAdManager() != null) {
            EventBus.getDefault().post(new MainMenuEvent(false));//禁用首页菜单
            getListBean(itemPosition).getAdManager().startPlayAd();
        } else {
            EventBus.getDefault().post(new MainMenuEvent(true));//启用首页菜单
            listener.onPageSelected(itemPosition);
            initBarrage(itemPosition);
            initVoicePlayer(itemPosition);
        }
    }

    /**
     * 初始化播放器
     */
    private void initVoicePlayer (int itemPosition) {
        if (preLoadManager == null) {
            preLoadManager = PreLoadManager.getInstance(mContext);
        }

        audioPlayerManager.setAudioUrl(preLoadManager.getLocalUrlAppendWithUrl(getListBean(itemPosition).getVoice_path()));
        backgroundAudioPlayerManager.setAudioUrl(preLoadManager.getLocalUrlAppendWithUrl(getListBean(itemPosition).getBg_music_path()));

        audioPlayerManager.setVolume(getListBean(itemPosition).getVoice_volume());
        backgroundAudioPlayerManager.setVolume(getListBean(itemPosition).getBg_music_volume());

        if (tabId == type && !PreferencesUtils.getBoolean("playingFirstAd", false)) {
            audioPlayerManager.start();
            backgroundAudioPlayerManager.play();
        }

        mCurrentHolder.progressBarView.setPlayer(audioPlayerManager.getMediaPlayer());

        //更新预加载当前Url
        preLoadManager.currentVideoPlay(proBusIdAudio + type, getListBean(itemPosition).getVoice_path());
        preLoadManager.currentVideoPlay(proBusIdBgAudio + type, getListBean(itemPosition).getBg_music_path());
    }

    /**
     * 播放状态变化
     * 暂停、播放
     */
    @Override
    public void onReady () {
        if (mCurrentHolder == null) {
            return;
        }
        showProgressBarView();
        if (audioPlayerManager != null && audioPlayerManager.isPlaying() && !isBackground) {
            startCountDown();
            resumeBarrage();
            resumeAnimation();
            mCurrentHolder.ivPlayIcon.setVisibility(View.GONE);
        } else {
            clearCountDown();
            pauseBarrage();
            pauseAnimation();
            mCurrentHolder.ivPlayIcon.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 播放结束
     * 1、根据设置播放模式播放
     * 2、最后一条数据自动重播
     */
    @Override
    public void onEnd () {
        //播放模式,1-循环播放，0-自动播放
        int reportType = PreferencesUtils.getInt(PreferencesKey.PLAY_TYPE, 0);
        if (1 == reportType || getList().size() - 1 == mCurrentPosition) {
            replay(mCurrentPosition);
        } else {
            listener.playEnd(mCurrentPosition + 1, 0);
        }
    }

    /**
     * 播放失败
     */
    @Override
    public void onError (String msg) {
        cancelAnimation();
        clearCountDown();
        showLoadingView();
    }

    /**
     * 拖动了进度条回调
     */
    @Override
    public void isSeek () {
        isSeek = 1;
    }

    /**
     * 重播
     */
    public void replay (int position) {
        mCurrentPosition = position;
        voiceStop();
        initVoicePlayer(position);
    }

    /**
     * 结束播放
     */
    public void voiceStop () {
        if (audioPlayerManager != null && backgroundAudioPlayerManager != null) {
            audioPlayerManager.stop(true);
            backgroundAudioPlayerManager.stop(true);
        }
    }

    /**
     * 点击播放或者暂停
     */
    public void clickPlayOrPause () {
        if (!PreferencesUtils.getBoolean("playingFirstAd", false)) {
            isHandClick = audioPlayerManager.isPlaying();
            audioPlayerManager.playOrPause();
            backgroundAudioPlayerManager.playOrPause();
        }
    }

    /**
     * 播放:广告跟留声
     * 判断是否手动暂停
     */
    public void voicePlay () {
        if (!isHandClick && tabId == type && !PreferencesUtils.getBoolean("playingFirstAd", false)) {
            if (getListBean(mCurrentPosition).isAd() && getListBean(mCurrentPosition).getAdManager() != null) {
                getListBean(mCurrentPosition).getAdManager().onResume();
            } else {
                audioPlayerManager.start();
                backgroundAudioPlayerManager.play();
            }
        }
        //清除弹幕pop
        if (barrageManager != null) {
            barrageManager.releasePopup();
        }
    }

    /**
     * 发送单条或礼物弹幕后播放
     * 不考虑是否手动暂停
     */
    public void voicePlayBySendBarrage () {
        if (tabId == type && !PreferencesUtils.getBoolean("playingFirstAd", false)) {
            isHandClick = false;
            audioPlayerManager.start();
            backgroundAudioPlayerManager.play();
        }
    }

    /**
     * 播放到指定位置
     */
    public void voiceSeekTo () {
        //操作正在显示的tab
        if (tabId == type && !PreferencesUtils.getBoolean("playingFirstAd", false)) {
            long   audioCurrentPosition   = audioPlayerManager.getCurrentPosition();
            long   bgAudioCurrentPosition = backgroundAudioPlayerManager.getCurrentPosition();
            String audioUrl               = preLoadManager.getLocalUrlAppendWithUrl(audioPlayerManager.getAudioUrl());
            String bgAudioUrl             = preLoadManager.getLocalUrlAppendWithUrl(backgroundAudioPlayerManager.getAudioUrl());
            audioPlayerManager.stop(true);
            backgroundAudioPlayerManager.stop(true);
            audioPlayerManager.setAudioUrl(audioUrl);
            backgroundAudioPlayerManager.setAudioUrl(bgAudioUrl);
            audioPlayerManager.seekTo(audioCurrentPosition);
            backgroundAudioPlayerManager.seekTo(bgAudioCurrentPosition);
            audioPlayerManager.start();
            backgroundAudioPlayerManager.play();
        }
    }

    /**
     * 暂停:广告跟留声
     */
    public void voicePause () {
        if (getListBean(mCurrentPosition).isAd() && getListBean(mCurrentPosition).getAdManager() != null) {
            getListBean(mCurrentPosition).getAdManager().onPause();
        } else {
            audioPlayerManager.pause();
            backgroundAudioPlayerManager.pause();
        }
    }

    /**
     * VoicePlayRightView每条数据都会加载，为了性能不用EvenBus
     * 且只有点弹幕的时候接口需要当前播放时间，故与正常暂停分开
     * 暂停并且发送当前播放时间
     */
    public void voicePauseAndSendCurrentTime () {
        if (tabId == type) {
            PreferencesUtils.putInt("currentTime", currentTime);
        }
        voicePause();
    }

    /**
     * 显示进度条
     */
    private void showProgressBarView () {
        mCurrentHolder.progressBarView.setVisibility(barrageCount > 1 ? View.VISIBLE : View.GONE);
        mCurrentHolder.rlLoadingBar.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示加载条
     */
    private void showLoadingView () {
        mCurrentHolder.progressBarView.setVisibility(View.INVISIBLE);
        mCurrentHolder.rlLoadingBar.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化弹幕
     */
    private void initBarrage (int itemPosition) {
        if (barrageManager == null) {
            barrageManager = new BarrageManager();
        }
        barrageManager.setBarrageCallBack(this);
        barrageManager.init(mContext, mCurrentHolder.flBarrage); // 必须首先调用init方法
        barrageCount = getListBean(mCurrentPosition).getBarrageCount();
        barrageCountList = new ArrayList<>();
        if (!isBackground) {
            listener.getBarrageData(getListBean(itemPosition).getId(), 0, Math.min(getListBean(itemPosition).getVoice_time() / 1000, 30));
            barrageCountList.add(0);
        }
    }

    /**
     * 设置弹幕数据-待发送
     */
    public void setBarrageData (List<LiveVoiceCommentListBean> barrageList) {
        //根据弹幕时间重组弹幕数据
        for (LiveVoiceCommentListBean beanI : barrageList) {
            List<LiveVoiceCommentListBean> copyBarrageList = new ArrayList<>();
            for (LiveVoiceCommentListBean beanJ : barrageList) {
                if (beanI.getSeconds() == beanJ.getSeconds()) {
//                    //计算出送礼的次数
//                    if (beanI.getContent().contains("送出") &&
//                            TextUtils.equals(beanI.getContent(), beanJ.getContent())) {
//                        beanJ.setContentCount(beanJ.getContentCount() + 1);
//                    }
                    copyBarrageList.add(beanJ);
                }
            }
            //通过hashSet去重
            LinkedHashSet                  hashSet               = new LinkedHashSet<>(copyBarrageList);
            List<LiveVoiceCommentListBean> listWithoutDuplicates = new ArrayList<>(hashSet);
            barrageMap.put(beanI.getSeconds(), listWithoutDuplicates);
        }
    }

    /**
     * 暂停弹幕
     */
    public void pauseBarrage () {
        if (null != barrageManager) {
            barrageManager.pause();
        }
    }

    /**
     * 继续弹幕
     */
    public void resumeBarrage () {
        if (null != barrageManager) {
            barrageManager.resume();
        }
    }

    /**
     * 清除倒计时
     */
    public void clearCountDown () {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
            mHandler = null;
        }
    }

    /**
     * 开始倒计时
     */
    public void startCountDown () {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.post(mRunnable);
    }

    /**
     * 获取弹幕
     * 发送弹幕
     */
    public        int      currentTime = 0;
    private final Runnable mRunnable   = new Runnable() {
        @Override
        public void run () {
            //音频当前播放时间
            currentTime = (int) (audioPlayerManager.getCurrentPosition() / 1000);
            //获取30秒后的弹幕
            getNextPageBarrage();
            //过滤播放时间对应的弹幕数据并发送弹幕
            filterBarrageData();
            //每秒执行一次，根据播放时间发送弹幕
            mHandler.postDelayed(this, 1000);
        }
    };

    /**
     * 获取30秒后的弹幕
     */
    private void getNextPageBarrage () {
        for (int i = 0; i < barrageCount; i++) {
            if (currentTime >= i * 30) {
                if (barrageCountList != null && !barrageCountList.contains(i)) {
                    listener.getBarrageData(getListBean(mCurrentPosition).getId(), i * 30, (i + 1) * 30);
                    barrageCountList.add(i);
                    break;
                }
            }
        }
    }

    /**
     * 过滤播放时间对应的弹幕数据并发送弹幕
     */
    private void filterBarrageData () {
        if (barrageMap.get(currentTime) != null) {
            for (LiveVoiceCommentListBean item : barrageMap.get(currentTime)) {
                sendBarrage(item);
            }
        }
    }

    /**
     * 发送弹幕
     */
    private int lastCurrentTime;

    public void sendBarrage (LiveVoiceCommentListBean item) {
        if (item != null) {
            BarrageModel barrageModel = BarrageModel.getInstance();
            barrageModel.text = item.getContentCount() > 0 ? item.getContent() + "x" + item.getContentCount() : item.getContent();
            barrageModel.color = item.isSelf() ? BarrageModel.COLOR_RED : barrageModel.randomColor();
            barrageModel.isClear = lastCurrentTime != currentTime;
            if (!item.isSelf() && item.getSend_user_info() != null) {
                barrageModel.userId = item.getSend_user_info().getUid();
                barrageModel.avatar = item.getSend_user_info().getAvatar();
            } else {
                barrageModel.userId = PreferencesUtils.getString(PreferencesKey.USER_ID, "");
                barrageModel.avatar = PreferencesUtils.getString(PreferencesKey.USER_AVATOR, "");
            }
            barrageManager.send(barrageModel);
            lastCurrentTime = currentTime;
        }
    }

    /**
     * 旋转动画继续
     */
    public void resumeAnimation () {
        if (null != mCurrentHolder && null != mCurrentHolder.viewRotation) {
            mCurrentHolder.viewRotation.resumeAnimation();
        }
    }

    /**
     * 旋转动画暂停
     */
    public void pauseAnimation () {
        if (null != mCurrentHolder && null != mCurrentHolder.viewRotation) {
            mCurrentHolder.viewRotation.pauseAnimation();
        }
    }

    /**
     * 旋转动画取消
     */
    public void cancelAnimation () {
        if (null != mCurrentHolder && null != mCurrentHolder.viewRotation) {
            mCurrentHolder.viewRotation.cancelAnimation();
            mCurrentHolder.viewRotation.clearAnimation();
        }
    }

    /**
     * 所有有关注地方关注后改变数据状态
     */
    public void newDataAfterAttention (AttentionEvent event) {
        for (PhonicListBean.ListBean bean : getList()) {
            if (TextUtils.equals(bean.getUser_id(), event.userId)) {
                //后端字段不统一，首页0-未关注，1-已关注，其它地方1 - 未关注 2 - 互粉 3 - 已关注
                bean.setIs_attention(event.statues == 1 ? 0 : 1);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 所有有点赞地方点赞后改变数据状态
     */
    public void newDataAfterPraise (PraiseEvent event) {
        for (PhonicListBean.ListBean bean : getList()) {
            if (bean.getId() == event.id) {
                bean.setIs_praise(event.praiseStatus);
                bean.setPraise_num(event.praiseNumber);
                break;
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 后台没做未登录关注、点赞状态处理
     * 本地写个登录状态，登录或取消登录改变关注、点赞状态
     */
    public void setLocalLoginStatus () {
        for (PhonicListBean.ListBean bean : getList()) {
            bean.setLogin(LoginUtil.checkLogin());
        }
        notifyDataSetChanged();
    }

    /**
     * 计算弹幕空间:
     * 昵称以上减去头部布局
     */
    private void measureBarrageContainerHeight (@NonNull ViewHolder holder) {
        if (barrageContainerHeight == 0) {
            ViewTreeObserver observer = holder.rlTextContainer.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout () {
                    holder.rlTextContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int height                      = holder.rlTextContainer.getTop();
                    int mainTitleAndStatusBarHeight = PreferencesUtils.getInt("MainTitleAndStatusBarHeight", DeviceUtil.dip2px(mContext, 68));
                    barrageContainerHeight = height - mainTitleAndStatusBarHeight;
                    PreferencesUtils.putInt("BarrageContainerHeight", barrageContainerHeight);
                    PreferencesUtils.putInt("MainTitleAndStatusBarHeight", mainTitleAndStatusBarHeight);
                }
            });
        }
    }

    /**
     * 创建广告位
     */
    private void initAd (ViewHolder holder, int position) {
        try {
            QCiVoiceSdk.get().createAdNative((Activity) mContext);
            QCiVoiceSdk.get().addCustomAudioAd(position, getAdAttr(position), new AudioCustomQcAdListener() {

                @Override
                public void onAdReceive (QcAdManager manager, View adView) {
                    LogUtils.LOGE("onAdReceive");
                    if (adView != null) {
                        holder.flAd.addView(adView);
                        getListBean(position).setAdManager(manager);
                    }
                }

                @Override
                public void onAdExposure () {
                    LogUtils.LOGE("onAdExposure");
                }

                @Override
                public void onAdUserInfo (String userId, String avater) {
                    LogUtils.LOGE("onAdUserInfo");
                }

                @Override
                public void onAdClick () {
                    LogUtils.LOGE("onAdClick");
                }

                @Override
                public void onAdCompletion () {
                    LogUtils.LOGE("onAdCompletion");
                    listener.playEnd(mCurrentPosition + 1, 5000);
                }

                @Override
                public void onAdError (String fail) {
                    LogUtils.LOGE("fail:" + fail);
                    getList().remove(position);
                    notifyItemChanged(position);
                }
            });
        } catch (Exception e) {
            LogUtils.LOGE("exception:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private AdAttr getAdAttr (int position) {
        return AdAttr.newBuild()
                .setAdid(getListBean(position - 1).getAdId())
                .setMid(getListBean(position - 1).getmId())
                .setCanPause(true)
                //********设置中间图片********//
                .setCoverSize(180)//设置封面图片的大小 ,单位dp 默认
                //********设置Title的方法********//
                .setTitleColor(Color.parseColor("#FFFFFFFF"))//设置标题的颜色#FF3D3B3B
                .setContentColor(Color.parseColor("#FFFFFFFF"))//设置展示内容的颜色
                .setTitleMagin(15, 100, 0, 0)//设置标题的magin值,单位dp
                //********设置左下角的Info窗口的方法********//
                .setInfoMagin(0, 0, 15, 80)//设置信息的magin值,单位dp
                .setInfoButtonBackgroundColor(Color.parseColor("#FF5BC0DE"))//设置信息内部按钮背景颜色
                //********设置右下角头像的方法********//
                .setAdHeadMagin(0, 0, 15, 300)//设置Logo的magin值,单位dp
                //********设置右下角点赞图片的方法********//
                .setPraiseNumberColor(Color.parseColor("#FFFFFFFF"))//设置点赞数量的颜色#FFFF5555
                .setBarrageNumberColor(Color.parseColor("#FFFFFFFF"))//设置弹幕数量的颜色#FFFF5555
                .setPraiseImageMagin(0, 0, 15, 240)//设置点赞图片的magin值,单位dp
                .setPraiseNumberMagin(0, 0, 15, 220)//设置点赞数量的的magin值,单位dp
                .setBarrageImageMagin(0, 0, 15, 170)//设置弹幕图片的magin值,单位dp
                .setBarrageNumberMagin(0, 0, 15, 150)//设置弹幕数量的magin值,单位dp
                ;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final RelativeLayout      rlNormal;
        private final ImageView           ivBackground;
        private final LikeView            likeView;
        private final LottieAnimationView viewRotation;
        private final CircleImageView     viewCircleHead;
        private final ImageView           ivPlayIcon;
        private final VoicePlayRightView  voicePlayRightView;
        private final FrameLayout         flBarrage;
        private final RelativeLayout      rlBar;
        private final RelativeLayout      rlLoadingBar;
        private final PlayerControlView   progressBarView;
        private final RelativeLayout      rlTextContainer;
        private final TextView            tvNickname;
        private final TextView            tvPhonicDesc;
        private final FrameLayout         flAd;

        ViewHolder (View itemView) {
            super(itemView);
            rlNormal = itemView.findViewById(R.id.rl_normal);
            ivBackground = itemView.findViewById(R.id.iv_background);
            likeView = itemView.findViewById(R.id.like_view);
            viewRotation = itemView.findViewById(R.id.view_rotation);
            viewCircleHead = itemView.findViewById(R.id.view_circleHead);
            ivPlayIcon = itemView.findViewById(R.id.iv_playIcon);
            voicePlayRightView = itemView.findViewById(R.id.voice_playRightView);
            flBarrage = itemView.findViewById(R.id.fl_barrage);
            rlBar = itemView.findViewById(R.id.rl_bar);
            rlLoadingBar = itemView.findViewById(R.id.rl_loadingBar);
            progressBarView = itemView.findViewById(R.id.progressBar_view);
            rlTextContainer = itemView.findViewById(R.id.rl_textContainer);
            tvNickname = itemView.findViewById(R.id.tv_nickname);
            tvPhonicDesc = itemView.findViewById(R.id.tv_phonicDesc);
            flAd = itemView.findViewById(R.id.fl_ad);
        }
    }

    public interface VoicePlayAdapterListener {
        void onPageSelected (int itemPosition);

        void getBarrageData (int voiceId, int beginSeconds, int endSeconds);

        void playEnd (int position, int delay);
    }
}
