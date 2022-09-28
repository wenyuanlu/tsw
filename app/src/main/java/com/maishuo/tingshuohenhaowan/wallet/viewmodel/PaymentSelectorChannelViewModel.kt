package com.maishuo.tingshuohenhaowan.wallet.viewmodel

import android.app.Activity
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.api.param.PayApiParam
import com.maishuo.tingshuohenhaowan.api.param.PayResultApiParam
import com.maishuo.tingshuohenhaowan.api.response.EmptyBean
import com.maishuo.tingshuohenhaowan.api.response.PayMoneyEnum
import com.maishuo.tingshuohenhaowan.api.response.PaymentInfoBean
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService
import com.maishuo.tingshuohenhaowan.bean.AliPayBean
import com.maishuo.tingshuohenhaowan.common.Constant
import com.maishuo.tingshuohenhaowan.databinding.ViewPaymentSelectorChannelLayoutBinding
import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils
import com.maishuo.tingshuohenhaowan.wallet.pay.AliPayBuilder
import com.maishuo.tingshuohenhaowan.wallet.pay.PayCallback
import com.maishuo.tingshuohenhaowan.wallet.pay.WxPayBean.PayInfoBean
import com.maishuo.tingshuohenhaowan.wallet.pay.WxPayBuilder
import com.maishuo.umeng.ConstantEventId
import com.qichuang.commonlibs.basic.BaseViewModel
import com.qichuang.commonlibs.utils.GlideUtils
import com.qichuang.commonlibs.utils.ToastUtil
import com.qichuang.retrofit.CommonObserver
import java.math.BigDecimal

/**
 * author : xpSun
 * date : 10/12/21
 * description :
 */
class PaymentSelectorChannelViewModel constructor(val activity: Activity?) : BaseViewModel() {

    private var paymentType: Int? = null
    private var payOrderId: String? = null

    val paymentResponseLiveData: MutableLiveData<String?> = MutableLiveData()

    var binding: ViewPaymentSelectorChannelLayoutBinding? = null
    var position: Int? = null
    var payId: Int? = null
    var money: String? = null

    fun onAliPayListener() {
        if (null == activity) {
            return
        }

        paymentType = 1

        binding?.let {
            GlideUtils.loadImage(
                    activity,
                    R.mipmap.home_pay_choose_click,
                    it.paymentSelectorChannelAliPay
            )
            GlideUtils.loadImage(
                    activity,
                    R.mipmap.home_pay_choose_default,
                    it.paymentSelectorChannelWechatPay
            )
        }
    }

    fun onWechatPayListener() {
        if (null == activity) {
            return
        }

        paymentType = 2

        binding?.let {
            GlideUtils.loadImage(
                    activity,
                    R.mipmap.home_pay_choose_click,
                    it.paymentSelectorChannelWechatPay
            )
            GlideUtils.loadImage(
                    activity,
                    R.mipmap.home_pay_choose_default,
                    it.paymentSelectorChannelAliPay
            )
        }
    }

    fun confirmPayment() {
        try {
            if (1 == paymentType ?: 1) {
                paymentToAli(money)
                TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_Recharge_Alipay)
            } else {
                if (TextUtils.isEmpty(money)) {
                    return
                }

                val value = BigDecimal(money).multiply(BigDecimal("100"))
                paymentToWechat(value.toString())
                TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_Recharge_WeChat)
            }

            val value = PayMoneyEnum.valueOf("Recharge_$position").value
            TrackingAgentUtils.onEvent(activity, value)
            TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_Recharge_payment)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun paymentToAli(money: String?) {
        val aliPayApiParam = PayApiParam()
        aliPayApiParam.money = money
        if (null != payId && payId != -1) {
            aliPayApiParam.payId = payId
        }
        ApiService.instance.aliPayApi(aliPayApiParam)
                .subscribe(object : CommonObserver<AliPayBean>() {
                    override fun onResponseSuccess(response: AliPayBean?) {
                        if (response != null) {
                            payOrderId = response.orderId
                            aliPay(response.order)
                        }
                    }
                })
    }

    private fun paymentToWechat(money: String?) {
        val wxPayApiParam = PayApiParam()
        wxPayApiParam.money = (money ?: 0).toString()
        if (null != payId && payId != -1) {
            wxPayApiParam.payId = payId
        }
        ApiService.instance.wxPayApi(wxPayApiParam)
                .subscribe(object : CommonObserver<PaymentInfoBean>() {
                    override fun onResponseSuccess(response: PaymentInfoBean?) {
                        if (response != null) {
                            payOrderId = response.orderId
                            val bean = PayInfoBean()
                            bean.appid = response.appid
                            bean.partnerid = response.partnerid
                            bean.prepayid = response.prepayid
                            bean.packageX = "Sign=WXPay"
                            bean.sign = response.sign
                            bean.noncestr = response.noncestr
                            bean.timestamp = response.timestamp
                            wechatPay(bean)
                        }
                    }
                })
    }

    //支付宝支付
    private fun aliPay(orderParams: String) {
        AliPayBuilder.getInstance(activity, object : PayCallback {
            override fun onSuccess(message: String) {
                ToastUtil.showToast(message)
                sendPaySuccess(1) //发送支付成功的回调
            }

            override fun onFailed(message: String) {
                ToastUtil.showToast(message)
            }
        }).pay(orderParams)
    }

    //微信支付
    private fun wechatPay(bean: PayInfoBean) {
        WxPayBuilder.getInstance(Constant.WX_APP_ID, object : PayCallback {
            override fun onSuccess(message: String) {
                ToastUtil.showToast(message)
                sendPaySuccess(2) //发送支付成功的回调
            }

            override fun onFailed(message: String) {
                ToastUtil.showToast(message)
            }
        }).pay(bean)
    }

    /**
     * 发送支付完成的请求
     *
     * @param type type=1 是支付宝 type=2 是微信
     */
    private fun sendPaySuccess(type: Int) {
        val payResultApiParam = PayResultApiParam()
        payResultApiParam.orderId = payOrderId
        payResultApiParam.type = type.toString()
        payResultApiParam.payId = payId.toString()
        payResultApiParam.goodsId = (-1).toString()
        ApiService.instance.payResultApi(payResultApiParam)
                .subscribe(object : CommonObserver<EmptyBean>() {
                    override fun onResponseSuccess(response: EmptyBean?) {
                        //热云钱包充值
                        if (type == 1) {
                            if (type == 3) {
                                TrackingAgentUtils.setPayment(
                                        String.format("Chat:%s", payOrderId),
                                        "alipay",
                                        "CNY",
                                        money?.toFloat() ?: 0f)
                            } else if (type == 4) {
                                TrackingAgentUtils.setPayment(
                                        String.format("Main:%s", payOrderId),
                                        "alipay",
                                        "CNY",
                                        money?.toFloat() ?: 0f)
                            }
                        } else {
                            if (type == 3) {
                                TrackingAgentUtils.setPayment(
                                        String.format("Chat:%s", payOrderId),
                                        "weixinpay",
                                        "CNY",
                                        money?.toFloat() ?: 0f)
                            } else if (type == 4) {
                                TrackingAgentUtils.setPayment(
                                        String.format("Main:%s", payOrderId),
                                        "weixinpay",
                                        "CNY",
                                        money?.toFloat() ?: 0f)
                            }
                        }
                        paymentResponseLiveData.postValue(null)
                    }

                    override fun onResponseError(message: String?, e: Throwable?, code: String?) {
                        super.onResponseError(message, e, code)
                        paymentResponseLiveData.postValue(null)
                    }
                })
    }
}