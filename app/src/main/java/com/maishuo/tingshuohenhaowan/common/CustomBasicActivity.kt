package com.maishuo.tingshuohenhaowan.common

import android.text.TextUtils
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.maishuo.tingshuohenhaowan.R
import com.qichuang.commonlibs.basic.BaseActivity
import com.qichuang.commonlibs.utils.PreferencesUtils

/**
 * author : xpSun
 * date : 9/13/21
 * description :
 */
abstract class CustomBasicActivity : BaseActivity() {

    override fun fetchRootView(): View? {
        return fetchLayoutView()
    }

    abstract fun fetchLayoutView(): View?

    override fun initData() {

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.left_in_activity, R.anim.left_out_activity)
    }

    override fun onResume() {
        super.onResume()
        val proxy = PreferencesUtils.getString("ProxyIp")
        if (!TextUtils.isEmpty(proxy)) {
            ImmersionBar.with(this).statusBarColor(R.color.red).init()
        } else {
            ImmersionBar.with(this).transparentBar().init()
        }
    }
}