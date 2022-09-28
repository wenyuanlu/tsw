package com.maishuo.tingshuohenhaowan.main.adapter;

import com.maishuo.tingshuohenhaowan.R;
import com.qichuang.commonlibs.basic.CustomBaseAdapter;
import com.qichuang.commonlibs.basic.CustomBaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * author ：yh
 * date : 2021/2/7 14:04
 * description : 搜索记录Adapter
 */
public class SearchHistoryAdapter extends CustomBaseAdapter<String, CustomBaseViewHolder> {

    public SearchHistoryAdapter () {
        super(R.layout.search_history_item);
    }

    @Override
    protected void onConvert (@NotNull CustomBaseViewHolder holder, @Nullable String item) {
        holder.setText(R.id.tv_serach_history, item);
    }
}