package com.maishuo.tingshuohenhaowan.setting.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.PhonicListBean;
import com.maishuo.tingshuohenhaowan.widget.lock.SlidingFinishLayout;
import com.qichuang.commonlibs.basic.IBasicActivity;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.GlideUtils;
import com.qichuang.commonlibs.utils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * author ：Seven
 * date : 3/24/21
 * description :锁屏页
 */
public class LockActivity extends IBasicActivity implements SlidingFinishLayout.OnSlidingFinishListener {

    private ImageView           ivParent;
    private ImageView           ivHead;
    private TextView            tvDesc;
    private LottieAnimationView viewAnimationHead;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        fullScreen(this);
        setContentView(R.layout.activity_lock);
        initView();
        initAnimation();
    }

    /**
     * 通过设置全屏，设置状态栏透明
     */
    public static void fullScreen (Activity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            View decorView = window.getDecorView();
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN//充满状态栏
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION//充满导航栏
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        } else {
            WindowManager.LayoutParams attributes            = window.getAttributes();
            int                        flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            attributes.flags |= flagTranslucentStatus;
            window.setAttributes(attributes);
        }
    }


    private void initView () {
        ivParent = findViewById(R.id.iv_parent);
        ivHead = findViewById(R.id.iv_head);
        tvDesc = findViewById(R.id.tv_desc);
        viewAnimationHead = findViewById(R.id.view_animationHead);
        SlidingFinishLayout vLockRoot = findViewById(R.id.lock_root);
        vLockRoot.setOnSlidingFinishListener(this);

        initData(PreferencesUtils.getString(PreferencesKey.LOCK_BG_IMAGE, ""),
                PreferencesUtils.getString(PreferencesKey.LOCK_IMAGE, ""),
                PreferencesUtils.getString(PreferencesKey.LOCK_DESC, ""));
    }

    private void initData (String bgPath, String imagePath, String desc) {
        if (TextUtils.isEmpty(imagePath)) {
            GlideUtils.INSTANCE.loadImage(
                    this,
                    R.mipmap.about_pic_logo,
                    ivHead
            );
        } else {
            GlideUtils.INSTANCE.loadImage(
                    this,
                    imagePath,
                    ivHead
            );
            GlideUtils.INSTANCE.loadImage(
                    this,
                    bgPath,
                    ivParent
            );
        }
        tvDesc.setText(desc);
    }

    //动画
    private void initAnimation () {
        viewAnimationHead.setImageAssetsFolder("images/");
        viewAnimationHead.setAnimation("bowen_white.json");
        viewAnimationHead.setProgress(1);
        viewAnimationHead.setRepeatCount(Integer.MAX_VALUE);
        viewAnimationHead.playAnimation();
    }

    /**
     * 重写物理返回键，使不能回退
     */
    @Override
    public void onBackPressed () {
    }

    /**
     * 滑动销毁锁屏页面
     */
    @Override
    public void onSlidingFinish () {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (PhonicListBean.ListBean event) {
        if (event != null) {
            initData(event.getBg_img(), event.getImage_path(), event.getDesc());
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        viewAnimationHead.clearAnimation();
        EventBus.getDefault().unregister(this);
    }
}
