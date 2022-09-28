package com.qichuang.commonlibs.basic;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.qichuang.commonlibs.basic.action.ActivityAction;
import com.qichuang.commonlibs.basic.action.ClickAction;
import com.qichuang.commonlibs.basic.action.HandlerAction;
import com.qichuang.commonlibs.databinding.ActivityBaseBinding;
import com.qichuang.commonlibs.utils.ScreenUtils;

/**
 * author : yun
 * desc   : Activity 基类
 */
public abstract class BaseActivity extends IBasicActivity
        implements ActivityAction, ClickAction, HandlerAction {

    private ActivityBaseBinding binding;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomBasicApplication.instance.addActivity(this);
        initLayout();
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
     * 初始化布局
     */
    protected void initLayout () {
        binding = ActivityBaseBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        //子类传递布局
        if (getLayoutId() > 0) {
            View view = LayoutInflater.from(this).inflate(getLayoutId(), null);
            binding.flBaseContent.addView(view);
        } else {
            binding.flBaseContent.addView(fetchRootView());
        }

        binding.ivBaseBack.setOnClickListener(view -> {
            onBackListener();
        });

        try {
            if (ScreenUtils.isNavigationBarShowing(this)) {
                int value = ScreenUtils.getNavigationHeight(this);
                if (value > 50) {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) binding.flBaseContent.getLayoutParams();
                    layoutParams.bottomMargin = value;
                    binding.flBaseContent.setLayoutParams(layoutParams);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackListener () {
        finish();
    }

    protected View fetchRootView () {
        return null;
    }

    /**
     * 设置title
     */
    public void setTitle (String title) {
        binding.rlBaseTitle.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(title)) {
            binding.tvBaseTitle.setText(title);
        }
    }

    /**
     * 设置右侧图片
     */
    public void setRightImage (int resId) {
        binding.ivBaseRight.setVisibility(View.VISIBLE);
        if (resId > 0) {
            binding.ivBaseRight.setImageResource(resId);
        }
    }

    /**
     * 设置右侧图片
     */
    public void setRightText (int resId) {
        binding.tvBaseRightText.setVisibility(View.VISIBLE);
        if (resId > 0) {
            binding.tvBaseRightText.setText(resId);
        }
    }

    /**
     * 把自定义的view添加到右面
     */
    public void addRightMoreView (View view) {
        if (view != null) {
            binding.flBaseRightMore.addView(view);
            binding.flBaseRightMore.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 清除右面的控件
     */
    public void removeRightMoreView () {
        if (binding.flBaseRightMore != null) {
            binding.flBaseRightMore.removeAllViews();
        }
    }

    public void setRightTextOnClick (View.OnClickListener listener) {
        binding.tvBaseRightText.setOnClickListener(listener);
    }

    /**
     * 获取返回图片控件,用于点击事件等
     */
    public ImageView getIvBack () {
        return binding.ivBaseBack;
    }

    public TextView getTitleView () {
        return binding.tvBaseTitle;
    }

    /**
     * 获取右侧图片控件,用于点击事件等
     */
    public ImageView getRightImage () {
        return binding.ivBaseRight;
    }

    @Override
    protected void onDestroy () {
        removeCallbacks();
        CustomBasicApplication.instance.removeActivity(this);
        super.onDestroy();
    }

    @Override
    public void finish () {
        super.finish();
    }

    /**
     * 如果当前的 Activity（singleTop 启动模式） 被复用时会回调
     */
    @Override
    protected void onNewIntent (Intent intent) {
        super.onNewIntent(intent);
        // 设置为当前的 Intent，避免 Activity 被杀死后重启 Intent 还是最原先的那个
        setIntent(intent);
    }

    @Override
    public Context getContext () {
        return this;
    }

    public FrameLayout getContent () {
        return binding.flBaseContent;
    }

    @Override
    public Resources getResources () {
        Resources     res    = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
}