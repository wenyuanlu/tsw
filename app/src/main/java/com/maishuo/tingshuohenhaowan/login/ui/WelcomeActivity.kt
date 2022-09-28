package com.maishuo.tingshuohenhaowan.login.ui

import android.text.TextUtils
import android.view.KeyEvent
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity
import com.maishuo.tingshuohenhaowan.databinding.ActivityWelcomeLayoutBinding
import com.maishuo.tingshuohenhaowan.main.activity.MainActivity
import com.qichuang.commonlibs.common.PreferencesKey
import com.qichuang.commonlibs.utils.PreferencesUtils

/**
 * author : xpSun
 * date : 9/6/21
 *description :
 */
class WelcomeActivity : CustomBaseActivity<ActivityWelcomeLayoutBinding>() {

    companion object {
        private const val DELAY_TIMER = 1500L
    }

    private var customRunnable: CustomRunnable? = null

    override fun initView() {

    }

    override fun initData() {
        toMainActivity()
    }

    private fun toMainActivity() {
        customRunnable = CustomRunnable()
        postDelayed(customRunnable, DELAY_TIMER)
    }

    private inner class CustomRunnable : Runnable {
        override fun run() {
            val isShowGuide = PreferencesUtils.getString(PreferencesKey.IS_SHOW_GUIDE, "")
            startActivity(if (!TextUtils.isEmpty(isShowGuide)) {
                MainActivity::class.java
            } else {
                GuideActivity::class.java
            }) {
                overridePendingTransition(R.anim.activity_animation_in, R.anim.activity_animation_out)
            }
        }
    }

    //启动页面禁止返回
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeCallbacks(customRunnable)
    }

}