package com.maishuo.tingshuohenhaowan.setting.ui

import android.os.NetworkOnMainThreadException
import android.text.TextUtils
import com.maishuo.tingshuohenhaowan.BuildConfig
import com.maishuo.tingshuohenhaowan.common.Constant
import com.maishuo.tingshuohenhaowan.common.CustomApplication
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity
import com.maishuo.tingshuohenhaowan.databinding.ActivityAboutUsBinding
import com.maishuo.tingshuohenhaowan.listener.OnProxyDialogBackListener
import com.maishuo.tingshuohenhaowan.rtmchat.ChatLoginUtils
import com.maishuo.tingshuohenhaowan.rtmchat.FriendChatUtil
import com.maishuo.tingshuohenhaowan.ui.activity.WebViewActivity
import com.maishuo.tingshuohenhaowan.utils.CustomPreferencesUtils
import com.maishuo.tingshuohenhaowan.utils.DialogUtils
import com.qichuang.commonlibs.utils.DeviceUtil
import com.qichuang.commonlibs.utils.PreferencesUtils
import com.qichuang.commonlibs.utils.ToastUtil
import com.qichuang.retrofit.ApiConstants
import com.qichuang.retrofit.ApiConstants.isProxy
import com.qichuang.retrofit.ApiServiceFactory.Companion.instance
import okhttp3.OkHttpClient
import java.net.InetSocketAddress
import java.net.Proxy

/**
 * author ：xpSun
 * date : 9/13/21
 * description : 关于我们
 */
class AboutUsActivity : CustomBaseActivity<ActivityAboutUsBinding>() {

    private var count = 0

    override fun initView() {
        setTitle("关于我们")

        vb?.let {
            it.tvAboutUsVersion.text = String.format("当前版本V%s", DeviceUtil.getVersionName())

            val tel = CustomPreferencesUtils.fetchComplaintPhone()
            it.aboutWeComplaintPhone.text = String.format("投诉电话: %s", if (TextUtils.isEmpty(tel)) {
                ""
            } else {
                tel
            })

            //服务协议
            it.tvAboutUsProtocol.setOnClickListener {
                WebViewActivity.to(
                        this,
                        "https://live.tingshuowan.com/listen/agreement?type=1",
                        "服务协议"
                )
            }

            //隐私协议
            it.tvAboutUsPrivacy.setOnClickListener {
                WebViewActivity.to(
                        this,
                        "https://live.tingshuowan.com/listen/agreement?type=2",
                        "隐私协议"
                )
            }

            //设置代理
            it.viewRight.setOnClickListener { clickRule() }
        }
    }

    override fun initData() {}

    /**
     * 点击规则
     */
    private fun clickRule() {
        count++
        postDelayed({
            count--
            count = count.coerceAtLeast(0)
        }, 2000)
        if (count > 10) {
            count = 0
            showProxyDialog()
        }
    }

    /**
     * 设置代理弹窗
     */
    private fun showProxyDialog() {
        DialogUtils.showProxyDialog(
                this,
                String.format("当前环境：%s", if (ApiConstants.isDebug) "测试" else "正式"),
                String.format("当前版本：%s", BuildConfig.BUILD_TYPE),
                String.format("当前渠道：%s", CustomApplication.getApp().channelName),
                PreferencesUtils.getString("ProxyIp"),
                object : OnProxyDialogBackListener {
                    override fun onRelease(content: String) {
                        //代理设置失败了，切了环境也不能用
                        if (setProxy(content)) {
                            if (ApiConstants.isDebug) {
                                setServer(true)
                            } else {
                                changeProxySetting()
                            }
                        }
                    }

                    override fun onDebug(content: String) {
                        //代理设置失败了，切了环境也不能用
                        if (setProxy(content)) {
                            if (!ApiConstants.isDebug) {
                                setServer(false)
                            } else {
                                changeProxySetting()
                            }
                        }
                    }
                })
    }

    /**
     * 设置服务环境
     */
    private fun setServer(isRelease: Boolean) {
        ApiConstants.isDebug = !isRelease
        PreferencesUtils.putBoolean(Constant.COMMON_CHANGER_SERVICE_TAG, ApiConstants.isDebug)

        //登录声网相关
        FriendChatUtil.getInstance().initSDK(CustomApplication.getApp())
        FriendChatUtil.getInstance().initChat()
        ChatLoginUtils.gotoOnLine()
        ChatLoginUtils.loginOut { changeProxySetting() }
    }

    /**
     * 代理切换后通知首页关闭侧滑菜单并finish当前页面
     */
    private fun changeProxySetting() {
        setResult(RESULT_OK)
        finish()
    }

    /**
     * 设置代理
     */
    private fun setProxy(content: String): Boolean {
        try {
            val builder: OkHttpClient.Builder = instance.fetchBuilder()
            isProxy = if (!TextUtils.isEmpty(content)) {
                if (DeviceUtil.pingProxy(content)) {
                    builder.proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress(content, 8888)))
                    true
                } else {
                    ToastUtil.showToast("IP地址Ping不通，请检查")
                    PreferencesUtils.putString("ProxyIp", "")
                    return false
                }
            } else {
                true
            }
            ToastUtil.showToast("设置成功")
            PreferencesUtils.putString("ProxyIp", content)

            //retrofit 设置代理
            instance.init(builder)
        } catch (e: NetworkOnMainThreadException) {
            e.printStackTrace()
            ToastUtil.showToast("IP地址不正确")
            return false
        }
        return isProxy
    }
}