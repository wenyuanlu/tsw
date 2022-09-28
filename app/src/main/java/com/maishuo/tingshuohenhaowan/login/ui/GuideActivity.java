package com.maishuo.tingshuohenhaowan.login.ui;

import android.view.KeyEvent;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.databinding.ActivityGuideBinding;
import com.maishuo.tingshuohenhaowan.login.adapter.GuidePagerAdapter;
import com.maishuo.tingshuohenhaowan.login.dialog.UserAgreementDialog;
import com.maishuo.tingshuohenhaowan.main.activity.MainActivity;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.PreferencesUtils;

/**
 * author : yun
 * time   : 2019/09/21
 * desc   : APP 引导页
 */
public final class GuideActivity extends CustomBaseActivity<ActivityGuideBinding>
        implements ViewPager.OnPageChangeListener {

    private GuidePagerAdapter mPagerAdapter;

    @Override
    protected void initView () {
        setOnClickListener(vb.btnGuideComplete);
        vb.pvGuideIndicator.setViewPager(vb.vpGuidePager);
    }

    @Override
    protected void initData () {
        mPagerAdapter = new GuidePagerAdapter();
        vb.vpGuidePager.setAdapter(mPagerAdapter);
        vb.vpGuidePager.addOnPageChangeListener(this);
        showProtocolDialog();
    }

    @Override
    public void onClick (View v) {
        if (v == vb.btnGuideComplete) {
            PreferencesUtils.putString(PreferencesKey.IS_SHOW_GUIDE, "1");
            startActivity(MainActivity.class, () -> {
                GuideActivity.this.finish();
                overridePendingTransition(R.anim.activity_animation_in, R.anim.activity_animation_out);
            });
        }
    }

    private void showProtocolDialog () {
        new UserAgreementDialog(this).showDialog();
    }

    @Override
    public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {
        if (vb.vpGuidePager.getCurrentItem() == mPagerAdapter.getCount() - 1 && positionOffsetPixels > 0) {
            vb.btnGuideComplete.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPageSelected (int position) {
    }

    @Override
    public void onPageScrollStateChanged (int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            vb.btnGuideComplete.setVisibility(
                    vb.vpGuidePager.getCurrentItem() ==
                            mPagerAdapter.getCount() - 1
                            ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finishAffinity();
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}