package com.maishuo.tingshuohenhaowan.setting.ui

import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity
import com.maishuo.tingshuohenhaowan.databinding.ActivityPrivacySettingBinding
import com.maishuo.tingshuohenhaowan.ui.activity.WebViewActivity
import com.qichuang.commonlibs.common.PreferencesKey
import com.qichuang.commonlibs.utils.PreferencesUtils
import com.qichuang.commonlibs.utils.ToastUtil
import com.qichuang.retrofit.ApiConstants

class PrivacySettingActivity : CustomBaseActivity<ActivityPrivacySettingBinding>() {


    override fun initView() {
        setTitle("隐私设置")
        vb?.privacySettingSwitch?.isChecked = PreferencesUtils.getBoolean(PreferencesKey.PRIVACY_SETTING_AD,true)
        vb?.privacySettingSwitch2?.isChecked = PreferencesUtils.getBoolean(PreferencesKey.PRIVACY_SETTING_CONTENT,true)
    }

    override fun initData() {
        vb?.privacySettingSwitch?.setOnCheckedChangeListener { _, isChecked ->
            PreferencesUtils.putBoolean(PreferencesKey.PRIVACY_SETTING_AD, isChecked)
        }

        vb?.privacySettingSwitch2?.setOnCheckedChangeListener { _, isChecked ->
            PreferencesUtils.putBoolean(PreferencesKey.PRIVACY_SETTING_CONTENT, isChecked)
        }
        vb?.tvPrivacyThird?.setOnClickListener { WebViewActivity.to(this, ApiConstants.THIRD_SDK_LIST,"第三方SDK列表") }
    }


}