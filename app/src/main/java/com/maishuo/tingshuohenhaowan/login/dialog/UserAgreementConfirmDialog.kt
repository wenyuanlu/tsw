package com.maishuo.tingshuohenhaowan.login.dialog

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.maishuo.tingshuohenhaowan.databinding.ViewAgreementConfirmDialogLayoutBinding
import com.maishuo.tingshuohenhaowan.ui.activity.WebViewActivity
import com.maishuo.tingshuohenhaowan.utils.CustomPreferencesUtils
import com.qichuang.commonlibs.basic.BaseDialogFragment
import com.qichuang.commonlibs.utils.ScreenUtils


/**
 * author : xpSun
 * date : 2021/4/12
 * description : 用户隐私协议弹窗
 */
class UserAgreementConfirmDialog : BaseDialogFragment {

    constructor() : super()

    constructor(appCompatActivity: AppCompatActivity) : super(appCompatActivity)

    private var binding: ViewAgreementConfirmDialogLayoutBinding? = null

    var onDialogSelectorClickListener: (() -> Unit)? = null

    companion object {
        private const val USER_SERVICE_AGREEMENT_URL =
            "http://live.tingshuowan.com/listen/agreement?type=1"
        private const val PRIVACY_SERVICES_AGREEMENT_URL =
            "http://live.tingshuowan.com/listen/agreement?type=2"
    }

    override fun fetchRootView(): View? {
        binding = ViewAgreementConfirmDialogLayoutBinding.inflate(LayoutInflater.from(context))
        return binding?.root
    }

    override fun initWidgets() {
        binding?.agreementDialogContent?.text = getClickableSpan()
        binding?.agreementDialogContent?.movementMethod = LinkMovementMethod.getInstance();

        isCancelable = false
        dialog?.setCanceledOnTouchOutside(false)
    }

    private fun getClickableSpan(): SpannableString {
        val spannableString = SpannableString(
            if (TextUtils.isEmpty(binding?.agreementDialogContent?.text))
                ""
            else
                binding?.agreementDialogContent?.text.toString()
        )

        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                WebViewActivity.to(context, USER_SERVICE_AGREEMENT_URL, "服务协议")
            }
        }, 3, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(NoUnderlineSpan(), 3, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                WebViewActivity.to(context, PRIVACY_SERVICES_AGREEMENT_URL, "隐私政策")
            }
        }, 17, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(NoUnderlineSpan(), 17, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    override fun initWidgetsEvent() {
        binding?.agreementDialogCancel?.setOnClickListener {
            dismiss()
            appCompatActivity?.finishAffinity()
        }

        binding?.agreementDialogOk?.setOnClickListener {
            CustomPreferencesUtils.saveAgreement(this::class.java.name)
            onDialogSelectorClickListener?.invoke()
            dismiss()

        }
    }

    override fun onResume() {
        super.onResume()
        val width = ScreenUtils.getScreenWidth(context) * 0.85
        val height = ScreenUtils.getRealyScreenHeight(context) * 0.6
        initWidgetSize(Gravity.CENTER, width.toInt(), height.toInt())
    }

}