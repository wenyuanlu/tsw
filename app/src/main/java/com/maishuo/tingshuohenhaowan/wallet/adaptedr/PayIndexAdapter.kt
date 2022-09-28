package com.maishuo.tingshuohenhaowan.wallet.adaptedr

import android.view.View
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.api.response.PayIndexBean.PayListsBean
import com.qichuang.commonlibs.basic.CustomBaseAdapter
import com.qichuang.commonlibs.basic.CustomBaseViewHolder

class PayIndexAdapter : CustomBaseAdapter<PayListsBean, CustomBaseViewHolder>(R.layout.item_pay_index) {
    //1是首次,0是非首次
    var isFirstPay = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var userSelectorPosition = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onConvert(holder: CustomBaseViewHolder, item: PayListsBean?) {
        val diamondNumber = item?.diamondNumber //原来的钻石
        val freeDiamondNumber = item?.freeDiamondNumber //送的钻石
        val pay = item?.pay //需要支付的金额

        //是否首次支付
        holder.setVisible(R.id.iv_first_pay_index,
                holder.absoluteAdapterPosition == 0 && isFirstPay == 1)
        //是否选中
        holder.getView<View>(R.id.lin_pay_index_item).isSelected = userSelectorPosition == holder.absoluteAdapterPosition

        //展示
        holder.setText(R.id.tv_common_money_pay_index, diamondNumber.toString())

        if (freeDiamondNumber ?: 0 > 0) {
            holder.setText(R.id.tv_discounts_money_pay_index, " +$freeDiamondNumber")
        }

        holder.setText(R.id.tv_money_play_index, pay.toString())
    }
}