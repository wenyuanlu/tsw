package com.maishuo.tingshuohenhaowan.main.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.PublishTagBean;
import com.qichuang.commonlibs.basic.CustomBaseAdapter;
import com.qichuang.commonlibs.basic.CustomBaseViewHolder;
import com.qichuang.commonlibs.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;

/**
 * author : xpSun
 * date : 2021/3/15
 * description : 录音选择分类适配器
 */
public class CustomRecorderSelectorTypeAdapter extends CustomBaseAdapter<PublishTagBean.TypeListBean, CustomBaseViewHolder> {

    public CustomRecorderSelectorTypeAdapter () {
        super(R.layout.view_selector_tag_layout);
    }

    @Override
    protected void onConvert (@NotNull CustomBaseViewHolder baseViewHolder, PublishTagBean.TypeListBean typeListBean) {
        ImageView iv_tag_bg    = baseViewHolder.getView(R.id.iv_tag_bg);
        ImageView iv_tag_right = baseViewHolder.getView(R.id.iv_tag_right);
        TextView  tv_tag_name  = baseViewHolder.getView(R.id.tv_tag_name);

        if (typeListBean.getShowType() == 0) {
            GlideUtils.INSTANCE.loadImage(
                    getContext(),
                    R.mipmap.classification_bounced_button_default,
                    iv_tag_bg
            );

            iv_tag_right.setVisibility(View.GONE);
        } else {
            GlideUtils.INSTANCE.loadImage(
                    getContext(),
                    R.mipmap.classification_bounced_button_choose,
                    iv_tag_bg
            );

            iv_tag_right.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(typeListBean.getIcon_img())) {
                GlideUtils.INSTANCE.loadImage(
                        getContext(),
                        typeListBean.getIcon_img(),
                        iv_tag_right
                );
            }
        }

        tv_tag_name.setText(typeListBean.getName());
    }
}
