package com.maishuo.tingshuohenhaowan.wallet.adaptedr;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.module.LoadMoreModule;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.WalletTopUpIndexBean;
import com.qichuang.commonlibs.basic.CustomBaseAdapter;
import com.qichuang.commonlibs.basic.CustomBaseViewHolder;
import com.qichuang.commonlibs.utils.DeviceUtil;

import java.util.List;


public class WalletTopUpMoneyAdapter extends CustomBaseAdapter<WalletTopUpIndexBean.PayListsDTO, CustomBaseViewHolder> implements LoadMoreModule {
    private final Context context;

    public WalletTopUpMoneyAdapter (List<WalletTopUpIndexBean.PayListsDTO> list, Context context) {
        super(R.layout.item_wallet_top_up_money, list);
        this.context = context;
    }

    @Override
    protected void onConvert (@NonNull CustomBaseViewHolder helper, WalletTopUpIndexBean.PayListsDTO item) {
        int                      deviceWidth = DeviceUtil.getDeviceWidth();
        int                      space       = DeviceUtil.dip2px(context, 64);
        int                      width       = (deviceWidth - space) / 3;
        int                      height      = (int) (width * 0.7);
        FrameLayout.LayoutParams mParams     = new FrameLayout.LayoutParams(width, height);
        mParams.setMargins(8, 14, 8, 0);
        helper.getView(R.id.rlItemTopUp).setLayoutParams(mParams);
        helper.getView(R.id.rlItemTopUp).setSelected(item.isSelect());
        helper.setText(R.id.tvItemMoney, String.format("￥%s", item.getPay()));
        helper.setText(R.id.tvItemDiamond, String.valueOf(item.getDiamondNumber()));
        if (item.getFreeDiamondNumber() > 0)
            helper.setText(R.id.tvItemFreeDiamond, String.format("+%s", item.getFreeDiamondNumber()));
        if (item.getPayId() == 265)
            helper.getView(R.id.iv_first_pay_index).setVisibility(View.VISIBLE);

        helper.getView(R.id.rlItemTopUp).setOnClickListener(view -> {
            for (WalletTopUpIndexBean.PayListsDTO bean : getData()) {
                bean.setSelect(false);
            }
            getData().get(getItemPosition(item)).setSelect(true);
            notifyDataSetChanged();
        });
    }
}
