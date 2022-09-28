package com.maishuo.tingshuohenhaowan.setting.ui

import android.content.Intent
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.common.CustomBasicActivity
import com.maishuo.tingshuohenhaowan.databinding.ActivityUserPersonalEditLayoutBinding
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackListener
import com.maishuo.tingshuohenhaowan.setting.model.UserPersonalEditViewModel
import com.maishuo.tingshuohenhaowan.utils.DialogUtils
import com.qichuang.commonlibs.utils.GlideUtils


/**
 * author : xpSun
 * date : 9/14/21
 * description :
 */
class UserPersonalEditActivity : CustomBasicActivity() {

    private var binding: ActivityUserPersonalEditLayoutBinding? = null
    private var viewModel: UserPersonalEditViewModel? = null

    override fun fetchLayoutView(): View? {
        binding = ActivityUserPersonalEditLayoutBinding.inflate(LayoutInflater.from(this))
        viewModel = UserPersonalEditViewModel(this, binding)
        binding?.viewmodel = viewModel
        return binding?.root
    }

    override fun initView() {
        setTitle(getString(R.string.personal_center))
        setRightText(R.string.commit)

        viewModel?.initPreferences()
    }

    override fun initData() {
        setRightTextOnClick {
            viewModel?.commonPersonalInfo()
        }

        ivBack.setOnClickListener {
            backListener()
        }
    }

    private fun backListener() {
        if (viewModel?.isChanged() == true) {
            DialogUtils.showCommonDialog(
                    this,
                    getString(R.string.tips),
                    getString(R.string.signout_will_not_be_saved_data),
                    object : OnDialogBackListener {
                        override fun onSure(content: String) {
                            finish()
                        }

                        override fun onCancel() {}
                    })
        } else {
            finish()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backListener()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                val auator = data.getStringExtra("auator")
                if (!TextUtils.isEmpty(auator)) {
                    viewModel?.newAuator = auator

                    binding?.userPersonalEditHeader?.let {
                        if (!TextUtils.isEmpty(viewModel?.newAuator)) {
                            GlideUtils.initImageForHead(
                                    context,
                                    viewModel?.newAuator,
                                    R.mipmap.home_head_pic_default,
                                    it
                            )
                        }
                    }
                }
            }
        }
    }
}