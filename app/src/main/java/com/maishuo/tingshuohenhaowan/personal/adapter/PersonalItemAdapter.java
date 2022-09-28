package com.maishuo.tingshuohenhaowan.personal.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.MyPhonicListBean;
import com.maishuo.tingshuohenhaowan.main.event.AttentionEvent;
import com.maishuo.tingshuohenhaowan.main.event.PraiseEvent;
import com.qichuang.commonlibs.basic.CustomBaseAdapter;
import com.qichuang.commonlibs.basic.CustomBaseViewHolder;
import com.qichuang.commonlibs.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * author ：yh
 * date : 2021/2/5 17:48
 * description :
 */
public class PersonalItemAdapter extends CustomBaseAdapter<MyPhonicListBean, CustomBaseViewHolder> {
    private int mType = 1;//1是发布-展示播放量 ,2是喜欢-展示喜欢数目

    public PersonalItemAdapter (int type) {
        super(R.layout.personal_item_item);
        mType = type;
    }

    @Override
    protected void onConvert (@NotNull CustomBaseViewHolder holder, @Nullable MyPhonicListBean item) {
        if (mType == 1) {
            holder.setImageResource(R.id.iv_personal_type, R.mipmap.me_works_icon_play);
            holder.setText(R.id.tv_personal_count,
                    TextUtils.isEmpty(item.getPlayed_num_str()) ? "" : item.getPlayed_num_str());
        } else if (mType == 2) {
            holder.setImageResource(R.id.iv_personal_type, R.mipmap.search_list_icon_like);
            holder.setText(R.id.tv_personal_count,
                    TextUtils.isEmpty(item.getPraise_num_str()) ? "" : item.getPraise_num_str());
        }

        String image_path = item.getImage_path();
        if (!TextUtils.isEmpty(image_path)) {
            GlideUtils.INSTANCE.loadImage(
                    getContext(),
                    image_path,
                    (ImageView) holder.getView(R.id.iv_personal_icon)
            );
        }
    }

    /**
     * 在留声页关注后改变数据状态
     */
    public void newDataAfterAttention (AttentionEvent event) {
        if (getData() != null && !getData().isEmpty()) {
            for (MyPhonicListBean bean : getData()) {
                if (TextUtils.equals(bean.getUser_id(), event.userId)) {
                    //后端字段不统一，首页0-未关注，1-已关注，其它地方1 - 未关注 2 - 互粉 3 - 已关注
                    bean.setIs_attention(event.statues == 1 ? 0 : 1);
                }
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 在留声页点赞后改变数据状态
     */
    public void newDataAfterPraise (PraiseEvent event) {
        if (getData() != null && !getData().isEmpty()) {
            for (MyPhonicListBean bean : getData()) {
                if (bean.getId() == event.id) {
                    bean.setIs_praise(event.praiseStatus);
                    bean.setPraise_num(event.praiseNumber);
                    break;
                }
            }
            notifyDataSetChanged();
        }
    }
}
