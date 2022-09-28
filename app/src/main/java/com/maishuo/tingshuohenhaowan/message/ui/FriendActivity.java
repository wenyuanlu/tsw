package com.maishuo.tingshuohenhaowan.message.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.bean.UreadMessageEvent;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.databinding.ActivityFriendBinding;
import com.maishuo.tingshuohenhaowan.main.activity.SearchActivity;
import com.maishuo.tingshuohenhaowan.main.adapter.CommonViewPagerAdapter;

import org.greenrobot.eventbus.EventBus;

/**
 * author ：yh
 * date : 2021/2/24 13:39
 * description : 消息-好友页面
 */
public class FriendActivity extends CustomBaseActivity {

    public static final String         SELECTPOSITION = "select_position";
    private ActivityFriendBinding binding;

    @Override
    protected View fetchRootView () {
        binding = ActivityFriendBinding.inflate(LayoutInflater.from(this));
        return binding.getRoot();
    }

    @Override
    protected void initView () {
        setTitle("好友");
        setRightImage(R.mipmap.general_nav_icon_search);//右侧搜索图片
        getRightImage().setOnClickListener(this);
    }

    @Override
    protected void initData () {
        int position = getIntent().getIntExtra(SELECTPOSITION, 0);
        initViewPager(position);//初始化ViewPage
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.iv_base_right://右侧搜索
                startActivity(new Intent(this, SearchActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager (int position) {
        SparseArray<String> tabList = new SparseArray<>();
        tabList.put(0,"粉丝");
        tabList.put(1,"关注");
        tabList.put(2,"朋友");

        SparseArray<Fragment> fragments = new SparseArray<>();
        //粉丝
        FriendFragment mFansFragment = new FriendFragment(2);
        //关注
        FriendFragment mCareFragment = new FriendFragment(1);
        //朋友
        FriendFragment mFriendFragment = new FriendFragment(4);
        fragments.put(0, mFansFragment);
        fragments.put(1, mCareFragment);
        fragments.put(2, mFriendFragment);

        CommonViewPagerAdapter commonViewPagerAdapter = new CommonViewPagerAdapter(getSupportFragmentManager(),fragments,tabList);
        binding.vpFriend.setAdapter(commonViewPagerAdapter);
        binding.vpFriend.setOffscreenPageLimit(3);
        binding.vpFriend.setCurrentItem(position);
        binding.magicIndicatorFriend.setupWithViewPager(binding.vpFriend);
    }

    @Override
    public void finish () {
        EventBus.getDefault().post(new UreadMessageEvent());
        super.finish();
    }
}