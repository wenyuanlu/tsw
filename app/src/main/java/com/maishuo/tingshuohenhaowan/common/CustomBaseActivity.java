package com.maishuo.tingshuohenhaowan.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.viewbinding.ViewBinding;

import com.gyf.immersionbar.ImmersionBar;
import com.maishuo.tingshuohenhaowan.R;
import com.qichuang.commonlibs.basic.BaseActivity;
import com.qichuang.commonlibs.utils.PreferencesUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * author : yun
 * desc   : 项目中的 Activity 基类
 */
public abstract class CustomBaseActivity<T extends ViewBinding> extends BaseActivity {

    public T vb;

    @Override
    protected int getLayoutId () {
        return 0;
    }

    @Override
    protected View fetchRootView () {
        return getRootViewByReflect();
    }

    @Override
    protected void initLayout () {
        super.initLayout();
    }

    /**
     * 设置标题栏的标题
     */
    @Override
    public void setTitle (@StringRes int id) {
        setTitle(getString(id));
    }

    @Override
    public void startActivityForResult (Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.right_in_activity, R.anim.right_out_activity);
    }

    @Override
    public void finish () {
        super.finish();
        overridePendingTransition(R.anim.left_in_activity, R.anim.left_out_activity);
    }

    @Override
    protected void onResume () {
        super.onResume();
        String proxy = PreferencesUtils.getString("ProxyIp");
        if (!TextUtils.isEmpty(proxy)) {
            ImmersionBar.with(this).statusBarColor(R.color.red).init();
        } else {
            ImmersionBar.with(this).transparentBar().init();
        }
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