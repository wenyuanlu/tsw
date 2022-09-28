package com.maishuo.tingshuohenhaowan.message.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.SystemMessageBean;
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
public class SystemAdapter extends CustomBaseAdapter<SystemMessageBean, CustomBaseViewHolder> {

    public SystemAdapter () {
        super(R.layout.system_message_item);
    }

    @Override
    protected void onConvert (@NotNull CustomBaseViewHolder holder, @Nullable SystemMessageBean bean) {
        String  avatar   = bean.getSystemMessageAvatar();
        String  title    = bean.getSystemMessageTitle();
        String  content  = bean.getSystemMessageContent();
        String  time     = bean.getSystemMessageTime();
        boolean isUnread = bean.isIsUnread();

        if (!TextUtils.isEmpty(avatar)) {
            GlideUtils.INSTANCE.loadImage(
                    getContext(),
                    avatar,
                    (ImageView) holder.getView(R.id.iv_system_head)
            );
        }

        holder.setText(R.id.iv_system_name, TextUtils.isEmpty(title) ? "" : title);
        holder.setText(R.id.iv_system_time, TextUtils.isEmpty(time) ? "" : time);
        holder.setText(R.id.iv_system_content, TextUtils.isEmpty(content) ? "" : content);

        holder.getView(R.id.tv_system_count).setVisibility(isUnread ? View.INVISIBLE : View.VISIBLE);
    }
}
