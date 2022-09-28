package com.maishuo.tingshuohenhaowan.ui.dialog

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.databinding.ViewCommonDialogLayoutBinding
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackListener
import com.qichuang.commonlibs.basic.BaseDialog
import com.qichuang.commonlibs.utils.ScreenUtils

/**
 * author : xpSun
 * date : 10/13/21
 * description :
 */
class CommonDialog constructor(activity: AppCompatActivity?) :
        BaseDialog(activity, R.style.CustomDialog) {

    private var binding: ViewCommonDialogLayoutBinding? = null

    var showLeft: Boolean? = null
        set(value) {
            field = value
            binding?.btDialogCommonCancle?.visibility =
                    if (value == true)
                        View.VISIBLE
                    else
                        View.GONE
        }
    var showRight: Boolean? = null
        set(value) {
            field = value
            binding?.btDialogCommonSure?.visibility =
                    if (value == true)
                        View.VISIBLE
                    else
                        View.GONE
        }
    var title: String? = null
        set(value) {
            field = value
            binding?.tvDialogCommonTitle?.text = value
        }
    var content: String? = null
        set(value) {
            field = value
            binding?.tvDialogCommonContent?.text = value
        }
    var leftText: String? = null
        set(value) {
            field = value
            binding?.btDialogCommonCancle?.text = value
        }
    var rightText: String? = null
        set(value) {
            field = value
            binding?.btDialogCommonSure?.text = value
        }
    var onDialogBackListener: OnDialogBackListener? = null

    override fun fetchRootView(): View? {
        binding = ViewCommonDialogLayoutBinding.inflate(LayoutInflater.from(activity))
        return binding?.root
    }

    override fun initWidgets() {
        val width = ScreenUtils.getScreenWidth(activity) * 0.9
        setGravity(width = width.toInt())

        binding?.let {
            it.btDialogCommonCancle.setOnClickListener {
                onDialogBackListener?.onCancel()
                dismiss()
            }
            it.btDialogCommonSure.setOnClickListener {
                onDialogBackListener?.onSure("")
                dismiss()
            }
        }
    }
}