package com.maishuo.tingshuohenhaowan.gift.view

import android.text.TextUtils
import android.view.View
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.api.response.GetGfitBean
import com.qichuang.commonlibs.basic.CustomBaseAdapter
import com.qichuang.commonlibs.basic.CustomBaseViewHolder
import com.qichuang.commonlibs.utils.GlideUtils.loadImage

/**
 * description : gift弹窗中子页面的adapter
 */
class GiftItemAdapter : CustomBaseAdapter<GetGfitBean.GiftsBean?, CustomBaseViewHolder>(R.layout.view_dialog_gift_item_show_layout) {

    override fun onConvert(holder: CustomBaseViewHolder, item: GetGfitBean.GiftsBean?) {
        if (null == item) {
            return
        }

        if (!TextUtils.isEmpty(item.img)) {
            loadImage(
                    context,
                    item.img,
                    holder.getView(R.id.iv_gift_item_icon)
            )
        }
        holder.setText(R.id.iv_gift_item_name, if (TextUtils.isEmpty(item.name)) "" else item.name)
        holder.setText(R.id.iv_gift_item_money, item.unit_price.toString())
        //设置选中状态
        holder.getView<View>(R.id.ll_gift_item).isSelected = item.isSelect
    }
}