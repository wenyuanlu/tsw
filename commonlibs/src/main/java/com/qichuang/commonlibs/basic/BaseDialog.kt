package com.qichuang.commonlibs.basic

import android.app.Dialog
import android.content.DialogInterface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import com.qichuang.commonlibs.R

/**
 * author : xpSun
 * date : 9/22/21
 * description :
 */
abstract class BaseDialog @JvmOverloads
constructor(val activity: AppCompatActivity?,
            @StyleRes val themeResId: Int? = null) {

    abstract fun fetchRootView(): View?

    abstract fun initWidgets()

    var dialog: Dialog? = null
    var onDismissListener: DialogInterface.OnDismissListener? = null

    init {
        try {
            if (null != activity) {
                dialog = Dialog(activity, themeResId ?: R.style.SheetDialogStyle)

                val view = fetchRootView()
                if (null != view) {
                    dialog?.setContentView(view)
                    dialog?.setOnDismissListener { dialogIt ->
                        onDialogDismiss()
                        onDismissListener?.onDismiss(dialogIt)
                    }

                    setGravity(Gravity.CENTER)
                    initWidgets()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmOverloads
    fun setGravity(gravity: Int = Gravity.CENTER,
                   width: Int = ViewGroup.LayoutParams.MATCH_PARENT,
                   height: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
                   @StyleRes resId: Int? = null) {
        val window = dialog?.window

        if (null != resId) {
            window?.setWindowAnimations(resId)
        }
        val attributes = window?.attributes
        attributes?.gravity = gravity
        window?.attributes = attributes
        dialog?.window?.setLayout(width, height)
    }

    open fun setCanceledOnTouchOutside(cancel: Boolean) {
        dialog?.setCanceledOnTouchOutside(cancel)
    }

    open fun setCancelable(cancel: Boolean) {
        dialog?.setCancelable(cancel)
    }

    open fun showDialog() {
        dialog?.show()
    }

    open fun dismiss() {
        dialog?.dismiss()
    }

    open fun isShowing(): Boolean {
        return dialog?.isShowing ?: false
    }

    open fun onDialogDismiss() {

    }

}