package com.maishuo.tingshuohenhaowan.login.ui

import android.content.Intent
import android.graphics.Paint
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.api.param.LoginParam
import com.maishuo.tingshuohenhaowan.api.param.SendCodeParam
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService
import com.maishuo.tingshuohenhaowan.bean.LoginEvent
import com.maishuo.tingshuohenhaowan.bean.MainMessageBean
import com.maishuo.tingshuohenhaowan.common.Constant
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity
import com.maishuo.tingshuohenhaowan.databinding.ActivityLoginBinding
import com.maishuo.tingshuohenhaowan.api.response.LoginBean
import com.maishuo.tingshuohenhaowan.api.response.SendMessageBean
import com.maishuo.tingshuohenhaowan.ui.activity.WebViewActivity
import com.qichuang.commonlibs.utils.ToastUtil
import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils
import com.qichuang.commonlibs.common.PreferencesKey
import com.qichuang.commonlibs.utils.PhoneUtils
import com.qichuang.commonlibs.utils.PreferencesUtils
import com.qichuang.retrofit.CommonObserver
import org.greenrobot.eventbus.EventBus

class LoginActivity : CustomBaseActivity<ActivityLoginBinding>() {

    override fun initView() {
        vb?.let {
            it.tvLoginAgree.paint.flags = Paint.UNDERLINE_TEXT_FLAG
            //抗锯齿
            it.tvLoginAgree.paint.isAntiAlias = true
        }
    }

    override fun initData() {
        vb?.let {
            it.ivLoginBack.setOnClickListener(this)
            it.tvLoginGetCode.setOnClickListener(this)
            it.ivLoginIcon.setOnClickListener(this)
            it.tvLoginAgree.setOnClickListener(this)
            it.ivLogin.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_login_back -> {
                setResult(0, Intent())
                finish()
            }
            R.id.tv_login_get_code -> {
                if (TextUtils.isEmpty(vb?.etLoginPhone?.text)) {
                    ToastUtil.showToast("请填写手机号码")
                    return
                }

                if (!PhoneUtils.checkPhoneNumber(vb?.etLoginPhone?.text.toString())) {
                    ToastUtil.showToast("请输入正确手机号")
                    return
                }

                //开始倒计时
                if (countDownTimer != null) {
                    countDownTimer?.start()
                }
                sendVerifyCode(vb?.etLoginPhone?.text.toString())
            }
            R.id.tv_login_agree -> {
                WebViewActivity.to(context, Constant.USER_SERVICE_AGREEMENT_URL, "服务协议")
            }
            R.id.iv_login -> {
                if (TextUtils.isEmpty(vb?.etLoginPhone?.text)) {
                    ToastUtil.showToast("请填写手机号码")
                    return
                }

                if (TextUtils.isEmpty(vb?.etLoginVerifyCode?.text)) {
                    ToastUtil.showToast("验证码不能为空!")
                    return
                }

                if (vb?.ivLoginIcon?.isChecked != true) {
                    ToastUtil.showToast("请勾选用户协议")
                    return
                }

                login(vb?.etLoginPhone?.text.toString(), vb?.etLoginVerifyCode?.text.toString())
            }
            else -> {
            }
        }
    }

    private fun sendVerifyCode(phone: String?) {
        val param = SendCodeParam()
        param.phone = phone
        param.userId = ""
        ApiService.instance
                .sendCode(param)
                .subscribe(object : CommonObserver<SendMessageBean>() {
                    override fun onResponseSuccess(response: SendMessageBean?) {
                        ToastUtil.showToast("验证码发送成功")
                        vb?.etLoginVerifyCode?.requestFocus()

                        //热云注册
                        if (response?.isNewUser == 1) {
                            TrackingAgentUtils.setRegisterWithAccountID(TrackingAgentUtils.getDeviceId())
                        }
                    }
                })
    }

    private fun login(phoneNumber: String?, verifyCode: String?) {
        val param = LoginParam()
        param.phone = phoneNumber
        param.verificationCode = verifyCode
        ApiService.instance
                .login(param)
                .subscribe(object : CommonObserver<LoginBean>() {
                    override fun onResponseSuccess(response: LoginBean?) {
                        initLoginEvent(response)
                    }
                })
    }

    private fun initLoginEvent(data: LoginBean?) {
        if (null == data) {
            return
        }

        val userBean = data.userBasic
        PreferencesUtils.putString(PreferencesKey.TOKEN, data.token)
        PreferencesUtils.putString(PreferencesKey.USER_ID, userBean.userId)
        PreferencesUtils.putString(PreferencesKey.USER_NAME, userBean.name)
        PreferencesUtils.putString(PreferencesKey.USER_AVATOR, userBean.avatar)
        PreferencesUtils.putBoolean(PreferencesKey.ONLINE, true)
        PreferencesUtils.putInt(PreferencesKey.AUTH_STATUS, userBean.realStatus)
        PreferencesUtils.putInt(PreferencesKey.VIP, userBean.vip)
        PreferencesUtils.putString(PreferencesKey.PHONE, vb?.etLoginVerifyCode?.text.toString())
        ToastUtil.showToast("登录成功")
        EventBus.getDefault().post(MainMessageBean(1))

        //热云登录
        TrackingAgentUtils.setLoginSuccessBusiness(TrackingAgentUtils.getDeviceId())

        //通知登录成功
        EventBus.getDefault().post(LoginEvent())
        setResult(1, Intent())
        finish()
    }

    //验证码倒计时
    private var countDownTimer: CountDownTimer? = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val time = millisUntilFinished / 1000
            vb?.let {
                it.tvLoginGetCode.text = String.format("%ss", time)
                it.tvLoginGetCode.isClickable = false
            }
        }

        override fun onFinish() {
            vb?.let {
                it.tvLoginGetCode.text = "获取验证码"
                it.tvLoginGetCode.isClickable = true
            }
        }
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        countDownTimer?.onFinish()
        countDownTimer = null
        super.onDestroy()
    }
}