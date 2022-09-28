package com.maishuo.tingshuohenhaowan.setting.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity
import com.maishuo.tingshuohenhaowan.databinding.ActivityAccountSecurityLayoutBinding
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackListener
import com.maishuo.tingshuohenhaowan.utils.DialogUtils
import com.qichuang.commonlibs.common.PreferencesKey
import com.qichuang.commonlibs.utils.PreferencesUtils

/**
 * author : xpSun
 * date : 9/13/21
 * description : 账号安全界面
 */
class AccountSecurityActivity : CustomBaseActivity<ActivityAccountSecurityLayoutBinding?>() {

    private var mPhone: String? = null
    private var mPhoneMi = "未绑定"

    override fun initView() {
        setTitle("账号与安全")
        vb?.rlPhone?.setOnClickListener(this)
        vb?.rlDelete?.setOnClickListener(this)
    }

    override fun initData() {
        mPhone = PreferencesUtils.getString(PreferencesKey.USER_PHONE, "")
        if (!TextUtils.isEmpty(mPhone) && mPhone?.length == 11) {
            val sb = StringBuilder(mPhone?:"")
            sb.replace(3, 7, "****")
            mPhoneMi = sb.toString()
        }
        vb?.rlPhone?.setHintText(mPhoneMi)
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.rl_phone -> DialogUtils.showCommonDialog(this,
                    "更换绑定手机号",
                    "当前绑定的手机号码为$mPhoneMi",
                    object : OnDialogBackListener {
                        override fun onSure(content: String) {
                            val intent = Intent(this@AccountSecurityActivity, AccountVerifyActivity::class.java)
                            intent.putExtra(AccountVerifyActivity.ACCOUNT_VERIFY_TYPE_TAG, 1)
                            intent.putExtra(AccountVerifyActivity.ACCOUNT_VERIFY_PHONE_TAG, mPhone)
                            startActivity(intent)
                        }

                        override fun onCancel() {}
                    })
            R.id.rl_delete -> {
                val intent = Intent(this, AccountVerifyActivity::class.java)
                intent.putExtra(AccountVerifyActivity.ACCOUNT_VERIFY_TYPE_TAG, 3)
                intent.putExtra(AccountVerifyActivity.ACCOUNT_VERIFY_PHONE_TAG, mPhone)
                startActivity(intent)
            }
            else -> {
            }
        }
    }
}