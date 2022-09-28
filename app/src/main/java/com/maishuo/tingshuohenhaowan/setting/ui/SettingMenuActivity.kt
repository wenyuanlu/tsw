package com.maishuo.tingshuohenhaowan.setting.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.google.zxing.integration.android.IntentIntegrator
import com.maishuo.tingshuohenhaowan.api.param.CheckAuthParam
import com.maishuo.tingshuohenhaowan.api.response.CheckAuthResponse
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService
import com.maishuo.tingshuohenhaowan.common.AutoClosePlayEnum
import com.maishuo.tingshuohenhaowan.common.CustomBasicActivity
import com.maishuo.tingshuohenhaowan.databinding.ActivitySettingMenuLayoutBinding
import com.maishuo.tingshuohenhaowan.api.response.UpdateImageApiResponse
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackDissmissListener
import com.maishuo.tingshuohenhaowan.setting.model.SettingMenuViewModel
import com.maishuo.tingshuohenhaowan.utils.CleanDataUtils
import com.maishuo.tingshuohenhaowan.utils.DialogUtils
import com.qichuang.commonlibs.common.PreferencesKey
import com.qichuang.commonlibs.utils.DeviceUtil
import com.qichuang.commonlibs.utils.LogUtils
import com.qichuang.commonlibs.utils.PreferencesUtils
import com.qichuang.commonlibs.utils.ToastUtil
import com.qichuang.retrofit.CommonObserver
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author : xpSun
 * date : 9/13/21
 * description :
 */
class SettingMenuActivity : CustomBasicActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SettingMenuActivity::class.java)
            context.startActivity(intent)
        }
    }

    private var binding: ActivitySettingMenuLayoutBinding? = null
    private var viewModel: SettingMenuViewModel? = null
    private var mIsOpen = true //是否开启通知权限

    override fun fetchLayoutView(): View? {
        binding = ActivitySettingMenuLayoutBinding.inflate(LayoutInflater.from(this))
        viewModel = SettingMenuViewModel(this, binding)
        binding?.viewModel = viewModel
        return binding?.root
    }

    override fun initView() {
        setTitle("设置")

        EventBus.getDefault().register(this)

        val time = PreferencesUtils.getLong(PreferencesKey.TIMING)
        val autoClosePlayEnums = AutoClosePlayEnum.values()

        var autoCloseContent = ""
        for (i in autoClosePlayEnums.indices) {
            val autoClosePlayEnum = autoClosePlayEnums[i]
            if (i != 0 && autoClosePlayEnum.value == time) {
                autoCloseContent = autoClosePlayEnum.desc
                break
            }
        }

        val totalCacheSize = CleanDataUtils.getTotalCacheSize(this)
        val auth = PreferencesUtils.getInt(PreferencesKey.AUTH_STATUS, 0) //1-认证通过 0-未认证

        binding?.let {
            it.settingTimingDesc.text = autoCloseContent
            it.settingRealNameAuthDesc.text = if (1 == auth) {
                "已认证"
            } else {
                "未认证"
            }
            it.settingClearCacheDesc.text = totalCacheSize

            it.settingNoticeSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (!it.settingNoticeSwitch.isPressed) {
                    return@setOnCheckedChangeListener
                }

                LogUtils.LOGE("111", "是否开启=$isChecked")

                if (mIsOpen) {
                    DeviceUtil.startNotificationSetting(this@SettingMenuActivity)
                } else {
                    DialogUtils.showCommonDialog(this@SettingMenuActivity, "打开通知权限",
                            "第一时间获取点赞、弹幕、好友互动 等消息通知", "取消", "去开启",
                            true, true, true, object : OnDialogBackDissmissListener {
                        override fun onSure(content: String) {
                            DeviceUtil.startNotificationSetting(this@SettingMenuActivity)
                        }

                        override fun onCancel() {
                            mIsOpen = false
                            it.settingNoticeSwitch.isChecked = false
                        }

                        override fun dismiss() {
                            mIsOpen = DeviceUtil.checkNotifySetting(this@SettingMenuActivity)
                            it.settingNoticeSwitch.isChecked = mIsOpen
                        }
                    })
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null) {
            val result = intentResult.contents //二维码扫描返回值
            if (result == null) {
                ToastUtil.showToast("扫码失败")
            } else {
                if (result.contains("check/auth?")) {
                    val split = result.split("check/auth\\?".toRegex()).toTypedArray()
                    val key = split[1]
                    checkAuth(key)
                } else {
                    ToastUtil.showToast("扫码失败")
                }
            }
        } else {
            if (requestCode == 1 && resultCode == RESULT_OK) {
                finish()
            }
        }
    }

    /**
     * 扫码结果验证
     */
    private fun checkAuth(key: String?) {
        val param = CheckAuthParam()
        param.key = key
        ApiService.instance
                .checkAuth(param)
                .subscribe(object : CommonObserver<CheckAuthResponse>() {
                    override fun onResponseSuccess(response: CheckAuthResponse?) {
                        if (1 == response?.authLogin ?: 0) {
                            ToastUtil.showToast("登录成功")
                        }
                    }
                })
    }

    override fun onResume() {
        super.onResume()

        mIsOpen = DeviceUtil.checkNotifySetting(this)
        binding?.settingNoticeSwitch?.isChecked = mIsOpen
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(data: UpdateImageApiResponse) {
        PreferencesUtils.putInt(PreferencesKey.AUTH_STATUS, data.status) //1-认证通过 0-未认证

        viewModel?.refreshUserAuth()
    }

    override fun onDestroy() {
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }
}