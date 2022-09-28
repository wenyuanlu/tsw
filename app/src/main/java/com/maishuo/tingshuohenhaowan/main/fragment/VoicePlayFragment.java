package com.maishuo.tingshuohenhaowan.main.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.maishuo.tingshuohenhaowan.bean.LogOutSuccessEvent;
import com.maishuo.tingshuohenhaowan.bean.LoginEvent;
import com.maishuo.tingshuohenhaowan.common.CustomFragment;
import com.maishuo.tingshuohenhaowan.databinding.FragmentPlayVoiceBinding;
import com.maishuo.tingshuohenhaowan.api.response.LiveVoiceCommentListBean;
import com.maishuo.tingshuohenhaowan.api.response.PhonicListBean;
import com.maishuo.tingshuohenhaowan.api.response.PhonicTagBean;
import com.maishuo.tingshuohenhaowan.main.adapter.VoicePlayAdapter;
import com.maishuo.tingshuohenhaowan.main.event.AttentionEvent;
import com.maishuo.tingshuohenhaowan.main.event.DeleteProductionEvent;
import com.maishuo.tingshuohenhaowan.main.event.MainConfigEvent;
import com.maishuo.tingshuohenhaowan.main.event.PraiseEvent;
import com.maishuo.tingshuohenhaowan.main.event.UpdatePersonalInfoEvent;
import com.maishuo.tingshuohenhaowan.main.model.VoicePlayModel;
import com.maishuo.tingshuohenhaowan.utils.LoginUtil;
import com.maishuo.tingshuohenhaowan.widget.proload.VideoPreLoadFuture;
import com.qichuang.commonlibs.common.CommonHandler;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Seven
 * date : 5/25/21
 * description :音频播放列表
 */
public class VoicePlayFragment extends CustomFragment<FragmentPlayVoiceBinding> implements VoicePlayAdapter.VoicePlayAdapterListener, VoicePlayModel.VoicePlayModelCallBack {
    public static final String           VOICE_TYPE    = "VOICE_TYPE";
    public static final String           VOICE_ID      = "VOICE_ID";
    public static final String           VOICE_INDEX   = "VOICE_INDEX";
    public static final String           VOICE_DATA    = "VOICE_DATA";
    public static final String           VOICE_USER_ID = "VOICE_USER_ID";
    private             int              type;//0：关注，1：推荐，2：个人中心作品
    private             int              tagId;//音频标签，0为全部
    private             VoicePlayAdapter adapter;
    private             VoicePlayModel   voicePlayModel;

    private VideoPreLoadFuture preLoadAudioPlayer;//预加载核心类-主音频
    private VideoPreLoadFuture preLoadBackgroundAudioPlayer;//预加载核心类-背景音频

    private CommonHandler mHandler;
    private boolean       isLoadedPersonalInfo;//是否加载过个人信息，防止重复加载
    private boolean       isClear;//是否清空数据，筛选标签和关注页登录成功

    //单留声作品页面和自己或者别人的作品列表进入播放
    private String         stayVoiceId;
    private int            playPosition;
    private String         userId;
    private PhonicListBean phonicListBean;

    //切换账号上一个用户id
    private String oldUserId = PreferencesUtils.getString(PreferencesKey.USER_ID);

    //是否在播放音频
    public boolean isPlaying () {
        return adapter != null && adapter.audioPlayerManager.isPlaying();
    }

    public VoicePlayFragment () {
    }

    /**
     * 关注、推荐留声
     */
    public static VoicePlayFragment getInstance (int type) {
        VoicePlayFragment fragment = new VoicePlayFragment();
        Bundle            bundle   = new Bundle();
        bundle.putInt(VOICE_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 通过请求接口的单页面留声
     */
    public static VoicePlayFragment getInstance (int type, String stayVoiceId) {
        VoicePlayFragment fragment = new VoicePlayFragment();
        Bundle            bundle   = new Bundle();
        bundle.putInt(VOICE_TYPE, type);
        bundle.putString(VOICE_ID, stayVoiceId);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 通过传列表数据的列表留声
     */
    public static VoicePlayFragment getInstance (int type, int position, String userId, PhonicListBean phonicListBean) {
        VoicePlayFragment fragment = new VoicePlayFragment();
        Bundle            bundle   = new Bundle();
        bundle.putInt(VOICE_TYPE, type);
        bundle.putInt(VOICE_INDEX, position);
        bundle.putString(VOICE_USER_ID, userId);
        bundle.putSerializable(VOICE_DATA, phonicListBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView () {
        EventBus.getDefault().register(this);

        Bundle bundle = getArguments();
        if (null != bundle && bundle.containsKey(VOICE_TYPE)) {
            type = bundle.getInt(VOICE_TYPE, 1);
            stayVoiceId = bundle.getString(VOICE_ID, "");
            playPosition = bundle.getInt(VOICE_INDEX, 0);
            userId = bundle.getString(VOICE_USER_ID, "");
            phonicListBean = (PhonicListBean) bundle.getSerializable(VOICE_DATA);
        }
    }

    @Override
    protected void initData () {
        if (type == 0) initAttention();
        else if (type == 1) initRequestData();
        else if (type == 2) otherPlayPage();
    }

    /**
     * 关注
     */
    private void initAttention () {
        if (!LoginUtil.checkLogin()) {
            vb.rlNoLogin.setVisibility(View.VISIBLE);
            vb.playRecyclerView.setEnableLoadMore(false);
            vb.playRecyclerView.setEnablePullRefresh(true);
            vb.playRecyclerView.setComplete(true);
        }
        vb.tvLogin.setOnClickListener(view -> {
            LoginUtil.isLogin(getContext());
        });
        initRequestData();
    }

    /**
     * 关注和推荐
     */
    private void initRequestData () {
        initAdapter();
        initVoicePlayModel();
        initPreLoad();
    }

    /**
     * 1、单留声作品
     * 2、自己或者别人发布的作品
     */
    private void otherPlayPage () {
        initRequestData();
        adapter.tabId = 2;
        if (TextUtils.isEmpty(stayVoiceId)) {
            setPhonicListData(phonicListBean);
            vb.playRecyclerView.setComplete(true);
            vb.playRecyclerView.scrollToPosition(playPosition);
        } else {
            vb.playRecyclerView.setEnableLoadMore(false);
            getSinglePhonicListData();
        }
    }

    /**
     * 初始化Adapter
     */
    private void initAdapter () {
        if (adapter == null) {
            adapter = new VoicePlayAdapter(getContext(), this, type, userId);
            vb.playRecyclerView.setAdapter(adapter);
            vb.playRecyclerView.setLoadMoreListener(this::getPhonicListData);
        }
    }

    /**
     * 初始化数据请求类
     */
    private void initVoicePlayModel () {
        if (voicePlayModel == null) {
            voicePlayModel = new VoicePlayModel(getAttachActivity(), this);
        }
    }

    /**
     * 获取弹幕数据
     */
    @Override
    public void getBarrageData (int voiceId, int beginSeconds, int endSeconds) {
        voicePlayModel.getBarrageData(voiceId, beginSeconds, endSeconds);
    }

    /**
     * 设置弹幕数据
     */
    @Override
    public void setBarrageData (List<LiveVoiceCommentListBean> barrageList) {
        adapter.setBarrageData(barrageList);
    }

    /**
     * 初始化预加载类
     */
    protected void initPreLoad () {
        if (preLoadAudioPlayer == null) {
            preLoadAudioPlayer = new VideoPreLoadFuture(getContext(), adapter.proBusIdAudio + type);
        }
        if (preLoadBackgroundAudioPlayer == null) {
            preLoadBackgroundAudioPlayer = new VideoPreLoadFuture(getContext(), adapter.proBusIdBgAudio + type);
        }
    }

    /**
     * 获取留声列表数据
     */
    public void getPhonicListData () {
        if (LoginUtil.checkLogin()) {
            voicePlayModel.getPhonicListData(type == 0 ? 1 : 0, tagId, PreferencesUtils.getString(PreferencesKey.USER_ID, ""));
        } else if (type == 1) {
            voicePlayModel.getPhonicListData(0, tagId, PreferencesUtils.getString(PreferencesKey.USER_ID, ""));
        }
    }

    /**
     * 获取单个留声列表数据
     */
    public void getSinglePhonicListData () {
        voicePlayModel.getSinglePhonicListData(stayVoiceId);
    }

    /**
     * 设置留声列表数据
     * 推荐页最后一条设置广告标识
     */
    @Override
    public void setPhonicListData (PhonicListBean bean) {
        //重置列表数据
        if (isClear) {
            adapter.clearListData();
            isClear = false;
        }
        if (bean != null && bean.getList() != null && !bean.getList().isEmpty()) {
            //隐藏无数据页面
            vb.tvEmpty.setVisibility(View.GONE);
            //推荐页最后一条添加广告
            if (type == 1 && PreferencesUtils.getInt(PreferencesKey.ENABLE_AD, 0) == 1) {
                PhonicListBean.ListBean listBean = new PhonicListBean.ListBean();
                listBean.setAd(true);
                bean.getList().add(listBean);
            }
            //关注页小于10条数据代表没有更多了（这样写不好，但后台不好改，暂时这样写）
            if (type == 0 && bean.getList().size() < 10) {
                vb.playRecyclerView.setComplete(true);
            }
            adapter.addList(bean.getList());
            setProLoadData();
        } else {
            if (adapter.getList().size() == 0) {
                vb.tvEmpty.setVisibility(View.VISIBLE);
            } else {
                vb.playRecyclerView.setComplete(true);
            }
        }
    }

    /**
     * 设置预加载列表
     */
    private void setProLoadData () {
        List<String> audioList   = new ArrayList();
        List<String> bgAudioList = new ArrayList();
        for (PhonicListBean.ListBean listBean : adapter.getList()) {
            audioList.add(listBean.getVoice_path());
            bgAudioList.add(listBean.getBg_music_path());
        }
        preLoadAudioPlayer.updateUrls(audioList);
        preLoadBackgroundAudioPlayer.updateUrls(bgAudioList);
        audioList.clear();
        bgAudioList.clear();
    }

    /**
     * 音频播放结束回调
     * 如果是广告会延迟5秒
     */
    @Override
    public void playEnd (int position, int delay) {
        if (delay > 0) {
            if (mHandler == null) {
                mHandler = new CommonHandler<>(this);
            }
            mHandler.postDelay(delay, () -> vb.playRecyclerView.smoothScrollToPosition(position));
        } else {
            vb.playRecyclerView.smoothScrollToPosition(position);
        }

    }

    /**
     * 滑动到当前页
     */
    @Override
    public void onPageSelected (int itemPosition) {
        //是否加载过个人信息，防止重复加载
        this.isLoadedPersonalInfo = false;
        //还剩3条数据的时候加载下一页
        if (adapter != null &&
                adapter.getList().size() - 3 <= itemPosition &&
                type == 1) {
            getPhonicListData();
        }
        //上报当前播放音频
        uploadNowPlayEvent(itemPosition);
        //更新锁屏页
        updateLockScreenEvent(itemPosition);
        //防止广告回调结束后手动滑走
        closeCommonHandler();
    }

    /**
     * 上报完播信息
     */
    public void uploadNowPlayEvent (int itemPosition) {
        if (adapter != null &&
                0 != itemPosition &&
                itemPosition < adapter.getList().size() &&
                !isAdPage() &&
                type != 2) {
            voicePlayModel.uploadNowPlayEvent(adapter.getListBean(itemPosition).getId(), adapter.isSeek, adapter.currentTime * 1000);
        }
    }

    /**
     * 更新锁屏页
     */
    public void updateLockScreenEvent (int itemPosition) {
        if (adapter != null && adapter.tabId == type) {
            PhonicListBean.ListBean bean = adapter.getListBean(itemPosition);
            PreferencesUtils.putString(PreferencesKey.LOCK_BG_IMAGE, bean.getBg_img());
            PreferencesUtils.putString(PreferencesKey.LOCK_IMAGE, bean.getImage_path());
            PreferencesUtils.putString(PreferencesKey.LOCK_DESC, bean.getDesc());
            EventBus.getDefault().post(bean);
        }
    }

    /**
     * 更新个人信息
     */
    public void updatePersonalInfo () {
        if (adapter != null && type == 1 && !isLoadedPersonalInfo) {
            isLoadedPersonalInfo = true;
            PhonicListBean.ListBean bean = adapter.getListBean(adapter.mCurrentPosition);
            EventBus.getDefault().post(new UpdatePersonalInfoEvent(bean.getUser_id()));
        }
    }

    /**
     * 1-网络变化监听
     * 2-音频播放暂停监听
     * 3-弹幕播放暂停发送监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (MainConfigEvent event) {
        if (event == null || adapter == null) return;
        switch (event.getType()) {
            case 1:
                networkConnected(event.isConnect());
                break;
            case 2:
                handleVoicePlayOrPause(event);
                break;
            case 3:
                sendBarrage(event.getBarrageContent());
                break;
            default:
                break;
        }
    }

    /**
     * 网络连接成功：
     * 无列表数据请求列表数据
     * 有列表数据继续播放音频
     */
    private void networkConnected (boolean isConnect) {
        if (isConnect) {
            if (adapter.getList().isEmpty()) {
                getPhonicListData();
            } else {
                adapter.voiceSeekTo();
            }
            vb.tvNoNetwork.setVisibility(View.GONE);
            if (type == 0 && !LoginUtil.checkLogin()) {
                vb.rlNoLogin.setVisibility(View.VISIBLE);
            }
        } else {
            if (adapter.getList().isEmpty()) {
                vb.tvEmpty.setVisibility(View.GONE);
                vb.rlNoLogin.setVisibility(View.GONE);
                vb.tvNoNetwork.setVisibility(View.VISIBLE);
            }
        }

    }

    /**
     * 处理不同逻辑的播放暂停
     * isEnforce()=true是发弹幕和打赏传过来的-强制播放
     * isEnforce()=false是首页各种滑动-需要判断是否有手动暂停
     */
    private void handleVoicePlayOrPause (MainConfigEvent event) {
        if (event.isPlay()) {
            adapter.tabId = event.getTabId();
            if (event.isEnforce()) {
                adapter.voicePlayBySendBarrage();
            } else {
                play();
            }
        } else {
            if (event.isEnforce()) {
                adapter.voicePauseAndSendCurrentTime();
            } else {
                pause();
            }
        }
    }

    /**
     * 发送单条弹幕
     * 发送礼物弹幕
     */
    public void sendBarrage (String barrageContent) {
        if (adapter != null) {
            LiveVoiceCommentListBean bean = new LiveVoiceCommentListBean();
            bean.setSelf(true);
            bean.setContent(barrageContent);
            adapter.sendBarrage(bean);
        }
    }

    /**
     * 当前是否是广告页
     */
    public boolean isAdPage () {
        if (adapter != null) {
            return adapter.getListBean(adapter.mCurrentPosition).isAd();
        }
        return false;
    }

    /**
     * 关注后更新数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (AttentionEvent event) {
        //接收数据,用于更新列表数据的关注状态
        if (event != null && adapter != null) {
            //如果关注列表没有数据，就刷新列表，有数据则改变关注状态
            if (adapter.getList().isEmpty() && type == 0) {
                onMessageEvent(new LoginEvent());
            } else {
                adapter.newDataAfterAttention(event);
            }
        }
    }

    /**
     * 其它页面点赞后更新状态
     * event.type == 2:只处理单留声页面点赞
     * type != 2：单留声页面改变首页留声页面状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (PraiseEvent event) {
        //接收数据,用于更新列表数据的点赞状态
        if (event != null && adapter != null && event.type == 2 && type != 2) {
            adapter.newDataAfterPraise(event);
        }
    }

    /**
     * 选择音频标签后更新数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (PhonicTagBean event) {
        if (adapter != null && event != null && type == 1 && tagId != event.getId()) {
            tagId = event.getId();
            pullRefresh();
        }
    }

    /**
     * App登出成功回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (LogOutSuccessEvent event) {
        if (type == 0) {
            adapter.voiceStop();
            adapter.clearListData();
            vb.tvEmpty.setVisibility(View.GONE);
            vb.rlNoLogin.setVisibility(View.VISIBLE);
            vb.playRecyclerView.setEnableLoadMore(false);
            vb.playRecyclerView.setEnablePullRefresh(true);
            vb.playRecyclerView.setComplete(true);
        } else {
            adapter.setLocalLoginStatus();
        }
    }

    /**
     * App登录成功回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (LoginEvent event) {
        if (type == 0) {
            vb.rlNoLogin.setVisibility(View.GONE);
            vb.playRecyclerView.setEnableLoadMore(true);
            vb.playRecyclerView.setEnablePullRefresh(false);
            vb.playRecyclerView.setComplete(false);
        }
        checkIsRefresh();
    }

    /**
     * 登录后是否需要刷新留声列表：
     * 切换的账号是上一个登录账号不刷新
     * 切换的账号不是上一个登录账号刷新
     * 第一次登录账号刷新
     */
    private void checkIsRefresh () {
        if (TextUtils.isEmpty(oldUserId) || !TextUtils.equals(oldUserId, PreferencesUtils.getString(PreferencesKey.USER_ID))) {
            pullRefresh();
        }
    }

    /**
     * 删除作品
     * 单个作品直接finish页面
     * 多个作品移除删除作品
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (DeleteProductionEvent event) {
        if (null != event && type == 2 && adapter != null) {
            if (adapter.getList().size() > 1) {
                int deletePosition = adapter.mCurrentPosition;
                adapter.getList().remove(deletePosition);
                adapter.notifyItemRemoved(deletePosition);
                //判断是否是最后一条数据(remove后不用减1)
                if (adapter.getList().size() == deletePosition) {
                    adapter.replay(deletePosition - 1);
                } else {
                    adapter.replay(deletePosition);
                }
            } else {
                getActivity().finish();
            }
        }
    }

    /**
     * 播放音频
     */
    public void play () {
        if (adapter != null) {
            adapter.voicePlay();
        }
    }

    /**
     * 暂停音频
     */
    public void pause () {
        if (adapter != null) {
            adapter.voicePause();
        }
    }

    /**
     * 下拉刷新
     */
    public void pullRefresh () {
        isClear = true;
        getPhonicListData();
        vb.playRecyclerView.setComplete(false);
        vb.playRecyclerView.scrollToPosition(0);
        vb.playRecyclerView.resetSelect();
    }

    @Override
    public void onResume () {
        super.onResume();
        if (adapter != null && !adapter.getList().isEmpty() && adapter.isBackground) {
            adapter.isBackground = false;
            adapter.onReady();
        }
    }

    @Override
    public void onStop () {
        super.onStop();
        if (adapter != null) {
            adapter.isBackground = true;
            adapter.clearCountDown();
        }
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        if (adapter != null) {
            adapter.release();
        }
        closeCommonHandler();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 结束Handler，防止内存泄漏
     */
    private void closeCommonHandler () {
        if (mHandler != null) {
            mHandler.releaseHandler();
            mHandler = null;
        }
    }
}
