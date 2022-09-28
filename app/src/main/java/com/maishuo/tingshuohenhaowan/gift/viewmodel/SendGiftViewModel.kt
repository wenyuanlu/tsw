package com.maishuo.tingshuohenhaowan.gift.viewmodel

import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.maishuo.tingshuohenhaowan.api.param.GetGiftApiParam
import com.maishuo.tingshuohenhaowan.api.param.UnifiedSendGiftParam
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService
import com.maishuo.tingshuohenhaowan.api.response.GetGfitBean
import com.maishuo.tingshuohenhaowan.api.response.GiftBuyBean
import com.maishuo.tingshuohenhaowan.wallet.ui.PaySelectorMoneyDialog
import com.qichuang.commonlibs.basic.BaseViewModel
import com.qichuang.commonlibs.utils.ToastUtil
import com.qichuang.retrofit.CommonObserver

/**
 * author : xpSun
 * date : 10/12/21
 * description :
 */
class SendGiftViewModel constructor(val activity: AppCompatActivity?) : BaseViewModel() {
    companion object {
        private const val GIFT_BUY_ERROR_CODE: String = "1014"
    }

    private var gifts: MutableList<GetGfitBean.GiftsBean>? = null

    var fetchGiftDataLiveData: MutableLiveData<GetGfitBean?> = MutableLiveData()
    var fetchGiftBuyLiveData: MutableLiveData<GiftBuyBean?> = MutableLiveData()
    var sendGiftErrorLiveData: MutableLiveData<String?> = MutableLiveData()
    var dismissDialogLiveData: MutableLiveData<String?> = MutableLiveData()

    var type: Int? = null

    //发送给谁
    var mToUserId: String? = null

    //留声Id
    var mPhonicId: String? = null

    var userSelectorSendGiftBean: GetGfitBean.GiftsBean? = null

    fun fetchGiftData() {
        val getGiftApiParam = GetGiftApiParam()
        getGiftApiParam.liveType = type
        ApiService.instance.getGiftApi(getGiftApiParam)
                .subscribe(object : CommonObserver<GetGfitBean>(true) {
                    override fun onResponseSuccess(response: GetGfitBean?) {
                        fetchGiftDataLiveData.postValue(response)
                        gifts = response?.gifts
                    }
                })
    }

    fun userClickRecharge() {
        dismissDialogLiveData.postValue(null)
        val dialog = PaySelectorMoneyDialog(activity)
        dialog.type = type
        dialog.showDialog()
    }

    fun sendGift() {
        if (gifts.isNullOrEmpty()) {
            return
        }

        for (item in gifts!!.iterator()) {
            if (item.isSelect) {
                userSelectorSendGiftBean = item
                break
            }
        }

        val param = UnifiedSendGiftParam()
        param.gift_id = userSelectorSendGiftBean?.id
        param.gift_num = 1
        param.gift_type = userSelectorSendGiftBean?.type
        param.type = type
        param.to_user = mToUserId
        param.type_obj_id_1 = mPhonicId
        ApiService.instance.unifiedSendGift(param)
                .subscribe(object : CommonObserver<GiftBuyBean>(true) {
                    override fun onResponseSuccess(response: GiftBuyBean?) {
                        fetchGiftBuyLiveData.postValue(response)
                    }

                    override fun onResponseError(message: String?, e: Throwable?, code: String?) {
                        if (!TextUtils.isEmpty(code) && GIFT_BUY_ERROR_CODE == code) {
                            sendGiftErrorLiveData.postValue(message)
                        } else {
                            ToastUtil.showToast(message)
                        }
                    }
                })
    }
}