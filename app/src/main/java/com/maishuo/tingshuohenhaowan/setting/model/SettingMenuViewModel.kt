package com.maishuo.tingshuohenhaowan.setting.model

import android.content.Intent
import com.google.zxing.integration.android.IntentIntegrator
import com.maishuo.tingshuohenhaowan.auth.AuthUtil
import com.maishuo.tingshuohenhaowan.databinding.ActivitySettingMenuLayoutBinding
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackListener
import com.maishuo.tingshuohenhaowan.rtmchat.ChatLoginUtils
import com.maishuo.tingshuohenhaowan.setting.ui.*
import com.maishuo.tingshuohenhaowan.utils.CleanDataUtils
import com.maishuo.tingshuohenhaowan.utils.DialogUtils
import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils
import com.maishuo.tingshuohenhaowan.utils.Utils
import com.maishuo.umeng.ConstantEventId
import com.qichuang.commonlibs.basic.BaseActivity
import com.qichuang.commonlibs.basic.BaseViewModel
import com.qichuang.commonlibs.common.PreferencesKey
import com.qichuang.commonlibs.utils.PreferencesUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus

/**
 * author : xpSun
 * date : 9/13/21
 * description :
 */
class SettingMenuViewModel constructor(val activity: BaseActivity?,
                                       val binding: ActivitySettingMenuLayoutBinding?) : BaseViewModel() {

    //扫一扫
    fun scanClick() {
        try {
            IntentIntegrator(activity)
                    .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE) // 扫码的类型,可选：一维码，二维码，一/二维码
                    .setPrompt("请对准二维码") // 设置提示语
                    .setCameraId(0) // 选择摄像头,可使用前置或者后置
                    .setBeepEnabled(true) // 是否开启声音,扫完码之后会"哔"的一声
                    .setCaptureActivity(ScanQrCodeActivity::class.java) //自定义扫码界面
                    .initiateScan() // 初始化扫码

            TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_mine_vip_scan)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //兴趣设置
    fun interestSettingClick() {
        activity?.startActivity(Intent(activity, InterestActivity::class.java))
        TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_mine_vip_interest)
    }

    //播放模式
    fun playerModelClick() {
        DialogUtils.showPlayTypeDialog(activity)
        TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_mine_vip_play)
    }

    //定时设置
    fun timingSettingClick() {
        DialogUtils.showTimingDialog(activity) {
            EventBus.getDefault().post(it)

            binding?.settingTimingDesc?.text = if (it.type == 0) {
                ""
            } else {
                it.desc
            }
        }

        TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_mine_vip_timing)
    }

    //实名认证
    fun realNameAuthClick() {
        if (!Utils.isFastClick()) {
            return
        }
        val auth = PreferencesUtils.getInt(PreferencesKey.AUTH_STATUS, 0) //1-认证通过 0-未认证

        if (auth == 2 || auth == 0) {
            //去认证
            AuthUtil.getInstance(activity).auth()
        }

        TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_mine_set_Real)
    }

    //刷新用户实名认证
    fun refreshUserAuth() {
        val auth = PreferencesUtils.getInt(PreferencesKey.AUTH_STATUS, 0) //1-认证通过 0-未认证
        binding?.settingRealNameAuthDesc?.let {
            it.text = if (1 == auth) {
                "已认证"
            } else {
                "未认证"
            }
        }
    }

    //清除缓存
    fun clearCacheClick() {
        try {
            binding?.settingClearCacheDesc?.text = "0M"
            GlobalScope.launch {
                CleanDataUtils.clearAllCache(activity)
            }
            TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_mine_set_clean)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //账号安全
    fun accountSecurityClick() {
        activity?.startActivity(Intent(activity, AccountSecurityActivity::class.java))
        TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_mine_set_safe)
    }

    //隐私设置
    fun privacySettingClick() {
        activity?.startActivity(Intent(activity, PrivacySettingActivity::class.java))
    }

    //关于我们
    fun aboutWeClick() {
        activity?.startActivityForResult(Intent(activity, AboutUsActivity::class.java), 1)
        TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_mine_set_about)
    }

    //意见反馈
    fun feedBackClick() {
        activity?.startActivity(Intent(activity, SuggestActivity::class.java))
        TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_mine_set_feedback)
    }

    //登出
    fun loginOutClick() {
        DialogUtils.showCommonDialog(
                activity,
                "是否退出登录?",
                object : OnDialogBackListener {
                    override fun onSure(content: String) {
                        ChatLoginUtils.loginOut {
                            activity?.finish()
                        }
                    }

                    override fun onCancel() {}
                })
    }
}