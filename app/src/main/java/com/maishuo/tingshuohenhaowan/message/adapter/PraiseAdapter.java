package com.maishuo.tingshuohenhaowan.message.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.PraiseMessageBean;
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
public class PraiseAdapter extends CustomBaseAdapter<PraiseMessageBean, CustomBaseViewHolder> {

    public PraiseAdapter () {
        super(R.layout.praise_item);
    }

    @Override
    protected void onConvert (@NotNull CustomBaseViewHolder holder, @Nullable PraiseMessageBean bean) {
        String userName        = bean.getUserName();
        String userAvatar      = bean.getUserAvatar();
        String worksCoverImage = bean.getWorksCoverImage();
        String time            = bean.getPraiseTime();
        String praiseContent   = bean.getPraiseContent();

        holder.setBackgroundResource(R.id.ll_praise_item, R.drawable.shape_praise_read_item);

        holder.setText(R.id.iv_praise_name, TextUtils.isEmpty(userName) ? "" : userName);
        holder.setText(R.id.iv_praise_time, TextUtils.isEmpty(time) ? "" : time);
        holder.setText(R.id.iv_praise_hint, "赞了我");
        holder.setText(R.id.iv_praise_content, TextUtils.isEmpty(praiseContent) ? "" : praiseContent);

        if (!TextUtils.isEmpty(userAvatar)) {
            GlideUtils.INSTANCE.loadImage(
                    getContext(),
                    userAvatar,
                    (ImageView) holder.getView(R.id.iv_praise_head)
            );
        }

        if (!TextUtils.isEmpty(worksCoverImage)) {
            GlideUtils.INSTANCE.loadImage(
                    getContext(),
                    worksCoverImage,
                    (ImageView) holder.getView(R.id.iv_praise_icon)
            );
        }
    }
}
