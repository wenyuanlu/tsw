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
 * author ???Seven
 * date : 5/25/21
 * description :??????????????????
 */
public class VoicePlayAdapter extends PlayAdapter<VoicePlayAdapter.ViewHolder> implements AudioPlayerManager.DefaultEventListener, BarrageManager.BarrageCallBack {
    public final  String                        proBusIdAudio   = "AudioPlayer";
    public final  String                        proBusIdBgAudio = "BgAudioPlayer";
    private final Context                       mContext;
    private final int                           type;               //0????????????1????????????2?????????????????????
    public        int                           tabId           = 1;//0????????????1?????????
    private final String                        userId;
    private final VoicePlayAdapterListener      listener;
    private final List<PhonicListBean.ListBean> list;
    private       Handler                       mHandler;

    private ViewHolder mCurrentHolder;
    public  int        mCurrentPosition;

    public final  AudioPlayerManager           audioPlayerManager;
    private final BackgroundAudioPlayerManager backgroundAudioPlayerManager;
    private       boolean                      isHandClick;//?????????????????????

    private final Map<Integer, List<LiveVoiceCommentListBean>> barrageMap;
    private       BarrageManager                               barrageManager;
    private       int                                          barrageCount;//???????????????30????????????
    private       List<Integer>                                barrageCountList;//???????????????????????????????????????
    private       int                                          barrageContainerHeight;//?????????????????????

    private PreLoadManager preLoadManager;//??????????????????
    public  boolean        isBackground;//??????????????????????????????
    public  int            isSeek;//???????????????

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
            //?????????
            holder.flAd.removeAllViews();
            holder.rlNormal.setVisibility(View.GONE);
            initAd(holder, position);
        } else {
            //???????????????
            holder.flAd.removeAllViews();
            holder.rlNormal.setVisibility(View.VISIBLE);

            //????????????
            GlideUtils.INSTANCE.loadImage(mContext, itemBean.getBg_img(), holder.ivBackground);
            //???????????????
            GlideUtils.INSTANCE.loadImage(mContext, itemBean.getImage_path(), holder.viewCircleHead);

            //?????????
            holder.tvNickname.setText(String.format("@%s", TextUtils.isEmpty(itemBean.getUname()) ? "" : itemBean.getUname()));
            //???????????????
            if (type != 2) {
                holder.tvNickname.setOnClickListener(view -> clickBarrageHeadView(itemBean.getUser_id()));
            }

            //??????
            holder.tvPhonicDesc.setText(TextUtils.isEmpty(itemBean.getDesc()) ? "" : itemBean.getDesc());
            //????????????
            holder.voicePlayRightView.setInitData(itemBean, type, userId);
            //????????????
            holder.likeView.setOnLikeListener(() -> holder.voicePlayRightView.praise(true));
            //?????????????????????
            holder.likeView.setOnPlayPauseListener(this::clickPlayOrPause);
            holder.viewCircleHead.setOnClickListener(view -> clickPlayOrPause());

            //??????????????????????????????????????????
            measureBarrageContainerHeight(holder);
        }
    }

    /**
     * ??????????????????
     */
    @Override
    public void clickBarrageView () {
        if (barrageManager != null) {
            barrageManager.setHandClick(isHandClick);
        }
    }

    /**
     * ??????????????????
     */
    @Override
    public void clickBarrageHeadView (String userId) {
        voicePause();
        Intent intent = new Intent(mContext, PersonCenterActivity.class);
        intent.putExtra("userId", userId);
        ((Activity) mContext).startActivityForResult(intent, 0);
    }

    /**
     * ???????????????
     * ????????????
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
        //??????????????????getListBean(mCurrentPosition)????????????type=2????????????????????????
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
            EventBus.getDefault().post(new MainMenuEvent(false));//??????????????????
            getListBean(itemPosition).getAdManager().startPlayAd();
        } else {
            EventBus.getDefault().post(new MainMenuEvent(true));//??????????????????
            listener.onPageSelected(itemPosition);
            initBarrage(itemPosition);
            initVoicePlayer(itemPosition);
        }
    }

    /**
     * ??????????????????
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

        //?????????????????????Url
        preLoadManager.currentVideoPlay(proBusIdAudio + type, getListBean(itemPosition).getVoice_path());
        preLoadManager.currentVideoPlay(proBusIdBgAudio + type, getListBean(itemPosition).getBg_music_path());
    }

    /**
     * ??????????????????
     * ???????????????
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
     * ????????????
     * 1?????????????????????????????????
     * 2?????????????????????????????????
     */
    @Override
    public void onEnd () {
        //????????????,1-???????????????0-????????????
        int reportType = PreferencesUtils.getInt(PreferencesKey.PLAY_TYPE, 0);
        if (1 == reportType || getList().size() - 1 == mCurrentPosition) {
            replay(mCurrentPosition);
        } else {
            listener.playEnd(mCurrentPosition + 1, 0);
        }
    }

    /**
     * ????????????
     */
    @Override
    public void onError (String msg) {
        cancelAnimation();
        clearCountDown();
        showLoadingView();
    }

    /**
     * ????????????????????????
     */
    @Override
    public void isSeek () {
        isSeek = 1;
    }

    /**
     * ??????
     */
    public void replay (int position) {
        mCurrentPosition = position;
        voiceStop();
        initVoicePlayer(position);
    }

    /**
     * ????????????
     */
    public void voiceStop () {
        if (audioPlayerManager != null && backgroundAudioPlayerManager != null) {
            audioPlayerManager.stop(true);
            backgroundAudioPlayerManager.stop(true);
        }
    }

    /**
     * ????????????????????????
     */
    public void clickPlayOrPause () {
        if (!PreferencesUtils.getBoolean("playingFirstAd", false)) {
            isHandClick = audioPlayerManager.isPlaying();
            audioPlayerManager.playOrPause();
            backgroundAudioPlayerManager.playOrPause();
        }
    }

    /**
     * ??????:???????????????
     * ????????????????????????
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
        //????????????pop
        if (barrageManager != null) {
            barrageManager.releasePopup();
        }
    }

    /**
     * ????????????????????????????????????
     * ???????????????????????????
     */
    public void voicePlayBySendBarrage () {
        if (tabId == type && !PreferencesUtils.getBoolean("playingFirstAd", false)) {
            isHandClick = false;
            audioPlayerManager.start();
            backgroundAudioPlayerManager.play();
        }
    }

    /**
     * ?????????????????????
     */
    public void voiceSeekTo () {
        //?????????????????????tab
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
     * ??????:???????????????
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
     * VoicePlayRightView?????????????????????????????????????????????EvenBus
     * ????????????????????????????????????????????????????????????????????????????????????
     * ????????????????????????????????????
     */
    public void voicePauseAndSendCurrentTime () {
        if (tabId == type) {
            PreferencesUtils.putInt("currentTime", currentTime);
        }
        voicePause();
    }

    /**
     * ???????????????
     */
    private void showProgressBarView () {
        mCurrentHolder.progressBarView.setVisibility(barrageCount > 1 ? View.VISIBLE : View.GONE);
        mCurrentHolder.rlLoadingBar.setVisibility(View.INVISIBLE);
    }

    /**
     * ???????????????
     */
    private void showLoadingView () {
        mCurrentHolder.progressBarView.setVisibility(View.INVISIBLE);
        mCurrentHolder.rlLoadingBar.setVisibility(View.VISIBLE);
    }

    /**
     * ???????????????
     */
    private void initBarrage (int itemPosition) {
        if (barrageManager == null) {
            barrageManager = new BarrageManager();
        }
        barrageManager.setBarrageCallBack(this);
        barrageManager.init(mContext, mCurrentHolder.flBarrage); // ??????????????????init??????
        barrageCount = getListBean(mCurrentPosition).getBarrageCount();
        barrageCountList = new ArrayList<>();
        if (!isBackground) {
            listener.getBarrageData(getListBean(itemPosition).getId(), 0, Math.min(getListBean(itemPosition).getVoice_time() / 1000, 30));
            barrageCountList.add(0);
        }
    }

    /**
     * ??????????????????-?????????
     */
    public void setBarrageData (List<LiveVoiceCommentListBean> barrageList) {
        //????????????????????????????????????
        for (LiveVoiceCommentListBean beanI : barrageList) {
            List<LiveVoiceCommentListBean> copyBarrageList = new ArrayList<>();
            for (LiveVoiceCommentListBean beanJ : barrageList) {
                if (beanI.getSeconds() == beanJ.getSeconds()) {
//                    //????????????????????????
//                    if (beanI.getContent().contains("??????") &&
//                            TextUtils.equals(beanI.getContent(), beanJ.getContent())) {
//                        beanJ.setContentCount(beanJ.getContentCount() + 1);
//                    }
                    copyBarrageList.add(beanJ);
                }
            }
            //??????hashSet??????
            LinkedHashSet                  hashSet               = new LinkedHashSet<>(copyBarrageList);
            List<LiveVoiceCommentListBean> listWithoutDuplicates = new ArrayList<>(hashSet);
            barrageMap.put(beanI.getSeconds(), listWithoutDuplicates);
        }
    }

    /**
     * ????????????
     */
    public void pauseBarrage () {
        if (null != barrageManager) {
            barrageManager.pause();
        }
    }

    /**
     * ????????????
     */
    public void resumeBarrage () {
        if (null != barrageManager) {
            barrageManager.resume();
        }
    }

    /**
     * ???????????????
     */
    public void clearCountDown () {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
            mHandler = null;
        }
    }

    /**
     * ???????????????
     */
    public void startCountDown () {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.post(mRunnable);
    }

    /**
     * ????????????
     * ????????????
     */
    public        int      currentTime = 0;
    private final Runnable mRunnable   = new Runnable() {
        @Override
        public void run () {
            //????????????????????????
            currentTime = (int) (audioPlayerManager.getCurrentPosition() / 1000);
            //??????30???????????????
            getNextPageBarrage();
            //??????????????????????????????????????????????????????
            filterBarrageData();
            //???????????????????????????????????????????????????
            mHandler.postDelayed(this, 1000);
        }
    };

    /**
     * ??????30???????????????
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
     * ??????????????????????????????????????????????????????
     */
    private void filterBarrageData () {
        if (barrageMap.get(currentTime) != null) {
            for (LiveVoiceCommentListBean item : barrageMap.get(currentTime)) {
                sendBarrage(item);
            }
        }
    }

    /**
     * ????????????
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
     * ??????????????????
     */
    public void resumeAnimation () {
        if (null != mCurrentHolder && null != mCurrentHolder.viewRotation) {
            mCurrentHolder.viewRotation.resumeAnimation();
        }
    }

    /**
     * ??????????????????
     */
    public void pauseAnimation () {
        if (null != mCurrentHolder && null != mCurrentHolder.viewRotation) {
            mCurrentHolder.viewRotation.pauseAnimation();
        }
    }

    /**
     * ??????????????????
     */
    public void cancelAnimation () {
        if (null != mCurrentHolder && null != mCurrentHolder.viewRotation) {
            mCurrentHolder.viewRotation.cancelAnimation();
            mCurrentHolder.viewRotation.clearAnimation();
        }
    }

    /**
     * ????????????????????????????????????????????????
     */
    public void newDataAfterAttention (AttentionEvent event) {
        for (PhonicListBean.ListBean bean : getList()) {
            if (TextUtils.equals(bean.getUser_id(), event.userId)) {
                //??????????????????????????????0-????????????1-????????????????????????1 - ????????? 2 - ?????? 3 - ?????????
                bean.setIs_attention(event.statues == 1 ? 0 : 1);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * ????????????????????????????????????????????????
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
     * ????????????????????????????????????????????????
     * ???????????????????????????????????????????????????????????????????????????
     */
    public void setLocalLoginStatus () {
        for (PhonicListBean.ListBean bean : getList()) {
            bean.setLogin(LoginUtil.checkLogin());
        }
        notifyDataSetChanged();
    }

    /**
     * ??????????????????:
     * ??????????????????????????????
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
     * ???????????????
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
                //********??????????????????********//
                .setCoverSize(180)//??????????????????????????? ,??????dp ??????
                //********??????Title?????????********//
                .setTitleColor(Color.parseColor("#FFFFFFFF"))//?????????????????????#FF3D3B3B
                .setContentColor(Color.parseColor("#FFFFFFFF"))//???????????????????????????
                .setTitleMagin(15, 100, 0, 0)//???????????????magin???,??????dp
                //********??????????????????Info???????????????********//
                .setInfoMagin(0, 0, 15, 80)//???????????????magin???,??????dp
                .setInfoButtonBackgroundColor(Color.parseColor("#FF5BC0DE"))//????????????????????????????????????
                //********??????????????????????????????********//
                .setAdHeadMagin(0, 0, 15, 300)//??????Logo???magin???,??????dp
                //********????????????????????????????????????********//
                .setPraiseNumberColor(Color.parseColor("#FFFFFFFF"))//???????????????????????????#FFFF5555
                .setBarrageNumberColor(Color.parseColor("#FFFFFFFF"))//???????????????????????????#FFFF5555
                .setPraiseImageMagin(0, 0, 15, 240)//?????????????????????magin???,??????dp
                .setPraiseNumberMagin(0, 0, 15, 220)//????????????????????????magin???,??????dp
                .setBarrageImageMagin(0, 0, 15, 170)//?????????????????????magin???,??????dp
                .setBarrageNumberMagin(0, 0, 15, 150)//?????????????????????magin???,??????dp
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
