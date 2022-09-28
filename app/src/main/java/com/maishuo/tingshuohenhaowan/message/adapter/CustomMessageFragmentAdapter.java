package com.maishuo.tingshuohenhaowan.message.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.MessageListBean;
import com.qichuang.commonlibs.basic.CustomBaseAdapter;
import com.qichuang.commonlibs.basic.CustomBaseViewHolder;
import com.qichuang.commonlibs.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * author ：yh
 * date : 2021/1/15 13:39
 * description :首页-消息列表的展示adapter
 */
public class CustomMessageFragmentAdapter extends CustomBaseAdapter<MessageListBean.FriendBean, CustomBaseViewHolder> {

    public CustomMessageFragmentAdapter () {
        super(R.layout.fragment_my_message_item);
    }

    @Override
    protected void onConvert (@NotNull CustomBaseViewHolder holder, @Nullable MessageListBean.FriendBean item) {
        String userAvatar      = item.getUserAvatar();
        String userName        = item.getUserName();
        String onlineTime      = item.getOnlineTime();
        String time            = item.getTime();
        int    unReadNum       = item.getUnReadNum();
        String lastReadMessage = item.getLastReadMessage();//最后一条消息 本地拼接
        String type            = item.getType();//1文本，2，image  3音频

        if (!TextUtils.isEmpty(userAvatar)) {
            GlideUtils.INSTANCE.initImageForHead(
                    getContext(),
                    userAvatar,
                    R.mipmap.home_head_pic_default,
                    (ImageView) holder.getView(R.id.iv_message_head)
            );
        }

        holder.setText(R.id.tv_meaasge_name,
                TextUtils.isEmpty(userName) ? "" : userName);
        holder.setText(R.id.tv_message_time,
                TextUtils.isEmpty(time) ? "" : time);
        if (type.equals("1")) {
            holder.setText(R.id.tv_meaasge_last,
                    String.format("%s %s", onlineTime, lastReadMessage));
        } else if (type.equals("2")) {
            holder.setText(R.id.tv_meaasge_last,
                    String.format("%s [图片]", onlineTime));
        } else if (type.equals("3")) {
            holder.setText(R.id.tv_meaasge_last,
                    String.format("%s [语音]", onlineTime));
        } else {
            holder.setText(R.id.tv_meaasge_last,
                    TextUtils.isEmpty(onlineTime) ? "" : onlineTime);
        }
        //数量的显示
        if (unReadNum > 99) {
            holder.setGone(R.id.tv_message_count, true);
            holder.setGone(R.id.iv_message_count_image, false);
        } else if (unReadNum > 0) {
            holder.setText(R.id.tv_message_count, String.valueOf(unReadNum));
            holder.setGone(R.id.tv_message_count, false);
            holder.setGone(R.id.iv_message_count_image, true);
        } else {
            holder.setGone(R.id.tv_message_count, true);
            holder.setGone(R.id.iv_message_count_image, true);
        }
    }
}