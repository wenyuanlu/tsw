package com.qichuang.commonlibs.basic;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.qichuang.commonlibs.basic.action.ActivityAction;
import com.qichuang.commonlibs.basic.action.ClickAction;
import com.qichuang.commonlibs.basic.action.HandlerAction;
import com.qichuang.commonlibs.basic.action.ResourcesAction;

/**
 * author : yun
 * desc   : Fragment 基类
 */
public abstract class BaseFragment<A extends BaseActivity> extends Fragment implements
        ActivityAction, ResourcesAction, HandlerAction, ClickAction {

    /**
     * Activity 对象
     */
    private A mActivity;

    /**
     * 根布局
     */
    public View mRootView;

    /**
     * 当前是否加载过
     */
    private boolean mLoading;

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach (@NonNull Context context) {
        super.onAttach(context);
        mActivity = (A) requireActivity();
    }

    @Override
    public void onDetach () {
        removeCallbacks();
        mActivity = null;
        super.onDetach();
    }

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLoading = false;
        if (getLayoutId() > 0) {
            return mRootView = inflater.inflate(getLayoutId(), null);
        } else {
            return mRootView = fetchRootView();
        }
    }

    protected View fetchRootView () {
        return null;
    }

    @Override
    public void onDestroyView () {
        mLoading = false;
        mRootView = null;
        super.onDestroyView();
    }

    @Override
    public void onResume () {
        super.onResume();
        if (!mLoading) {
            mLoading = true;
            initFragment();
        }
    }

    /**
     * 这个 Fragment 是否已经加载过了
     */
    public boolean isLoading () {
        return mLoading;
    }

    @NonNull
    @Override
    public View getView () {
        return mRootView;
    }

    /**
     * 获取绑定的 Activity，防止出现 getActivity 为空
     */
    public A getAttachActivity () {
        return mActivity;
    }

    protected void initFragment () {
        initView();
        initData();
    }

    /**
     * 获取布局 ID
     */
    protected int getLayoutId () {
        return 0;
    }

    /**
     * 初始化控件
     */
    protected abstract void initView ();

    /**
     * 初始化数据
     */
    protected abstract void initData ();

    /**
     * 根据资源 id 获取一个 View 对象
     */
    @Override
    public <V extends View> V findViewById (@IdRes int id) {
        return mRootView.findViewById(id);
    }
}