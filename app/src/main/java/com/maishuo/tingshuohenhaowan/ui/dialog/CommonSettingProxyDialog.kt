package com.maishuo.tingshuohenhaowan.ui.dialog

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.databinding.ViewSettingProxyDialogLayoutBinding
import com.maishuo.tingshuohenhaowan.listener.OnProxyDialogBackListener
import com.qichuang.commonlibs.basic.BaseDialog
import com.qichuang.commonlibs.utils.ScreenUtils

/**
 * author : xpSun
 * date : 10/13/21
 * description :
 */
class CommonSettingProxyDialog constructor(activity: AppCompatActivity?) :
        BaseDialog(activity, R.style.CustomDialog) {

    private var binding: ViewSettingProxyDialogLayoutBinding? = null

    var environment: String? = null
        set(value) {
            field = value
            binding?.tvDialogTitle?.text = value
        }
    var version: String? = null
        set(value) {
            field = value
            binding?.tvDialogSubTitle?.text = value
        }
    var channel: String? = null
        set(value) {
            field = value
            binding?.tvDialogSub2Title?.text = value
        }
    var content: String? = null
        set(value) {
            field = value
            if (!TextUtils.isEmpty(value)) {
                binding?.etContent?.setText(value)
                binding?.etContent?.setSelection(value?.length ?: 0)
            }
        }

    var onProxyDialogBackListener: OnProxyDialogBackListener? = null

    override fun fetchRootView(): View? {
        binding = ViewSettingProxyDialogLayoutBinding.inflate(LayoutInflater.from(activity))
        return binding?.root
    }

    override fun initWidgets() {
        val width = ScreenUtils.getScreenWidth(activity) * 0.8
        setGravity(width = width.toInt())

        binding?.let {
            it.tvRelease.setOnClickListener {
                onProxyDialogBackListener?.onRelease(binding?.etContent?.text.toString())
            }
            it.tvDebug.setOnClickListener {
                onProxyDialogBackListener?.onDebug(binding?.etContent?.text.toString())
            }
        }
    }
}