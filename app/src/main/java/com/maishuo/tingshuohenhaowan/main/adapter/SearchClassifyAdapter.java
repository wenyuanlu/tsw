package com.maishuo.tingshuohenhaowan.main.adapter;

import android.text.TextUtils;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.SearchTagBean;
import com.qichuang.commonlibs.basic.CustomBaseAdapter;
import com.qichuang.commonlibs.basic.CustomBaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * author ：yh
 * date : 2021/2/7 14:04
 * description : 搜索分类Adapter
 */
public class SearchClassifyAdapter extends CustomBaseAdapter<SearchTagBean.TypesBean, CustomBaseViewHolder> {

    public SearchClassifyAdapter () {
        super(R.layout.search_classify_item);
    }

    @Override
    protected void onConvert (@NotNull CustomBaseViewHolder holder, @Nullable SearchTagBean.TypesBean item) {
        holder.setText(R.id.tv_serach_classify,
                null != item &&
                        !TextUtils.isEmpty(item.getType_name())
                        ?
                        item.getType_name() : ""
        );
    }
}