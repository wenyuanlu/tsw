package com.maishuo.tingshuohenhaowan.main.dialog

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.databinding.ViewShowActivityDialogLayoutBinding
import com.maishuo.tingshuohenhaowan.ui.activity.WebViewActivity
import com.maishuo.tingshuohenhaowan.utils.LoginUtil
import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils
import com.qichuang.commonlibs.basic.BaseDialog
import com.qichuang.commonlibs.utils.GlideUtils

class ShowActivityDialog constructor(appCompatActivity: AppCompatActivity) :
        BaseDialog(appCompatActivity, R.style.DialogTheme) {

    private var binding: ViewShowActivityDialogLayoutBinding? = null
    private var ldpUrl: String? = null
    private var img: String? = null
    private var title: String? = null

    fun setShowData(img: String?, ldpUrl: String?, title: String?) {
        this.img = img
        this.ldpUrl = ldpUrl
        this.title = title

        val options = RequestOptions().transform(RoundedCorners(30))

        if (null == activity) {
            return
        }

        binding?.let {
            GlideUtils.loadImage(
                    activity,
                    img,
                    it.ivActivityCenter,
                    options)
        }
    }

    override fun fetchRootView(): View? {
        binding = ViewShowActivityDialogLayoutBinding.inflate(LayoutInflater.from(activity))
        return binding?.root
    }

    override fun initWidgets() {
        setCanceledOnTouchOutside(false)

        binding?.ivActivityCenter?.setOnClickListener {
            if (!LoginUtil.isLogin(activity)) {
                return@setOnClickListener
            }
            WebViewActivity.to(activity, ldpUrl, title)
            TrackingAgentUtils.setEvent("event_4")
            dismiss()
        }

        binding?.ivActivityClose?.setOnClickListener { dismiss() }
    }
}