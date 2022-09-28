package com.maishuo.tingshuohenhaowan.message.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.BarrageMessageBean;
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
public class BarrageAdapter extends CustomBaseAdapter<BarrageMessageBean, CustomBaseViewHolder> {

    public BarrageAdapter () {
        super(R.layout.praise_item);
    }

    @Override
    protected void onConvert (@NotNull CustomBaseViewHolder holder, @Nullable BarrageMessageBean item) {
        String userName      = item.getUserName();
        String userAvatar    = item.getUserAvatat();
        String image_path    = item.getImage_path();
        String content       = item.getContent();
        String time          = item.getTime();
        String praiseContent = item.getVoiceTitle();

        holder.setBackgroundResource(R.id.ll_praise_item, R.drawable.shape_praise_read_item);

        holder.setText(R.id.iv_praise_name, TextUtils.isEmpty(userName) ? "" : userName);
        holder.setText(R.id.iv_praise_hint, TextUtils.isEmpty(content) ? "" : content);
        holder.setText(R.id.iv_praise_time, TextUtils.isEmpty(time) ? "" : time);
        holder.setText(R.id.iv_praise_content, TextUtils.isEmpty(praiseContent) ? "" : praiseContent);

        if (!TextUtils.isEmpty(userAvatar)) {
            GlideUtils.INSTANCE.loadImage(
                    getContext(),
                    userAvatar,
                    (ImageView) holder.getView(R.id.iv_praise_head)
            );
        }

        if (!TextUtils.isEmpty(image_path)) {
            GlideUtils.INSTANCE.loadImage(
                    getContext(),
                    image_path,
                    (ImageView) holder.getView(R.id.iv_praise_icon)
            );
        }
    }
}
