package com.maishuo.tingshuohenhaowan.message.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.CollectCareBean;
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
public class FriendAdapter extends CustomBaseAdapter<CollectCareBean, CustomBaseViewHolder> {

    public FriendAdapter () {
        super(R.layout.friend_item);
    }

    @Override
    protected void onConvert (@NotNull CustomBaseViewHolder holder, @Nullable CollectCareBean bean) {
        String userAvatar       = bean.getUserAvatar();
        String userName         = bean.getUserName();
        int    userFans         = bean.getUserFans();
        int    phonicNumber     = bean.getPhonicNumber();
        int    attentionsStatus = bean.getAttentionsStatus();//1是粉丝  3是我关注的 2是朋友

        if (!TextUtils.isEmpty(userAvatar)) {
            GlideUtils.INSTANCE.loadImage(
                    getContext(),
                    userAvatar,
                    (ImageView)
                            holder.getView(R.id.iv_friend_result_head)
            );
        }
        holder.setText(R.id.iv_friend_result_name, TextUtils.isEmpty(userName) ? "" : userName);
        holder.setText(R.id.iv_friend_result_work, String.format("作品：%s", phonicNumber));
        holder.setText(R.id.iv_friend_result_fans, String.format("粉丝：%s", userFans));

        if (attentionsStatus == 1) {
            //粉丝
            holder.setText(R.id.tv_friend_care, "关注");
            holder.setGone(R.id.tv_friend_care, false);
            holder.getView(R.id.tv_friend_care).setSelected(false);
            holder.setGone(R.id.ll_friend_hi, true);
        } else if (attentionsStatus == 3) {
            //我关注的
            holder.setText(R.id.tv_friend_care, "已关注");
            holder.setGone(R.id.tv_friend_care, false);
            holder.getView(R.id.tv_friend_care).setSelected(true);
            holder.setGone(R.id.ll_friend_hi, true);
        } else if (attentionsStatus == 2) {
            //朋友
            holder.setGone(R.id.tv_friend_care, true);
            holder.setGone(R.id.ll_friend_hi, false);
        }
    }
}
