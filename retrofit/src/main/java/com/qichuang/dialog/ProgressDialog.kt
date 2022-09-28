package com.qichuang.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.qichuang.commonlibs.basic.BaseDialog
import com.qichuang.retrofit.R
import com.qichuang.retrofit.databinding.ViewProgressDialogLayoutBinding

/**
 * author : xpSun
 * date : 10/25/21
 * description : 通用的等待加载框
 */
class ProgressDialog constructor(appCompatActivity: AppCompatActivity?)
    : BaseDialog(appCompatActivity, R.style.transparentDialogStyle) {

    private var binding: ViewProgressDialogLayoutBinding? = null

    var message: String? = null
        set(value) {
            field = value
            binding?.progressTvShow?.text = value
        }

    var cancelable: Boolean? = null
        set(value) {
            field = value
            setCancelable(cancelable ?: false)
        }

    override fun fetchRootView(): View? {
        binding = ViewProgressDialogLayoutBinding.inflate(LayoutInflater.from(activity))
        return binding?.root
    }

    override fun initWidgets() {
        setGravity(
                width = ViewGroup.LayoutParams.WRAP_CONTENT,
                height = ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

}