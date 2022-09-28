package com.maishuo.tingshuohenhaowan.wallet.ui

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.maishuo.tingshuohenhaowan.databinding.ViewPaymentSelectorChannelLayoutBinding
import com.maishuo.tingshuohenhaowan.wallet.viewmodel.PaymentSelectorChannelViewModel
import com.qichuang.commonlibs.basic.BaseDialogFragment

/**
 * author : xpSun
 * date : 10/12/21
 * description :
 */
class PaymentSelectorChannelDialog : BaseDialogFragment {

    constructor()

    constructor(activity: AppCompatActivity?) : super(activity)

    private var binding: ViewPaymentSelectorChannelLayoutBinding? = null
    private var viewModel: PaymentSelectorChannelViewModel? = null

    var position: Int? = null
    var money: Int? = null
    var payId: Int? = null
    var type: Int? = null

    override fun fetchRootView(): View? {
        binding = ViewPaymentSelectorChannelLayoutBinding.inflate(LayoutInflater.from(activity))
        viewModel = PaymentSelectorChannelViewModel(activity)
        binding?.viewmodel = viewModel
        return binding?.root
    }

    override fun initWidgets() {
        viewModel?.let {
            it.binding = binding
            it.payId = payId
            it.money = money?.toString()
            it.position = position
            it.paymentResponseLiveData.observe(this) {
                dismiss()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initWidgetSize(Gravity.BOTTOM)
    }
}