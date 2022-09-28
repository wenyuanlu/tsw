package com.maishuo.tingshuohenhaowan.wallet.adaptedr;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.module.LoadMoreModule;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.WalletDetailsBean;
import com.qichuang.commonlibs.basic.CustomBaseAdapter;
import com.qichuang.commonlibs.basic.CustomBaseViewHolder;

import java.util.List;


public class WalletDetailsAdapter extends CustomBaseAdapter<WalletDetailsBean, CustomBaseViewHolder> implements LoadMoreModule {
    private boolean isTopUp;

    public WalletDetailsAdapter (List<WalletDetailsBean> list, boolean isTopUp) {
        super(R.layout.item_wallet_details, list);
        this.isTopUp = isTopUp;
    }

    @Override
    protected void onConvert (@NonNull CustomBaseViewHolder helper, WalletDetailsBean item) {
        if (isTopUp) {
            helper.setText(R.id.tvDescribe, item.getTitle());
            helper.setText(R.id.tvDate, item.getPayDate());
            helper.setText(R.id.tvMoney, item.getDiamond());
        } else {
            helper.setText(R.id.tvDescribe, item.getName());
            helper.setText(R.id.tvDate, item.getTime());
            helper.setText(R.id.tvMoney, item.getPlayCoinsOrMoney());
        }
    }
}
