package com.maishuo.tingshuohenhaowan.main.dialog

import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.databinding.ViewSendBarrageDialogLayoutBinding
import com.maishuo.tingshuohenhaowan.main.helper.SoftKeyboardHelper
import com.maishuo.tingshuohenhaowan.main.helper.SoftKeyboardHelper.SoftKeyboardStateListener
import com.qichuang.commonlibs.basic.BaseDialog
import com.qichuang.commonlibs.utils.PreferencesUtils

/**
 * 发送弹幕
 */
class SendBarrageDialog constructor(activity: AppCompatActivity?) :
        BaseDialog(activity, R.style.InputTextDialog), SoftKeyboardStateListener {

    private var binding: ViewSendBarrageDialogLayoutBinding? = null
    private var softKeyboardStateHelper: SoftKeyboardHelper? = null

    var onInputValueListener: ((text: String?, giftId: Int, giftNum: Int) -> Unit?)? = null

    override fun fetchRootView(): View? {
        binding = ViewSendBarrageDialogLayoutBinding.inflate(LayoutInflater.from(activity))
        return binding?.root
    }

    override fun initWidgets() {
        setGravity(Gravity.BOTTOM)

        setCanceledOnTouchOutside(true)

        binding?.sendBarrageInputValue?.requestFocus()
        binding?.sendBarrageInputValue?.setOnEditorActionListener { _, _, _ ->
            sendBarrage()
            true
        }

        binding?.sendBarrageValue?.setOnClickListener {
            sendBarrage()
        }

        binding?.sendBarrageRootView?.setOnClickListener {
            dismiss()
        }

        //添加键盘监听
        softKeyboardStateHelper = SoftKeyboardHelper(binding?.sendBarrageInputValue)
        softKeyboardStateHelper?.addSoftKeyboardStateListener(this)
    }

    //发送弹幕
    private fun sendBarrage() {
        if (TextUtils.isEmpty(binding?.sendBarrageInputValue?.text)) {
            return
        }

        PreferencesUtils.putBoolean("InputDialogClickSend", true) //如果点击了发送，首页强制继续弹幕
        val text = binding?.sendBarrageInputValue?.text.toString().trim { it <= ' ' }

        if (!TextUtils.isEmpty(text)) {
            onInputValueListener?.invoke(text, -100, 0)
            dismiss()
        }
    }

    override fun dismiss() {
        if (isShowing()) {
            if (softKeyboardStateHelper != null) {
                softKeyboardStateHelper?.removeSoftKeyboardStateListener(this)
            }
            super.dismiss()
        }
    }

    override fun onSoftKeyboardOpened() {

    }

    override fun onSoftKeyboardClosed() {
        if (isShowing()) {
            dismiss()
        }
    }
}