package com.qichuang.retrofit

import android.text.TextUtils
import com.qichuang.bean.BasicResponse
import com.qichuang.commonlibs.utils.LogUtils
import com.qichuang.dialog.ProgressDialogUtil
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * author : xpSun
 * date : 6/10/21
 *description :
 */
abstract class CommonBasicObserver<T> : Observer<BasicResponse<T>> {

    override fun onSubscribe(d: Disposable) {

    }

    override fun onComplete() {

    }

    override fun onNext(data: BasicResponse<T>) {
        when (data.code) {
            CommonResCodeEnum.RES_CODE_200.recCode -> {
                onResponseSuccess(data.data)
            }
            else -> {
                ProgressDialogUtil.instance.dismiss()
                if (!TextUtils.isEmpty(data.msg ?: "")) {
                    onResponseError(data.msg ?: "", null, data.code)
                }
            }
        }
    }

    override fun onError(e: Throwable) {
        LogUtils.LOGE("onError", e.toString())
    }

    /**
     * 可以提供请求错误到界面的error
     */
    open fun onResponseError(message: String?, e: Throwable? = null, code: String? = null) {

    }

    /**
     * 接口数据回调
     */
    abstract fun onResponseSuccess(response: T?)

}