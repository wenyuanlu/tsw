package com.maishuo.tingshuohenhaowan.main.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.SoundeffectBean;
import com.maishuo.tingshuohenhaowan.utils.CustomPreferencesUtils;
import com.qichuang.commonlibs.basic.CustomBaseAdapter;
import com.qichuang.commonlibs.basic.CustomBaseViewHolder;
import com.qichuang.commonlibs.utils.GlideUtils;

import org.jetbrains.annotations.NotNull;

/**
 * author : xpSun
 * date : 2021/3/12
 * description : 录音选择背景音乐适配器
 */
public class CustomBGMSelectorAdapter extends CustomBaseAdapter<SoundeffectBean, CustomBaseViewHolder> {

    private int selectorPosition;

    public void setSelectorPosition (int selectorPosition) {
        this.selectorPosition = selectorPosition;
        CustomPreferencesUtils.saveRecorderSelectorPosition(selectorPosition);
        notifyDataSetChanged();
    }

    public CustomBGMSelectorAdapter () {
        super(R.layout.item_bg_music);
    }

    @Override
    protected void onConvert (@NotNull CustomBaseViewHolder baseViewHolder,
            SoundeffectBean soundeffectBean) {

        ImageView imageView = baseViewHolder.getView(R.id.iv_bg_music);
        TextView  tvShow    = baseViewHolder.getView(R.id.tv_bg_music_name);

        baseViewHolder.setVisible(R.id.iv_bg_music_selector_layout, selectorPosition == baseViewHolder.getAbsoluteAdapterPosition());

        if (0 == baseViewHolder.getAbsoluteAdapterPosition()) {
            baseViewHolder.setVisible(R.id.iv_bg_music_selector_layout, false);

            tvShow.setText("无");

            imageView.setImageResource(R.mipmap.recording_music_bounced_icon_none);
            imageView.setBackgroundResource(R.drawable.drawable_272432_r4_shape);
        } else {
            tvShow.setText(soundeffectBean.getName());

            baseViewHolder.setBackgroundResource(R.id.iv_bg_music,
                    R.color.transparent);
            GlideUtils.INSTANCE.loadImage(
                    getContext(),
                    soundeffectBean.getImg_path(),
                    imageView
            );
        }
    }
}
