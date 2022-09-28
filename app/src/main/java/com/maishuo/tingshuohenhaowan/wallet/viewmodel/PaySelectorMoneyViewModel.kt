package com.maishuo.tingshuohenhaowan.wallet.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService
import com.maishuo.tingshuohenhaowan.api.response.PayIndexBean
import com.maishuo.tingshuohenhaowan.utils.DialogUtils
import com.maishuo.tingshuohenhaowan.wallet.ui.PaymentSelectorChannelDialog
import com.qichuang.commonlibs.basic.BaseViewModel
import com.qichuang.retrofit.CommonObserver

/**
 * author : xpSun
 * date : 10/12/21
 * description :
 */
class PaySelectorMoneyViewModel constructor(val activity: AppCompatActivity?) : BaseViewModel() {
    val fetchPaymentIndexLiveData: MutableLiveData<PayIndexBean?> = MutableLiveData()
    val sendPayMoneyLiveData: MutableLiveData<String?> = MutableLiveData()

    var userPaymentSelectorPosition: Int? = null

    private var rechargeTitle: String? = null
    private var mVipRule: String? = null
    private var apiResponse: PayIndexBean? = null

    fun fetchPaymentIndex() {
        ApiService.instance.payIndexApi()
                .subscribe(object : CommonObserver<PayIndexBean>() {
                    override fun onResponseSuccess(response: PayIndexBean?) {
                        fetchPaymentIndexLiveData.postValue(response)

                        apiResponse = response
                        rechargeTitle = response?.rechargeTitle
                        mVipRule = response?.vipRule
                    }
                })
    }

    fun showPayTip() {
        DialogUtils.showCommonDialog(
                activity,
                rechargeTitle,
                mVipRule,
                "取消",
                "确定",
                false,
                true,
                true,
                null)
    }

    fun sendPayMoney() {
        try {
            sendPayMoneyLiveData.postValue(null)

            apiResponse?.let {
                var item: PayIndexBean.PayListsBean? = null
                if (!it.payLists.isNullOrEmpty() &&
                        userPaymentSelectorPosition ?: 0 < it.payLists.size) {
                    item = it.payLists[userPaymentSelectorPosition ?: 0]
                }

                if (null != activity) {
                    val dialog = PaymentSelectorChannelDialog(activity)
                    dialog.position = userPaymentSelectorPosition
                    dialog.money = item?.pay
                    dialog.payId = item?.payId
                    dialog.showDialog()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}