package com.maishuo.tingshuohenhaowan.wallet.ui

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.databinding.ViewPaySelectorMoneyLayoutBinding
import com.maishuo.tingshuohenhaowan.wallet.adaptedr.PayIndexAdapter
import com.maishuo.tingshuohenhaowan.wallet.viewmodel.PaySelectorMoneyViewModel
import com.maishuo.tingshuohenhaowan.widget.CommonItemDecoration
import com.qichuang.commonlibs.basic.BaseDialogFragment

/**
 * author : xpSun
 * date : 10/12/21
 * description :
 */
class PaySelectorMoneyDialog : BaseDialogFragment {

    constructor()

    constructor(activity: AppCompatActivity?) : super(activity)

    private var binding: ViewPaySelectorMoneyLayoutBinding? = null
    private var viewModel: PaySelectorMoneyViewModel? = null
    private var adapter: PayIndexAdapter? = null

    //类型 1直播,2私聊,3密友,4留声,5听书,6演说
    var type: Int? = null

    override fun fetchRootView(): View? {
        binding = ViewPaySelectorMoneyLayoutBinding.inflate(LayoutInflater.from(activity))
        viewModel = PaySelectorMoneyViewModel(appCompatActivity)
        binding?.viewmodel = viewModel
        return binding?.root
    }

    override fun initWidgets() {
        adapter = PayIndexAdapter()
        binding?.giftBuyPayRecycler?.layoutManager = GridLayoutManager(activity, 3)
        val padding = resources.getDimension(R.dimen.dp_8).toInt()
        val commonItemDecoration = CommonItemDecoration(padding, padding)
        binding?.giftBuyPayRecycler?.addItemDecoration(commonItemDecoration)
        binding?.giftBuyPayRecycler?.adapter = adapter

        viewModel?.let {
            it.fetchPaymentIndexLiveData.observe(this) { response ->
                adapter?.isFirstPay = response?.userIsFirstPay ?: 0
                adapter?.setNewInstance(response?.payLists)
                binding?.giftBuyPayMoneyNumber?.text = response?.userDiamond.toString()
            }
            it.fetchPaymentIndex()
            it.sendPayMoneyLiveData.observe(this) {
                dismiss()
            }
        }
    }

    override fun initWidgetsEvent() {
        adapter?.setOnItemClickListener { _, _, position ->
            adapter?.userSelectorPosition = position
            viewModel?.userPaymentSelectorPosition = position
        }
    }

    override fun onResume() {
        super.onResume()
        initWidgetSize(Gravity.BOTTOM)
    }
}