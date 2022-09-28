package com.maishuo.tingshuohenhaowan.main.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.SearchResultBean.ResultListBean;
import com.qichuang.commonlibs.basic.CustomBaseAdapter;
import com.qichuang.commonlibs.basic.CustomBaseViewHolder;
import com.qichuang.commonlibs.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * author ：yh
 * date : 2021/2/7 10:48
 * description :
 */
public class SearchResultAdapter extends CustomBaseAdapter<ResultListBean, CustomBaseViewHolder> {

    //3是用户,5是留声作品
    private final int mType;

    public SearchResultAdapter (int type) {
        super(type == 3 ? R.layout.search_user_item : R.layout.search_phonic_item);
        mType = type;
    }

    @Override
    protected void onConvert (@NotNull CustomBaseViewHolder holder, @Nullable ResultListBean item) {
        if (mType == 3) {
            String userAvatar = item.getUserAvatar();
            if (!TextUtils.isEmpty(userAvatar)) {
                GlideUtils.INSTANCE.loadImage(
                        getContext(),
                        userAvatar,
                        (ImageView) holder
                                .getView(R.id.iv_search_result_user_head)
                );
            }

            holder.setText(R.id.iv_search_result_user_name,
                    TextUtils.isEmpty(item.getUserName()) ?
                            "" : item.getUserName());
            holder.setText(R.id.iv_search_result_user_fans,
                    String.format("粉丝: %s", item.getFansNumber()));
            holder.setText(R.id.iv_search_result_user_care,
                    String.format("关注: %s", item.getUserAttentionsNumber()));
        } else {
            holder.setText(R.id.iv_search_result_content,
                    TextUtils.isEmpty(item.getDesc()) ?
                            "" : item.getDesc());
            holder.setText(R.id.iv_search_result_time,
                    TextUtils.isEmpty(item.getCreate_at_time()) ?
                            "" : item.getCreate_at_time());
            holder.setText(R.id.iv_search_result_name,
                    TextUtils.isEmpty(item.getUname()) ?
                            "" : item.getUname());
            holder.setText(R.id.iv_search_result_count,
                    String.valueOf(item.getPraise_num()));

            if (!TextUtils.isEmpty(item.getImage_path())) {
                GlideUtils.INSTANCE.loadImage(
                        getContext(),
                        item.getImage_path(),
                        (ImageView) holder.getView(R.id.iv_search_result_picture)
                );
            }

            if (!TextUtils.isEmpty(item.getAvatar())) {
                GlideUtils.INSTANCE.loadImage(
                        getContext(),
                        item.getAvatar(),
                        (ImageView) holder.getView(R.id.iv_search_result_head)
                );
            }
        }
    }
}
