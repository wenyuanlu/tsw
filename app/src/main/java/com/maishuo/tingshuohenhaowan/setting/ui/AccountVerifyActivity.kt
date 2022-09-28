package com.maishuo.tingshuohenhaowan.setting.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.api.param.AccountSendParam
import com.maishuo.tingshuohenhaowan.api.param.LoginParam
import com.maishuo.tingshuohenhaowan.api.param.ReplaceAccountParam
import com.maishuo.tingshuohenhaowan.api.param.SendCodeParam
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity
import com.maishuo.tingshuohenhaowan.databinding.ActivityAccountVerifyLayoutBinding
import com.maishuo.tingshuohenhaowan.api.response.LoginBean
import com.maishuo.tingshuohenhaowan.api.response.SendMessageBean
import com.maishuo.tingshuohenhaowan.api.response.VerifyCodeBean
import com.qichuang.commonlibs.utils.ToastUtil
import com.qichuang.commonlibs.utils.PhoneUtils
import com.qichuang.retrofit.CommonObserver

/**
 * 账号验证页面 ( 更换账号 和 注销账号 )
 */
class AccountVerifyActivity : CustomBaseActivity<ActivityAccountVerifyLayoutBinding>() {

    companion object {
        const val ACCOUNT_VERIFY_TYPE_TAG = "account_verify_type"
        const val ACCOUNT_VERIFY_PHONE_TAG = "account_verify_phone"

        private const val TYPE_DEFAULT_VALUE = 0

        //1更换手机号的验证
        private const val TYPE_PHONE_VERIFICATION: Int = 1

        //2更换手机号
        private const val TYPE_CHANGER_PHONE: Int = 2

        //3注销账号
        private const val TYPE_REMOVE_ACCOUNT: Int = 3

        // 4绑定手机号(用于朋友圈的绑定等等,ios的绑定需求等)
        private const val TYPE_BINDING_PHONE: Int = 4
    }

    private var mType: Int? = null

    //用户手机号
    private var mPhone: String? = null

    override fun initView() {
        vb?.tvVerifyGetCode?.setOnClickListener(this)
        vb?.btVerifySure?.setOnClickListener(this)
    }

    override fun initData() {
        mType = intent.getIntExtra(ACCOUNT_VERIFY_TYPE_TAG, TYPE_DEFAULT_VALUE)
        mPhone = intent.getStringExtra(ACCOUNT_VERIFY_PHONE_TAG)

        vb?.let {
            it.etVerifyPhone.hint = when (mType) {
                TYPE_PHONE_VERIFICATION -> {
                    setTitle("账号验证")
                    it.tvVerifyHint.visibility = View.GONE
                    if (!TextUtils.isEmpty(mPhone)) {
                        it.etVerifyPhone.setText(mPhone)
                        it.etVerifyPhone.isEnabled = false
                    } else {
                        it.etVerifyPhone.isEnabled = true
                    }
                    "请输入原手机号"
                }
                TYPE_CHANGER_PHONE -> {
                    setTitle("更换手机号")
                    it.tvVerifyHint.visibility = View.VISIBLE
                    it.etVerifyPhone.isEnabled = true
                    "请输入新手机号"
                }
                TYPE_REMOVE_ACCOUNT -> {
                    setTitle("注销账号")
                    it.tvVerifyHint.visibility = View.GONE
                    it.etVerifyPhone.isEnabled = true
                    "请输入原手机号"
                }
                TYPE_BINDING_PHONE -> {
                    setTitle("绑定手机号")
                    it.tvVerifyHint.visibility = View.GONE
                    it.etVerifyPhone.isEnabled = true
                    "请输入手机号"
                }
                else -> {
                    ""
                }
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_verify_get_code -> {
                when (mType) {
                    TYPE_PHONE_VERIFICATION,
                    TYPE_REMOVE_ACCOUNT -> {
                        if (TextUtils.isEmpty(vb?.etVerifyPhone?.text)) {
                            ToastUtil.showToast("请填写手机号码")
                            return
                        }

                        if (!PhoneUtils.checkPhoneNumber(vb?.etVerifyPhone?.text.toString())) {
                            ToastUtil.showToast("请填写手机号码")
                            return
                        }

                        getVerifyCode(vb?.etVerifyPhone?.text.toString())
                    }
                    TYPE_CHANGER_PHONE -> {
                        if (TextUtils.isEmpty(vb?.etVerifyPhone?.text)) {
                            ToastUtil.showToast("请填写手机号码")
                            return
                        }

                        if (!PhoneUtils.checkPhoneNumber(vb?.etVerifyPhone?.text.toString())) {
                            ToastUtil.showToast("请填写手机号码")
                            return
                        }

                        getChangeVerifyCode(vb?.etVerifyPhone?.text.toString())
                    }
                }
            }
            R.id.bt_verify_sure -> {
                vb?.let {
                    if (TextUtils.isEmpty(it.etVerifyPhone.text)) {
                        ToastUtil.showToast("请填写手机号码")
                        return
                    }

                    if (!PhoneUtils.checkPhoneNumber(it.etVerifyPhone.text.toString())) {
                        ToastUtil.showToast("请填写手机号码")
                        return
                    }

                    if (TextUtils.isEmpty(it.etVerifyCode.text)
                            || 4 != it.etVerifyCode.text?.length ?: 0) {
                        ToastUtil.showToast("请填写正确的验证码")
                        return
                    }

                    val phone = it.etVerifyPhone.text.toString()
                    val code = it.etVerifyCode.text.toString()

                    when (mType) {
                        TYPE_PHONE_VERIFICATION -> { //更换手机号的账号校验
                            loginAndVerify(phone, code, "1")
                        }
                        TYPE_CHANGER_PHONE -> { //更换手机号
                            replaceAccountPhone(phone, code)
                        }
                        TYPE_REMOVE_ACCOUNT -> { //注销账号
                            loginAndVerify(phone, code, "2")
                        }
                    }
                }
            }
            else -> {
            }
        }
    }

    //获取验证码
    private fun getVerifyCode(phone: String) {
        val param = SendCodeParam()
        param.phone = phone
        param.userId = ""
        ApiService.instance
                .sendCode(param)
                .subscribe(object : CommonObserver<SendMessageBean>() {
                    override fun onResponseSuccess(response: SendMessageBean?) {
                        if (response != null) {
                            countDownTimer!!.start() //开始倒计时
                            ToastUtil.showToast("验证码发送成功")
                            vb!!.tvVerifyGetCode.requestFocus()
                        }
                    }
                })
    }

    //获取更换手机的验证码
    private fun getChangeVerifyCode(phone: String) {
        val param = AccountSendParam()
        param.phone = phone
        ApiService.instance
                .accountSend(param)
                .subscribe(object : CommonObserver<VerifyCodeBean>() {
                    override fun onResponseSuccess(response: VerifyCodeBean?) {
                        if (null == response) {
                            return
                        }
                        val status = response.status
                        if (status == 1) {
                            countDownTimer!!.start() //开始倒计时
                            ToastUtil.showToast("验证码发送成功")
                            vb!!.etVerifyCode.requestFocus()
                        }
                    }
                })
    }

    //校验接口(用登录的接口进行校验)
    //type=1,更换手机号的校验,type=2,注销的校验
    private fun loginAndVerify(phoneNumber: String, verifyCode: String, type: String) {
        val param = LoginParam()
        param.phone = phoneNumber
        param.verificationCode = verifyCode
        param.type = type
        ApiService.instance
                .login(param)
                .subscribe(object : CommonObserver<LoginBean>() {
                    override fun onResponseSuccess(response: LoginBean?) {
                        if (type == "1") {
                            //从账号验证跳转到更换手机号界面
                            val intent = Intent(this@AccountVerifyActivity, AccountVerifyActivity::class.java)
                            intent.putExtra(ACCOUNT_VERIFY_TYPE_TAG, TYPE_CHANGER_PHONE)
                            intent.putExtra(ACCOUNT_VERIFY_PHONE_TAG, mPhone)
                            startActivity(intent)
                            finish()
                        } else if (type == "2") {
                            ///跳转到账号注销的提示的界面
                            val intent = Intent(this@AccountVerifyActivity, AccountRemoveActivity::class.java)
                            intent.putExtra(AccountRemoveActivity.PHONE, phoneNumber)
                            intent.putExtra(AccountRemoveActivity.CODE, verifyCode)
                            startActivity(intent)
                            finish()
                        }
                    }
                })
    }

    //更换手机号
    private fun replaceAccountPhone(phone: String, code: String) {
        val param = ReplaceAccountParam()
        param.phone = phone
        param.verificationCode = code
        ApiService.instance
                .replaceAccount(param)
                .subscribe(object : CommonObserver<Any>() {
                    override fun onResponseSuccess(response: Any?) {
                        ToastUtil.showToast("操作完成")
                        finish()
                    }
                })
    }

    //验证码倒计时
    private var countDownTimer: CountDownTimer? = object : CountDownTimer(1000 * 60, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val time = millisUntilFinished / 1000
            vb!!.tvVerifyGetCode.text = String.format("%ss", time)
            vb!!.tvVerifyGetCode.isClickable = false
        }

        override fun onFinish() {
            vb!!.tvVerifyGetCode.text = "获取验证码"
            vb!!.tvVerifyGetCode.isClickable = true
        }
    }

    override fun onDestroy() {
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
            countDownTimer!!.onFinish()
            countDownTimer = null
        }
        super.onDestroy()
    }
}