package com.maishuo.tingshuohenhaowan.main.dialog

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.databinding.ViewMainIndexDialogLayoutBinding
import com.qichuang.commonlibs.basic.BaseDialog

class MainIndexDialog constructor(appCompatActivity: AppCompatActivity?) :
        BaseDialog(appCompatActivity, R.style.DialogTheme) {

    private var binding: ViewMainIndexDialogLayoutBinding? = null

    override fun fetchRootView(): View? {
        binding = ViewMainIndexDialogLayoutBinding.inflate(LayoutInflater.from(activity))
        return binding?.root
    }

    override fun initWidgets() {
        setCanceledOnTouchOutside(false)

        binding?.mainIndexCenterNext?.setOnClickListener {
            binding?.mainIndexCourseCenter?.visibility = View.GONE
            binding?.mainIndexCenterNext?.visibility = View.GONE
            binding?.mainIndexCourseBottom?.visibility = View.VISIBLE
            binding?.mainIndexCourseComplete?.visibility = View.VISIBLE
        }

        binding?.mainIndexCourseComplete?.setOnClickListener { dismiss() }
    }
}