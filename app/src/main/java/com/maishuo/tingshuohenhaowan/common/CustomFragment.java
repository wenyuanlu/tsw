package com.maishuo.tingshuohenhaowan.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import com.gyf.immersionbar.ImmersionBar;
import com.qichuang.commonlibs.basic.BaseFragment;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * author : yun
 * desc   : 项目中 Fragment 懒加载基类
 */
public abstract class CustomFragment<T extends ViewBinding> extends BaseFragment {

    public T vb;

    public CustomFragment () {
    }

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ImmersionBar.with(this).transparentBar().init();
        return view;
    }

    @Override
    protected int getLayoutId () {
        return 0;
    }

    @Override
    protected View fetchRootView () {
        return getRootViewByReflect();
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
    }

    @Override
    public void onResume () {
        super.onResume();
    }

    /**
     * 通过反射获取rootView
     */
    private View getRootViewByReflect () {
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            try {
                Class<T> clazz  = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
                Method   method = clazz.getMethod("inflate", LayoutInflater.class);
                vb = (T) method.invoke(null, getLayoutInflater());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return vb.getRoot();
    }
}