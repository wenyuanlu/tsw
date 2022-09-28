package com.maishuo.tingshuohenhaowan.setting.ui

import android.text.TextUtils
import android.view.View
import com.maishuo.tingshuohenhaowan.api.param.FeedbackParam
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity
import com.maishuo.tingshuohenhaowan.databinding.ActivitySuggestLayoutBinding
import com.qichuang.commonlibs.utils.ToastUtil
import com.qichuang.retrofit.CommonObserver

/**
 * author : xpSun
 * date : 9/14/21
 * description : 意见反馈页面
 */
class SuggestActivity : CustomBaseActivity<ActivitySuggestLayoutBinding>() {

    private var mSuggestContent = ""
    private var mContact = ""

    override fun initView() {
        setTitle("意见反馈")

        vb?.let {
            it.btSuggestSubmit.setOnClickListener { _ ->
                if (TextUtils.isEmpty(it.etSuggestContent.text)) {
                    ToastUtil.showToast("内容不能为空！")
                    return@setOnClickListener
                }

                mSuggestContent = it.etSuggestContent.text.toString().trim()
                mContact = if (TextUtils.isEmpty(it.etSuggestContact.text)) {
                    ""
                } else {
                    it.etSuggestContact.text.toString().trim()
                }

                if (TextUtils.isEmpty(mSuggestContent)) {
                    ToastUtil.showToast("内容不能为空！")
                } else {
                    submitSuggest() //意见提交
                }
            }
        }
    }

    override fun initData() {}

    /**
     * 意见提交
     */
    private fun submitSuggest() {
        val param = FeedbackParam()
        param.content = mSuggestContent
        param.status = mContact
        ApiService.instance
                .feedback(param)
                .subscribe(object : CommonObserver<Any>() {
                    override fun onResponseSuccess(response: Any?) {
                        vb!!.rlSuggestSuccess.visibility = View.VISIBLE
                        vb!!.rlSuggestEdit.visibility = View.GONE
                    }
                })
    }
}