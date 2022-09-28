package com.maishuo.tingshuohenhaowan.wallet.adaptedr;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.module.LoadMoreModule;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.H5WithdrawRecordBean;
import com.qichuang.commonlibs.basic.CustomBaseAdapter;
import com.qichuang.commonlibs.basic.CustomBaseViewHolder;

import java.util.List;


public class H5WithdrawRecordAdapter extends CustomBaseAdapter<H5WithdrawRecordBean, CustomBaseViewHolder> implements LoadMoreModule {

    public H5WithdrawRecordAdapter (List<H5WithdrawRecordBean> list) {
        super(R.layout.item_h5_withdraw_record, list);
    }

    @Override
    protected void onConvert (@NonNull CustomBaseViewHolder helper, H5WithdrawRecordBean item) {
        helper.setText(R.id.tvDescribe, item.getDesc());
        helper.setText(R.id.tvDate, item.getCreated_at());
        helper.setText(R.id.tvMoney, item.getMoney());
        helper.setText(R.id.tvStatus, item.getStatus_cn());
    }

}
