package com.maishuo.tingshuohenhaowan.main.fragment;

import android.content.Intent;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.gyf.immersionbar.ImmersionBar;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.bean.UserInfoEvent;
import com.maishuo.tingshuohenhaowan.common.CustomFragment;
import com.maishuo.tingshuohenhaowan.databinding.FragmentMainBinding;
import com.maishuo.tingshuohenhaowan.api.response.PhonicTagBean;
import com.maishuo.tingshuohenhaowan.main.activity.MainActivity;
import com.maishuo.tingshuohenhaowan.main.activity.SearchActivity;
import com.maishuo.tingshuohenhaowan.main.adapter.CommonViewPagerAdapter;
import com.maishuo.tingshuohenhaowan.main.dialog.PhonicSelectorTypeDialog;
import com.maishuo.tingshuohenhaowan.main.event.MainConfigEvent;
import com.maishuo.tingshuohenhaowan.personal.ui.PersonalHomeFragment;
import com.maishuo.tingshuohenhaowan.personal.ui.PersonalView;
import com.maishuo.tingshuohenhaowan.utils.LoginUtil;
import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils;
import com.maishuo.umeng.ConstantEventId;
import com.qichuang.commonlibs.utils.DeviceUtil;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.retrofit.CommonObserver;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Seven
 * date : 5/20/21
 * description :左菜单（自己的个人中心），关注，推荐，右菜单（别人的个人中心）
 */
public class MainFragment extends CustomFragment<FragmentMainBinding> {

    private              PersonalView        personalView;//左菜单View
    private static final String[]            titles            = new String[]{"关注", "推荐"};
    private final        List<PhonicTagBean> mPhonicTagBeans   = new ArrayList<>();
    private              PhonicTagBean       indexBean         = null;
    private              boolean             slideChange;//滑动状态停止播放音频
    private              VoicePlayFragment   attentionFragment;//关注页
    private              VoicePlayFragment   recommendFragment;//推荐页
    private              boolean             isPlayByCloseMenu = true;//关闭菜单是否继续播放

    public MainFragment () {
    }

    @Override
    protected void initView () {
        initCenterMain();
        initLeftMenu();
        initRightMenu();
        drawerCallBack();
    }

    @Override
    protected void initData () {
    }

    public void getPhonicTag () {
        ApiService.Companion.getInstance().getPhonicTagListApi()
                .subscribe(new CommonObserver<List<PhonicTagBean>>() {
                    @Override
                    public void onResponseSuccess (@Nullable List<PhonicTagBean> data) {
                        if (data != null) {
                            mPhonicTagBeans.addAll(data);
                        }
                    }
                });
    }

    /**
     * 初始化中间主页
     */
    private void initCenterMain () {
        disableLeftMenu();
        setDistanceToStatusBarHeight();
        initTabLayoutAndViewPager();
        clickEvent();
        onRefresh();
    }

    /**
     * 初始化左菜单
     */
    public void initLeftMenu () {
        if (personalView == null) {
            personalView = new PersonalView(getAttachActivity());
            vb.flLeft.addView(personalView);
        }
    }

    /**
     * 初始化右菜单
     */
    private void initRightMenu () {
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.fl_right, PersonalHomeFragment.getInstance())
                .commitNow();
        setRightMenuWight();
    }

    /**
     * 设置距离状态栏高度的marginTop
     * 测量头部高度，计算弹幕弹出空间
     */
    private void setDistanceToStatusBarHeight () {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) vb.rlTitle.getLayoutParams();
        lp.topMargin = ImmersionBar.getStatusBarHeight(this);
        vb.rlTitle.setLayoutParams(lp);

        if (PreferencesUtils.getInt("MainTitleAndStatusBarHeight", 0) == 0) {
            vb.rlTitle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout () {
                    vb.rlTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    PreferencesUtils.putInt("MainTitleAndStatusBarHeight", vb.rlTitle.getBottom());
                }
            });
        }
    }


    /**
     * 初始化TabLayout和ViewPager
     */
    private void initTabLayoutAndViewPager () {
        SparseArray<Fragment> fragmentSparseArray = new SparseArray<>();
        attentionFragment = VoicePlayFragment.getInstance(0);
        recommendFragment = VoicePlayFragment.getInstance(1);

        fragmentSparseArray.put(0, attentionFragment);
        fragmentSparseArray.put(1, recommendFragment);

        SparseArray<String> menus = new SparseArray<>();
        menus.put(0, titles[0]);
        menus.put(1, titles[1]);

        CommonViewPagerAdapter adapter = new CommonViewPagerAdapter(getChildFragmentManager(), fragmentSparseArray, menus);
        vb.viewPager.setAdapter(adapter);
        vb.viewPager.setOffscreenPageLimit(fragmentSparseArray.size());
        setViewPagerItem(1);

        vb.tabLayout.addTab(vb.tabLayout.newTab().setText(titles[0]));
        vb.tabLayout.addTab(vb.tabLayout.newTab().setText(titles[1]));
        vb.tabLayout.setupWithViewPager(vb.viewPager);

        vb.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {
                changeVoiceBySlideStatus(positionOffset);//暂停音频
            }

            @Override
            public void onPageSelected (int position) {
                TrackingAgentUtils.onEvent(getActivity(),
                        position == 0 ? ConstantEventId.NEWvoice_follow : ConstantEventId.NEWvoice_recommend);
                vb.llRight.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                if (position == 0 && LoginUtil.checkLogin()) {
                    enableLeftMenu();
                } else {
                    disableLeftMenu();
                }
            }

            @Override
            public void onPageScrollStateChanged (int state) {
            }
        });
    }

    /**
     * 设置页面到推荐页
     */
    public void setViewPagerItem (int currentItem) {
        vb.viewPager.setCurrentItem(currentItem);
    }

    /**
     * 主页的点击事件
     */
    private void clickEvent () {
        //左侧菜单
        vb.ivPersonal.setOnClickListener(v -> {
            if (!LoginUtil.isLogin(getContext())) {
                return;
            }
            openLeftMenu();
        });

        //分类
        vb.ivType.setOnClickListener(v -> {
            if (getActivity() instanceof AppCompatActivity) {
                AppCompatActivity        appCompatActivity = (AppCompatActivity) getActivity();
                PhonicSelectorTypeDialog dialog            = new PhonicSelectorTypeDialog(appCompatActivity);
                dialog.showDialog();
                dialog.setPhonicTagBean(mPhonicTagBeans, indexBean);
                dialog.setOnSelectorTypeListener(it -> {
                    if (null == it) {
                        return null;
                    }
                    indexBean = it;
                    EventBus.getDefault().post(it);
                    return null;
                });
            }
        });

        //搜索
        vb.ivSearch.setOnClickListener(v -> {
            EventBus.getDefault().post(new MainConfigEvent().setType(2).setPlay(false));
            Intent intent = new Intent(getContext(), SearchActivity.class);
            startActivityForResult(intent, 0);
        });
    }

    /**
     * 子Fragment下拉刷新
     */
    private void onRefresh () {
        vb.refreshLayout.setEnableLoadMore(false);
        vb.refreshLayout.setOnRefreshListener(seven -> {
            if (getCurrentItem() == 0) attentionFragment.pullRefresh();
            else recommendFragment.pullRefresh();
            vb.refreshLayout.finishRefresh();
        });
    }

    /**
     * 子Fragment的播放状态
     */
    public boolean isPlayStatus () {
        return attentionFragment.isPlaying() || recommendFragment.isPlaying();
    }

    /**
     * 菜单拖动回调
     */
    private void drawerCallBack () {
        vb.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide (View view, float slideOffset) {
                if (getActivity() != null) ((MainActivity) getActivity()).setAlpha(1 - slideOffset);
                if (view.getId() == R.id.fl_right) recommendFragment.updatePersonalInfo();
                changeVoiceBySlideStatus(slideOffset);
            }

            @Override
            public void onDrawerOpened (View view) {
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).setNavigationVisibility(View.GONE);
                }

                //禁用右菜单：左菜单没有充满屏幕，防止从边缘滑出右菜单
                if (view.getId() == R.id.fl_left) {
                    disableRightMenu();
                    enableLeftMenu();
                }
            }

            @Override
            public void onDrawerClosed (View view) {
                //启用右菜单：需要判断当前是否是广告页，
                //广告页依然禁用，留声页才开启
                if (view.getId() == R.id.fl_left) {
                    if (!recommendFragment.isAdPage()) {
                        enableRightMenu();
                    }
                }
            }

            @Override
            public void onDrawerStateChanged (int i) {
                if (i != 0) {
                    if (getActivity() != null) {
                        ((MainActivity) getActivity()).setNavigationVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    /**
     * 计算滑动状态改变音频播放状态
     */
    private void changeVoiceBySlideStatus (float slideOffset) {
        if (slideOffset > 0) {
            if (!slideChange) {
                EventBus.getDefault().post(new MainConfigEvent().setType(2).setPlay(false));
                slideChange = true;
            }
        } else {
            slideChange = false;
            //Scheme跳转打开发布弹窗不需要继续播放
            if (isPlayByCloseMenu) {
                EventBus.getDefault().post(new MainConfigEvent().setType(2).setTabId(getCurrentItem()).setPlay(true));
            } else {
                isPlayByCloseMenu = true;
            }
        }
    }

    /**
     * 更新个人基础数据
     */
    public void updateUserInfo () {
        if (personalView != null) {
            personalView.initData();
        }
    }

    /**
     * 初始化个人列表数据
     */
    public void initPersonalList () {
        if (personalView != null) {
            personalView.initViewPager();
        }
    }

    /**
     * 改变用户信息
     */
    public void changeVipOrNameInfo (UserInfoEvent event) {
        if (personalView != null) {
            personalView.changeVipOrNameInfo(event);
        }
    }

    /**
     * 获取当前tab
     */
    public int getCurrentItem () {
        return vb.viewPager.getCurrentItem();
    }

    /**
     * 关闭所有菜单
     */
    public void closeMenu (boolean isPlay) {
        isPlayByCloseMenu = isPlay;
        vb.drawerLayout.closeDrawers();
    }

    /**
     * 打开左菜单
     */
    public void openLeftMenu () {
        vb.drawerLayout.openDrawer(Gravity.LEFT);
    }

    /**
     * 禁用左菜单
     */
    public void disableLeftMenu () {
        vb.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
    }

    /**
     * 启用左菜单
     */
    public void enableLeftMenu () {
        vb.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);
    }

    /**
     * 弹出左菜单时禁用右菜单
     */
    public void disableRightMenu () {
        vb.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
    }

    /**
     * 启用右菜单
     */
    public void enableRightMenu () {
        vb.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.RIGHT);
    }

    /**
     * 设置右菜单为全屏
     */
    private void setRightMenuWight () {
        ViewGroup.LayoutParams rightParams = vb.flRight.getLayoutParams();
        rightParams.width = DeviceUtil.getDeviceWidth();
        vb.flRight.setLayoutParams(rightParams);
    }

    /**
     * 隐藏暂停
     * 显示播放
     */
    @Override
    public void onHiddenChanged (boolean hidden) {
        super.onHiddenChanged(hidden);
        changeVoiceBySlideStatus(hidden ? 1 : 0);
    }
}
