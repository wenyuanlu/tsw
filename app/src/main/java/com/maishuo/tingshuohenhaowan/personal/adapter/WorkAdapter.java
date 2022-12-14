package com.maishuo.tingshuohenhaowan.personal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.module.LoadMoreModule;
import com.google.gson.Gson;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.MyPhonicListBean;
import com.maishuo.tingshuohenhaowan.main.event.AttentionEvent;
import com.maishuo.tingshuohenhaowan.main.event.PraiseEvent;
import com.maishuo.tingshuohenhaowan.main.activity.VoicePlayActivity;
import com.qichuang.commonlibs.basic.CustomBaseAdapter;
import com.qichuang.commonlibs.basic.CustomBaseViewHolder;
import com.qichuang.commonlibs.utils.DeviceUtil;
import com.qichuang.commonlibs.utils.GlideUtils;
import com.qichuang.commonlibs.utils.PreferencesUtils;

import java.util.List;


public class WorkAdapter extends CustomBaseAdapter<MyPhonicListBean, CustomBaseViewHolder> implements LoadMoreModule {

    private final String userId;
    int width;
    int height;

    public WorkAdapter (List<MyPhonicListBean> list, String userId, Context context) {
        super(R.layout.work_item, list);
        this.userId = userId;

        int deviceWidth = DeviceUtil.getDeviceWidth();
        int space       = DeviceUtil.dip2px(context, 10);
        width = (deviceWidth - space) / 3;
        height = (deviceWidth - space) / 3;
    }

    @Override
    protected void onConvert (@NonNull CustomBaseViewHolder helper, MyPhonicListBean item) {
        helper.getView(R.id.rl_item).setLayoutParams(new LinearLayout.LayoutParams(width, height + (height / 4)));

        GlideUtils.INSTANCE.loadImage(getContext(), item.getImage_path(), (ImageView) helper.getView(R.id.iv_work));
        helper.setText(R.id.tv_work_like_count, item.getPraise_num() + "");

        helper.getView(R.id.iv_work).setOnClickListener(view -> {
            PreferencesUtils.putString("PhonicListData", new Gson().toJson(getData()));
            VoicePlayActivity.to(getContext(), helper.getAbsoluteAdapterPosition(), userId);
        });
    }

    /**
     * ???????????????????????????????????????
     */
    public void newDataAfterAttention (AttentionEvent event) {
        if (!getData().isEmpty()) {
            for (MyPhonicListBean bean : getData()) {
                if (TextUtils.equals(bean.getUser_id(), event.userId)) {
                    //??????????????????????????????0-????????????1-????????????????????????1 - ????????? 2 - ?????? 3 - ?????????
                    bean.setIs_attention(event.statues == 1 ? 0 : 1);
                }
            }
            notifyDataSetChanged();
        }
    }

    /**
     * ???????????????????????????????????????
     */
    public void newDataAfterPraise (PraiseEvent event) {
        if (!getData().isEmpty()) {
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
