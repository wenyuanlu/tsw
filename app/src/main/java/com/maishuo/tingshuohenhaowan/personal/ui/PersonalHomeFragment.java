package com.maishuo.tingshuohenhaowan.personal.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.databinding.FragmentPersonBinding;
import com.maishuo.tingshuohenhaowan.api.response.PhonicListBean;
import com.maishuo.tingshuohenhaowan.main.event.UpdatePersonalInfoEvent;
import com.maishuo.tingshuohenhaowan.main.activity.MainActivity;
import com.maishuo.tingshuohenhaowan.ui.activity.WebViewFragment;
import com.qichuang.commonlibs.basic.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * author : Seven
 * date : 2021/3/31
 * description :个人中心Fragment
 */
public class PersonalHomeFragment extends BaseFragment<MainActivity> {
    private FragmentPersonBinding binding;

    private FragmentTransaction fragmentTransaction;
    private PersonalFragment    personalFragment;
    private WebViewFragment     webViewFragment;

    @Override
    protected int getLayoutId () {
        return -1;
    }

    @Override
    protected View fetchRootView () {
        binding = FragmentPersonBinding.inflate(LayoutInflater.from(getContext()));
        return binding.getRoot();
    }

    public static PersonalHomeFragment getInstance () {
        return new PersonalHomeFragment();
    }

    @Override
    protected void initView () {
        fragmentTransaction = getChildFragmentManager().beginTransaction();
    }

    @Override
    protected void initData () {
    }

    private void openPersonalFragment (String userId) {
        if (null == personalFragment) {
            personalFragment = new PersonalFragment(userId);
            commitFragment(personalFragment);
        } else {
            if (null != webViewFragment) {
                hideFragment(webViewFragment);
            }
            personalFragment.refresh(userId);
            showFragment(personalFragment);
        }
    }

    private void openWebViewFragment (PhonicListBean.ListBean.AdinfoBean bean) {
        if (null == webViewFragment) {
            webViewFragment = new WebViewFragment();
            commitFragment(webViewFragment);
        } else {
            if (null != personalFragment) {
                hideFragment(personalFragment);
            }
            webViewFragment.setData(bean.getLdp(), bean.getTitle());
            showFragment(webViewFragment);
        }
    }

    private void commitFragment (Fragment fragment) {
        fragmentTransaction.add(R.id.rlPersonalContainer, fragment);
        fragmentTransaction.commitNow();
    }

    private void showFragment (Fragment fragment) {
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        }
    }

    private void hideFragment (Fragment fragment) {
        fragmentTransaction.hide(fragment);
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (UpdatePersonalInfoEvent event) {
        if (event != null && !TextUtils.isEmpty(event.getUserId())) {
            openPersonalFragment(event.getUserId());
        }
    }
}
