package com.maishuo.tingshuohenhaowan.ui.dialog

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.databinding.ViewCommonEditDialogLayoutBinding
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackListener
import com.qichuang.commonlibs.basic.BaseDialog
import com.qichuang.commonlibs.utils.ScreenUtils

/**
 * author : xpSun
 * date : 10/13/21
 * description :
 */
class CommonEditDialog constructor(activity: AppCompatActivity?) :
        BaseDialog(activity, R.style.CustomDialog) {

    private var binding: ViewCommonEditDialogLayoutBinding? = null

    var showLeft: Boolean? = null
        set(value) {
            field = value
            binding?.btDialogCommonEditCancle?.visibility =
                    if (value == true)
                        View.VISIBLE
                    else
                        View.GONE
        }
    var showRight: Boolean? = null
        set(value) {
            field = value
            binding?.btDialogCommonEditSure?.visibility =
                    if (value == true)
                        View.VISIBLE
                    else
                        View.GONE
        }
    var title: String? = null
        set(value) {
            field = value
            binding?.tvDialogCommonEditTitle?.text = value
        }
    var hintContent: String? = null
        set(value) {
            field = value
            binding?.etDialogCommonEdit?.hint = value
        }
    var leftText: String? = null
        set(value) {
            field = value
            binding?.btDialogCommonEditCancle?.text = value
        }
    var rightText: String? = null
        set(value) {
            field = value
            binding?.btDialogCommonEditSure?.text = value
        }
    var maxNumber: Int? = null
        set(value) {
            field = value
            if (value ?: 0 > 0) {
                binding?.tvDialogCommonEditNumber?.text = String.format("0/%s", maxNumber)
                binding?.tvDialogCommonEditNumber?.visibility = View.VISIBLE
            } else {
                binding?.tvDialogCommonEditNumber?.visibility = View.GONE
            }
        }
    var onDialogBackListener: OnDialogBackListener? = null

    override fun fetchRootView(): View? {
        binding = ViewCommonEditDialogLayoutBinding.inflate(LayoutInflater.from(activity))
        return binding?.root
    }

    override fun initWidgets() {
        val width = ScreenUtils.getScreenWidth(activity) * 0.9
        setGravity(width = width.toInt())

        binding?.let {
            it.btDialogCommonEditCancle.setOnClickListener {
                onDialogBackListener?.onCancel()
                dismiss()
            }
            it.btDialogCommonEditSure.setOnClickListener {
                onDialogBackListener?.onSure("")
                dismiss()
            }

            //编辑的展示
            it.etDialogCommonEdit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable) {
                    if (maxNumber!! > 0) {
                        val length = s.length
                        if (length >= maxNumber!!) {
                            s.delete(maxNumber!!, length)
                        }
                        it.tvDialogCommonEditNumber.text = String.format("%s/%s", length, maxNumber)
                    }
                }
            })
        }
    }
}