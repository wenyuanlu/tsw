package com.maishuo.tingshuohenhaowan.widget;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.utils.LoginUtil;


/**
 * Create by yun on 2019-07-16
 * EXPLAIN:
 */
public class MainPageBottomNavigationView extends LinearLayout implements View.OnClickListener {

    LinearLayout        linHome;
    RelativeLayout      linMessage;
    LottieAnimationView animation_home;
    LottieAnimationView animation_message;
    TextView            tv_home_item;
    TextView            tv_home_message;
    TextView            tv_home_message_count;
    TextView            tv_home_message_system;
    LinearLayout        lin_home_center;
    View                main_divide_line;

    private static final long CLICK_INTERVAL_TIME = 300;
    private static       long lastClickTime       = 0;
    LinearLayout bottomNavigation;
    private OnItemClickListener listener;

    public MainPageBottomNavigationView (Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();

    }

    public MainPageBottomNavigationView (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init () {
        setOrientation(LinearLayout.HORIZONTAL);
        setClipChildren(false);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.main_page_bottom_navigation_view, null);
        this.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linHome = findViewById(R.id.lin_home);
        linMessage = findViewById(R.id.lin_message);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        animation_home = findViewById(R.id.animation_home);
        animation_message = findViewById(R.id.animation_message);
        tv_home_item = findViewById(R.id.tv_home_item);
        tv_home_message = findViewById(R.id.tv_home_message);
        tv_home_message_count = findViewById(R.id.tv_home_message_count);
        tv_home_message_system = findViewById(R.id.tv_home_message_system);
        lin_home_center = findViewById(R.id.lin_home_center);
        main_divide_line = findViewById(R.id.main_divide_line);
        animation_home.setImageAssetsFolder("images/");
        animation_home.setAnimation("shouye.json");
        animation_message.setAnimation("xiaoxi.json");
        animation_home.setProgress(1);
        animation_message.setProgress(0);
        animation_home.playAnimation();

        linHome.setOnClickListener(this);
        linMessage.setOnClickListener(this);
        lin_home_center.setOnClickListener(this);
    }


    private             int selectorPosition          = SELECTOR_POSITION_HOME;
    public static final int SELECTOR_POSITION_HOME    = 0;
    public static final int SELECTOR_POSITION_CENTER  = 1;
    public static final int SELECTOR_POSITION_MESSAGE = 2;

    public int getSelectorPosition () {
        return selectorPosition;
    }

    @Override
    public void onClick (View view) {
        switch (view.getId()) {
            case R.id.lin_home:
                if (SELECTOR_POSITION_HOME == selectorPosition) {
                    return;
                }

                //获取系统当前毫秒数，从开机到现在的毫秒数(手机睡眠时间不包括在内)
                long currentTimeMillis = SystemClock.uptimeMillis();
                //两次点击间隔时间小于300ms代表双击
                if (Math.abs(currentTimeMillis - lastClickTime) < CLICK_INTERVAL_TIME) {
                    return;
                }
                lastClickTime = currentTimeMillis;

                selectHome();
                break;
            case R.id.lin_home_center:
                selectorPosition = SELECTOR_POSITION_CENTER;
                break;
            case R.id.lin_message:
                if (LoginUtil.checkLogin()) {
                    selectorPosition = SELECTOR_POSITION_MESSAGE;
                    animation_message.playAnimation();
                    animation_home.setProgress(0);
                }
                break;
            default:
                break;
        }
        if (listener != null) {
            listener.itemClick(view.getId());
        }
    }

    public void selectHome () {
        selectorPosition = SELECTOR_POSITION_HOME;
        animation_home.playAnimation();
        animation_message.setProgress(0);
    }


    public void setItemClickListener (OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void itemClick (int itemId);
    }

    /**
     * 设置分割线显示🐟
     */
    public void setLineVisibility (int mVisibility) {
        main_divide_line.setVisibility(mVisibility);

    }

    public void setMessageCount (int count, boolean isSystem) {
        if (isSystem) {
            tv_home_message_system.setVisibility(VISIBLE);
        } else {
            tv_home_message_system.setVisibility(GONE);
        }

        if (count > 0) {
            tv_home_message_count.setVisibility(VISIBLE);
            tv_home_message_system.setVisibility(GONE);
            tv_home_message_count.setText(String.valueOf(count));
        } else {
            tv_home_message_count.setVisibility(GONE);
        }
    }
}
