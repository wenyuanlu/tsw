package com.maishuo.tingshuohenhaowan.setting.ui

import android.content.Intent
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity
import com.maishuo.tingshuohenhaowan.databinding.ActivityAccountRemoveSuccessLayoutBinding
import com.maishuo.tingshuohenhaowan.main.activity.MainActivity

/**
 * author : xpSun
 * date : 9/14/21
 * description : 账号注销成功界面
 */
class AccountRemoveSuccessActivity : CustomBaseActivity<ActivityAccountRemoveSuccessLayoutBinding>() {

    override fun initView() {
        setTitle("注销成功")
        ivBack.setOnClickListener { goToMainActivity() }
        vb?.btAccountRemoveSuccess?.setOnClickListener { goToMainActivity() }
    }

    override fun initData() {}

    /**
     * 返回到主界面
     */
    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}