package com.maishuo.tingshuohenhaowan.setting.ui

import android.content.Intent
import android.text.TextUtils
import com.maishuo.tingshuohenhaowan.api.param.CancelAccountParam
import com.maishuo.tingshuohenhaowan.api.response.GetTokenResponse
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity
import com.maishuo.tingshuohenhaowan.databinding.ActivityAccountRemoveLayoutBinding
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackListener
import com.maishuo.tingshuohenhaowan.rtmchat.ChatLoginUtils
import com.maishuo.tingshuohenhaowan.utils.DialogUtils
import com.qichuang.commonlibs.common.PreferencesKey
import com.qichuang.commonlibs.utils.LogUtils
import com.qichuang.commonlibs.utils.PreferencesUtils
import com.qichuang.retrofit.CommonObserver

/**
 * author : xpSun
 * date : 9/14/21
 * description : 账号注销界面
 */
class AccountRemoveActivity : CustomBaseActivity<ActivityAccountRemoveLayoutBinding>() {

    companion object {
        const val PHONE = "account_remove_phone"
        const val CODE = "account_remove_code"
    }

    private var mPhone: String? = null
    private var mCode: String? = null

    override fun initView() {
        setTitle("注销账号")
        vb?.btAccountRemoveSure?.setOnClickListener { removeAccount(0) }
    }

    override fun initData() {
        mPhone = intent.getStringExtra(PHONE)
        mCode = intent.getStringExtra(CODE)
    }

    /**
     * 账户注销
     */
    private fun removeAccount(type: Int) {
        val param = CancelAccountParam()
        param.phone = mPhone
        param.verificationCode = mCode
        param.type = if (1 == type) type else null
        ApiService.instance
                .cancelAccount(param)
                .subscribe(object : CommonObserver<Any>() {
                    override fun onResponseSuccess(response: Any?) {
                        removeSuccess()
                    }

                    override fun onResponseError(message: String?, e: Throwable?, code: String?) {
                        if (!TextUtils.isEmpty(code) && "752" == code) {
                            var hint: String? = "注销失败"
                            if (!TextUtils.isEmpty(message)) {
                                hint = message
                            }

                            DialogUtils.showCommonDialog(this@AccountRemoveActivity, "注销账号", hint,
                                    object : OnDialogBackListener {
                                        override fun onSure(content: String) {
                                            removeAccount(1) //再次注销账户
                                        }

                                        override fun onCancel() {}
                                    })
                        } else {
                            super.onResponseError(message, e, code)
                        }
                    }
                })
    }

    /**
     * 注销成功
     */
    private fun removeSuccess() {
        ChatLoginUtils.clearUserInfo() //注销成功能,清理数据
        //账号注销成功，重新获取token。注销账号后没token跳转到首页会报缺少参数
        getToken {
            val intent = Intent(this@AccountRemoveActivity, AccountRemoveSuccessActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**
     * 获取Token
     */
    private fun getToken(listener: () -> Unit?) {
        ApiService.instance
                .getToken()
                .subscribe(object : CommonObserver<GetTokenResponse>() {
                    override fun onResponseSuccess(response: GetTokenResponse?) {
                        //对数据进行非空判断
                        if (response != null && !TextUtils.isEmpty(response.token)) {
                            // 更新 Token
                            PreferencesUtils.putString(PreferencesKey.TOKEN, response.token)
                            listener.invoke()
                        } else {
                            LogUtils.LOGE("登录获取token失败")
                        }
                    }
                })
    }
}