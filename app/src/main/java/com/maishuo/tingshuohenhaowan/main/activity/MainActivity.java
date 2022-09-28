package com.maishuo.tingshuohenhaowan.main.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.corpize.sdk.ivoice.QCiVoiceSdk;
import com.corpize.sdk.ivoice.admanager.QcAdManager;
import com.corpize.sdk.ivoice.listener.QcFirstVoiceAdViewListener;
import com.maishuo.sharelibrary.CustomShareUtils;
import com.maishuo.sharelibrary.OnActivityResultBean;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.FirstInitBean;
import com.maishuo.tingshuohenhaowan.bean.LogOutSuccessEvent;
import com.maishuo.tingshuohenhaowan.bean.LoginEvent;
import com.maishuo.tingshuohenhaowan.bean.LoginOutEvent;
import com.maishuo.tingshuohenhaowan.bean.MessageRemindEvent;
import com.maishuo.tingshuohenhaowan.bean.UserInfoEvent;
import com.maishuo.tingshuohenhaowan.common.AutoClosePlayEnum;
import com.maishuo.tingshuohenhaowan.common.Constant;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.common.UserConfig;
import com.maishuo.tingshuohenhaowan.databinding.ActivityMainBinding;
import com.maishuo.tingshuohenhaowan.greendaomanager.LocalRepository;
import com.maishuo.tingshuohenhaowan.login.ui.SchemeJumpActivity;
import com.maishuo.tingshuohenhaowan.main.event.AttentionEvent;
import com.maishuo.tingshuohenhaowan.main.event.MainConfigEvent;
import com.maishuo.tingshuohenhaowan.main.event.MainMenuEvent;
import com.maishuo.tingshuohenhaowan.main.event.PraiseEvent;
import com.maishuo.tingshuohenhaowan.main.helper.MainHelper;
import com.maishuo.tingshuohenhaowan.main.model.MainViewModel;
import com.maishuo.tingshuohenhaowan.main.dialog.CustomRecorderSelectorDialog;
import com.maishuo.tingshuohenhaowan.main.fragment.MainFragment;
import com.maishuo.tingshuohenhaowan.main.dialog.MainIndexDialog;
import com.maishuo.tingshuohenhaowan.main.dialog.ShowActivityDialog;
import com.maishuo.tingshuohenhaowan.message.ui.CustomMessageFragment;
import com.maishuo.tingshuohenhaowan.rtmchat.ChatLoginUtils;
import com.maishuo.tingshuohenhaowan.utils.LoginUtil;
import com.maishuo.tingshuohenhaowan.utils.TimeUtils;
import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils;
import com.maishuo.tingshuohenhaowan.widget.barrage.utils.BarragePopupUtil;
import com.maishuo.umeng.ConstantEventId;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.LogUtils;
import com.qichuang.commonlibs.utils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import constant.DownLoadBy;
import constant.UiType;
import listener.UpdateDownloadListener;
import model.UiConfig;
import model.UpdateConfig;
import update.UpdateAppUtils;

/**
 * author ：Seven
 * date : 5/20/21
 * description :首页，录制，消息
 */
public class MainActivity extends CustomBaseActivity<ActivityMainBinding> {
    private long exitTime = 0;

    private MainFragment                 mainFragment;
    private CustomMessageFragment        messageFragment;
    private CustomRecorderSelectorDialog customRecorderSelectorDialog;
    private String                       jumpTag;
    private boolean                      isShowFirstAd;//只播放一次首听广告
    private boolean                      isLoadMainData;//是否已经加载过初始化数据

    @Override
    protected void initView () {
        EventBus.getDefault().register(this);
        PreferencesUtils.putBoolean("playingFirstAd", true);//高端机与低端局加载首听速度不一样，统一最开始处理
        PreferencesUtils.putLong(PreferencesKey.TIMING, -1);//每次重新进入app都要重置该值
        clickEvent();
        schemeJump();
    }

    @Override
    protected void initData () {
        MainViewModel.getInstance(this).getToken();
        MainViewModel.getInstance(this).tokenLiveData.observe(this, it -> {
            resetFragment();
            openMainFragment();
            MainHelper.getInstance(this).init();
            initLiveData();
        });
    }

    /**
     * 获取token之后监听网络回调
     */
    private void initLiveData () {
        MainViewModel.getInstance(this).updateLiveData.observe(this, it -> showUpdateApp(it.getLatestVersion(), it.getLayerAd()));
        MainViewModel.getInstance(this).guideLiveData.observe(this, this::showGuideDialog);
        MainViewModel.getInstance(this).unRedLiveData.observe(this, it -> setUnreadMessageCount());
    }

    /**
     * 加载首听
     */
    private void addFirstAd (final FirstInitBean.LayerAdBean bean) {
        if (isShowFirstAd) {
            showActivityDialog(bean);
            return;
        }

        String adid = Constant.QC_FIRST_AD_ADID;
        QCiVoiceSdk.get().addFirstVoiceAd(this,
                adid,
                new QcFirstVoiceAdViewListener() {
                    @Override
                    public void onFirstVoiceAdClose () {
                        LogUtils.LOGE("onFirstVoiceAdClose");
                        onCompletion(bean);
                    }

                    @Override
                    public void onFirstVoiceAdCountDownCompletion () {
                        LogUtils.LOGE("onFirstVoiceAdCountDownCompletion");
                    }

                    @Override
                    public void onAdExposure () {
                        LogUtils.LOGE("onAdExposure");
                    }

                    @Override
                    public void onAdReceive (QcAdManager qcAdManager, View view) {
                        LogUtils.LOGE("onAdReceive");
                        vb.mainAdViewGroup.setVisibility(View.VISIBLE);

                        vb.mainAdViewGroup.removeAllViews();
                        vb.mainAdViewGroup.addView(view);
                        qcAdManager.startPlayAd();
                        isShowFirstAd = true;
                    }

                    @Override
                    public void onAdClick () {
                        LogUtils.LOGE("onAdClick");
                    }

                    @Override
                    public void onAdCompletion () {
                        LogUtils.LOGE("onAdCompletion");
                        onCompletion(bean);
                    }

                    @Override
                    public void onAdError (String s) {
                        LogUtils.LOGE("onAdError:" + s);
                        onCompletion(bean);
                    }
                });
    }

    private void onCompletion (FirstInitBean.LayerAdBean bean) {
        vb.mainAdViewGroup.removeAllViews();
        vb.mainAdViewGroup.setVisibility(View.GONE);
        showActivityDialog(bean);

        //通知留声播放
        PreferencesUtils.putBoolean("playingFirstAd", false);
        EventBus.getDefault().post(new MainConfigEvent().setType(2).setTabId(getMainFragmentItem()).setPlay(true));
    }

    /**
     * 点击事件
     */
    @SuppressLint("NonConstantResourceId")
    private void clickEvent () {
        vb.viewNavigation.setItemClickListener(itemId -> {
            switch (itemId) {
                //首页
                case R.id.lin_home:
                    openMainFragment();
                    TrackingAgentUtils.onEvent(this, ConstantEventId.NEWvoice);
                    break;
                //录制
                case R.id.lin_home_center:
                    openRecorderSelectorDialog();
                    TrackingAgentUtils.onEvent(this, ConstantEventId.NEWvoice_Record);
                    break;
                //消息
                case R.id.lin_message:
                    if (!LoginUtil.isLogin(getContext())) {
                        return;
                    }
                    openMessageFragment();
                    TrackingAgentUtils.onEvent(this, ConstantEventId.NEWvoice_news);
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * 录制弹窗
     */
    private void openRecorderSelectorDialog () {
        try {
            if (!LoginUtil.isLogin(getContext())) {
                return;
            }

            if (getActivity() instanceof AppCompatActivity) {
                if (null != customRecorderSelectorDialog && customRecorderSelectorDialog.isShowing()) {
                    return;
                }

                customRecorderSelectorDialog = new CustomRecorderSelectorDialog((AppCompatActivity) getActivity());
                customRecorderSelectorDialog.showDialog();
                convertJumpData();
                EventBus.getDefault().post(new MainConfigEvent().setType(2).setPlay(false));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理跳转带参数据
     */
    private void convertJumpData () {
        if (TextUtils.isEmpty(jumpTag)) {
            return;
        }
        Uri uri = Uri.parse(jumpTag);
        //tag标签
        jumpTag = uri.getQueryParameter("tag");
        //edit是否可编辑
        String edit = uri.getQueryParameter("edit");
        //判断活动标签是否可以点击
        String isCanCheckActivityTag = uri.getQueryParameter("isCanCheckActivityTag");

        String[]           jumpStr = jumpTag.split(",");
        ArrayList<Integer> list    = new ArrayList<>();

        if (null == jumpStr || jumpStr.length == 0) {
            return;
        }

        for (String s : jumpStr) {
            list.add(Integer.parseInt(s));
        }

        customRecorderSelectorDialog.setSelectorTypes(list);
        customRecorderSelectorDialog.setEdit(edit);
        customRecorderSelectorDialog.setCanCheckActivityTag(
                TextUtils.isEmpty(isCanCheckActivityTag) ?
                        -1
                        :
                        Integer.parseInt(isCanCheckActivityTag)
        );

        jumpTag = "";
    }

    /**
     * 首页
     */
    private void openMainFragment () {
        if (null == mainFragment) {
            mainFragment = new MainFragment();
            commitFragment(mainFragment);
        } else {
            if (null != messageFragment) {
                hideFragment(messageFragment);
            }
            showFragment(mainFragment);
        }
    }

    /**
     * 消息
     */
    private void openMessageFragment () {
        if (null == messageFragment) {
            messageFragment = new CustomMessageFragment();
            commitFragment(messageFragment);
            if (null != mainFragment) {
                hideFragment(mainFragment);
            }
        } else {
            if (null != mainFragment) {
                hideFragment(mainFragment);
            }
            showFragment(messageFragment);
        }
    }

    private void resetFragment () {
        FragmentManager     fm       = getSupportFragmentManager();
        FragmentTransaction ft       = fm.beginTransaction();
        Fragment            fragment = fm.findFragmentById(R.id.fl_container);
        if (fragment != null) {
            ft.remove(fragment);
            fm.popBackStack();
            ft.commit();
        }
    }

    private void commitFragment (Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fl_container, fragment);
        fragmentTransaction.commitNow();
    }

    private void showFragment (Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
            fragmentTransaction.commitNow();
        }
    }

    private void hideFragment (Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commitNow();
    }

    /**
     * 网络变化监听
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (MainConfigEvent event) {
        if (event != null && event.getType() == 1 && event.isConnect() && !isLoadMainData) {
            isLoadMainData = true;
            MainHelper.getInstance(this).loginChat();
            MainViewModel.getInstance(this).init();
            if (mainFragment != null) {
                mainFragment.getPhonicTag();
            }
        }
    }

    /**
     * 声网登出回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (LoginOutEvent event) {
        //退出登录
        ChatLoginUtils.loginOut(() -> {

        });
    }

    /**
     * App登出成功回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (LogOutSuccessEvent event) {
        MainViewModel.getInstance(this).getInitData();
        setViewPagerItem();
        closeMenu(true);
        disableMenu();
    }

    /**
     * App登录成功回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (LoginEvent event) {
        //登录成功需要弹活动窗
        PreferencesUtils.putLong(PreferencesKey.ACTIVITY_DIALOG, 0);
        //初始化数据
        MainViewModel.getInstance(this).getInitData();
        //释放右滑菜单并刷新
        unLockedRightMenuAndRefreshData();
        //刷新个人中心基础数据
        updateUserInfo();
        //初始化个人中心列表数据
        initPersonalList();
    }

    /**
     * 声网接收到消息更新红点
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (MessageRemindEvent event) {
        setUnreadMessageCount();
    }

    /**
     * 菜单开关管理：
     * 留声开启
     * 广告关闭
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (MainMenuEvent event) {
        if (event.enableMenu) {
            if (mainFragment != null) mainFragment.enableRightMenu();
        } else {
            if (mainFragment != null) mainFragment.disableRightMenu();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (AutoClosePlayEnum it) {
        try {
            if (0 == it.getValue()) {
                if (delayClosedRunnable != null) {
                    delayerHandler.removeCallbacks(delayClosedRunnable);
                }
                return;
            }

            if (delayClosedRunnable == null) {
                delayClosedRunnable = new DelayClosedRunnable();
            }

            delayerHandler.postDelayed(delayClosedRunnable, it.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final Handler delayerHandler = new Handler(Looper.getMainLooper());

    private DelayClosedRunnable delayClosedRunnable;

    private static class DelayClosedRunnable implements Runnable {
        @Override
        public void run () {
            EventBus.getDefault().post(new MainConfigEvent().setType(2).setPlay(false));
            PreferencesUtils.putLong(PreferencesKey.TIMING, -1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (UserInfoEvent event) {
        if (mainFragment != null && event != null) {
            mainFragment.changeVipOrNameInfo(event);
        }
    }

    /**
     * 作品关注后更新数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (AttentionEvent event) {
        if (event != null) {
            updateUserInfo();
        }
    }

    /**
     * 点赞自己作品后更新状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (PraiseEvent event) {
        if (event != null && TextUtils.equals(event.userId, PreferencesUtils.getString(PreferencesKey.USER_ID, ""))) {
            updateUserInfo();
        }
    }

    /**
     * 版本更新弹窗
     */
    private void showUpdateApp (FirstInitBean.LatestVersionBean latestVersionBean, FirstInitBean.LayerAdBean adBean) {
        UpdateConfig updateConfig = new UpdateConfig();
        updateConfig.setCheckWifi(true);
        updateConfig.setShowNotification(true);
        updateConfig.setAlwaysShowDownLoadDialog(false);
        updateConfig.setDownloadBy(DownLoadBy.APP);
        updateConfig.setNotifyImgRes(R.mipmap.ic_launcher);
        updateConfig.setForce(latestVersionBean.getIsForce() == 1);
        UiConfig uiConfig = new UiConfig();
        uiConfig.setUiType(UiType.CUSTOM);
        uiConfig.setCustomLayoutId(R.layout.view_update_dialog_custom);
        UpdateAppUtils
                .getInstance()
                .apkUrl(latestVersionBean.getApkUrl())
                .updateConfig(updateConfig)
                .uiConfig(uiConfig)
                .setOnInitUiListener((view, updateConfig1, uiConfig1) -> {
                    TextView tvTitle   = view.findViewById(R.id.tv_update_title);
                    TextView tvVersion = view.findViewById(R.id.tv_version_name);
                    tvTitle.setText(latestVersionBean.getTitle());
                    tvVersion.setText(String.format("V%s", latestVersionBean.getVersionName()));
                })
                .updateContent(latestVersionBean.getContent())
                .setUpdateDownloadListener(new UpdateDownloadListener() {
                    @Override
                    public void onStart () {

                    }

                    @Override
                    public void onDownload (int i) {

                    }

                    @Override
                    public void onFinish () {

                    }

                    @Override
                    public void onError (@NotNull Throwable throwable) {

                    }
                })
                .setCancelBtnClickListener(() -> {
                    postDelayed(() -> {
                        showGuideDialog(adBean);
                    }, 2000);
                    return false;
                })
                .setUpdateBtnClickListener(() -> false)
                .update();
    }

    /**
     * 首页引导页弹窗
     */
    private void showGuideDialog (FirstInitBean.LayerAdBean bean) {
        boolean isShowGuide = PreferencesUtils.getBoolean(PreferencesKey.PHONIC_GUIDE, false);
        if (!isShowGuide) {
            PreferencesUtils.putBoolean(PreferencesKey.PHONIC_GUIDE, true);
            MainIndexDialog programDialog = new MainIndexDialog(this);
            programDialog.setOnDismissListener(it -> postDelayed(() -> addFirstAd(bean), 2000));
            programDialog.showDialog();
        } else {
            addFirstAd(bean);
        }
    }

    /**
     * 首页活动弹窗
     * 用户未登录状态下，每次进入APP都弹出（需杀进程之后又进入）
     * 用户登录状态下，每日首次进入弹出一次（需杀进程之后）
     */
    private void showActivityDialog (FirstInitBean.LayerAdBean bean) {
        if (LoginUtil.checkLogin()) {
            long showDate = PreferencesUtils.getLong(PreferencesKey.ACTIVITY_DIALOG, 0);
            if (TimeUtils.IsToday(showDate)) {
                return;
            }
        }
        PreferencesUtils.putLong(PreferencesKey.ACTIVITY_DIALOG, System.currentTimeMillis());
        if (bean != null) {
            String url = bean.getImage();
            if (url != null && getContext() != null) {
                String             ldpUrl         = bean.getLdp();
                String             img            = bean.getImage();
                String             title          = bean.getName();
                ShowActivityDialog activityDialog = new ShowActivityDialog(this);
                activityDialog.setShowData(img, ldpUrl, title);
                activityDialog.showDialog();
            }
        }
    }

    /**
     * 设置右侧的未读消息的展示
     */
    public void setUnreadMessageCount () {
        if (LoginUtil.checkLogin()) {
            int unReadDan    = UserConfig.getInstance().getMessageUnreadDan();
            int unReadZan    = UserConfig.getInstance().getMessageUnreadZan();
            int unReadChat   = LocalRepository.getInstance().getUnReadCount();
            int UnReadSystem = UserConfig.getInstance().getMessageUnreadSystem();//系统消息
            vb.viewNavigation.setMessageCount(unReadDan + unReadZan + unReadChat, UnReadSystem > 0);
        } else {
            vb.viewNavigation.setMessageCount(0, false);
        }
    }

    /**
     * 检查权限回调
     */
    @Override
    public void onRequestPermissionsResult (
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (MainHelper.getInstance(this).PERMISSION_RECORD_CODE == requestCode) {
            MainHelper.getInstance(this).requestLocationPermission();
        }
    }

    /**
     * 回退逻辑处理
     * 消息页回退到首页
     * 打开了菜单回退到首页
     */
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //如果打开了消息页回退到首页
            if (0 != vb.viewNavigation.getSelectorPosition()) {
                vb.viewNavigation.selectHome();
                openMainFragment();
                return true;
            }

            //如果打开了侧滑菜单回退到首页
            if (vb.viewNavigation.getVisibility() == View.GONE) {
                closeMenu(true);
                return true;
            }

            if (Math.abs((System.currentTimeMillis() - exitTime)) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                //App使用时长统计
                long duration = PreferencesUtils.getLong("AppDuration", 0);
                long value    = System.currentTimeMillis() - duration;
                TrackingAgentUtils.setAppDuration(value);
                LogUtils.LOGE("App使用时长", System.currentTimeMillis() + "-" + duration + "=" + value);
                TrackingAgentUtils.exitSdk();

                finishAffinity();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 1、物理按键返回关闭菜单
     * 2、别人的个人中心返回关闭菜单
     * 3、Scheme跳转关闭菜单
     */
    public void closeMenu (boolean isPlay) {
        if (mainFragment != null) {
            mainFragment.closeMenu(isPlay);
        }
    }

    /**
     * 登出后禁用菜单
     */
    public void disableMenu () {
        if (mainFragment != null) {
            mainFragment.disableLeftMenu();
        }
    }

    /**
     * 设置页面到推荐页
     */
    public void setViewPagerItem () {
        if (mainFragment != null) {
            mainFragment.setViewPagerItem(1);
        }
    }

    /**
     * 1、释放右滑菜单
     * 2、刷新左右菜单数据
     */
    public void unLockedRightMenuAndRefreshData () {
        if (mainFragment != null) {
            //设置右滑菜单可用
            mainFragment.enableLeftMenu();
            //刷新左菜单
            mainFragment.initLeftMenu();
        }
    }

    /**
     * 是否在播放状态
     */
    public boolean isPlayStatus () {
        if (mainFragment != null) {
            return mainFragment.isPlayStatus();
        }
        return false;
    }

    /**
     * 刷新个人中心基础数据
     */
    public void updateUserInfo () {
        if (mainFragment != null) {
            mainFragment.updateUserInfo();
        }
    }

    /**
     * 初始化个人中心列表数据
     */
    public void initPersonalList () {
        if (mainFragment != null) {
            mainFragment.initPersonalList();
        }
    }

    /**
     * vip回调改变状态
     * 其它首页跳转回调播放音频
     */
    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        QCiVoiceSdk.get().onActivityResult(requestCode, resultCode, data);

        OnActivityResultBean onActivityResultBean = new OnActivityResultBean(requestCode, resultCode, data);
        CustomShareUtils.Companion.getInstance().onShareResponse(onActivityResultBean);

        if (null != customRecorderSelectorDialog && customRecorderSelectorDialog.isShowing()) {
            customRecorderSelectorDialog.dismiss();
        } else {
            EventBus.getDefault().post(new MainConfigEvent().setType(2).setTabId(getMainFragmentItem()).setPlay(true));
        }
    }

    /**
     * 获取当前VoiceFragment Item
     */
    public int getMainFragmentItem () {
        if (mainFragment != null) {
            return mainFragment.getCurrentItem();
        }
        return -1;
    }

    public void setNavigationVisibility (int visibility) {
        vb.viewNavigation.setVisibility(visibility);
    }

    public void setAlpha (float slideOffset) {
        vb.viewNavigation.setAlpha(slideOffset);
    }

    /**
     * 外部scheme跳转
     * 冷启动app需要在初始化调用
     */
    private void schemeJump () {
        Intent intent = getIntent();
        if (null != intent && intent.hasExtra(SchemeJumpActivity.JumpTag)) {
            jumpTag = getIntent().getStringExtra(SchemeJumpActivity.JumpTag);

            if (!TextUtils.isEmpty(jumpTag)) {
                closeMenu(false);
                openRecorderSelectorDialog();
            }
        }
    }


    @Override
    protected void onNewIntent (Intent intent) {
        super.onNewIntent(intent);
        schemeJump();
    }

    /**
     * 为了更好的弹幕点击效果（弹幕显示状态点其它弹幕可直接显示），禁止外部点击关闭弹幕pop
     * 在这里点屏幕任意地方先关闭弹幕pop
     */
    @Override
    public boolean dispatchTouchEvent (MotionEvent ev) {
        BarragePopupUtil.doDismiss();
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume () {
        super.onResume();
        QCiVoiceSdk.get().onResume();
    }

    @Override
    protected void onPause () {
        super.onPause();
        QCiVoiceSdk.get().onPause();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        QCiVoiceSdk.get().onDestroy();
        MainViewModel.getInstance(this).clearLiveData();
        MainHelper.getInstance(this).clear();
        EventBus.getDefault().unregister(this);
    }
}