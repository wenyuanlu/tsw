package com.maishuo.tingshuohenhaowan.wallet.adaptedr;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.module.LoadMoreModule;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.H5MoneyBean;
import com.qichuang.commonlibs.basic.CustomBaseAdapter;
import com.qichuang.commonlibs.basic.CustomBaseViewHolder;

import java.util.List;


public class H5WithdrawMoney2Adapter extends CustomBaseAdapter<H5MoneyBean, CustomBaseViewHolder> implements LoadMoreModule {

    public H5WithdrawMoney2Adapter (List<H5MoneyBean> list) {
        super(R.layout.item_h5_withdraw_money, list);
    }

    @Override
    protected void onConvert (@NonNull CustomBaseViewHolder helper, H5MoneyBean item) {
        helper.getView(R.id.rlItemMoney).setSelected(item.isSelect());
        helper.setTextColor(R.id.tvItemMoney, item.isSelect() ? Color.parseColor("#FFFFFF") : Color.parseColor("#333333"));
        helper.setText(R.id.tvItemMoney, item.getView());

        helper.getView(R.id.rlItemMoney).setOnClickListener(view -> {
            for (H5MoneyBean bean : getData()) {
                bean.setSelect(false);
            }
            getData().get(getItemPosition(item)).setSelect(true);
            notifyDataSetChanged();
        });
    }
}
