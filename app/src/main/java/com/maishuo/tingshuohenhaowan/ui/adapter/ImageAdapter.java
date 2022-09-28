package com.maishuo.tingshuohenhaowan.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.common.Constant;
import com.maishuo.tingshuohenhaowan.api.response.MyPersonalBean.BannerBean;
import com.maishuo.tingshuohenhaowan.personal.ui.PersonCenterActivity;
import com.maishuo.tingshuohenhaowan.ui.activity.WebViewActivity;
import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils;
import com.maishuo.tingshuohenhaowan.wallet.ui.VipActivity;
import com.maishuo.tingshuohenhaowan.widget.CircleImageView;
import com.maishuo.umeng.ConstantEventId;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.GlideUtils;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * author: yh
 * date: 2021/2/5 13:13
 * description: banner的图片加载
 */
public class ImageAdapter extends BannerAdapter<BannerBean, ImageAdapter.BannerViewHolder> {

    private Context mContext;

    public ImageAdapter (List<BannerBean> mDatas) {
        super(mDatas);
    }

    @Override
    public BannerViewHolder onCreateHolder (ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.banner_item, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindView (BannerViewHolder holder, BannerBean data, int position, int size) {
        //图片的展示
        if (!TextUtils.isEmpty(data.getIcon())) {
            GlideUtils.INSTANCE.loadImage(
                    mContext,
                    data.getIcon(),
                    holder.mIv
            );
        }
        //点击事件
        holder.mIv.setOnClickListener(v -> {
            int index = data.getIndex();
            if (index == 1) {//跳转vip页面
                TrackingAgentUtils.onEvent(mContext, ConstantEventId.NEWvoice_mine_banner1);
                mContext.startActivity(new Intent(mContext, VipActivity.class));
            } else if (index == 2) {//TODO邀请好友,先判断是否登录 Constant.INVITE_URL
                TrackingAgentUtils.onEvent(mContext, ConstantEventId.NEWvoice_mine_mine_banner2);
                String userId = PreferencesUtils.getString(PreferencesKey.USER_ID, "");
                String url    = Constant.INVITE_URL + "?key=" + userId;
                WebViewActivity.to(mContext, url, "");
            } else if (index == 3) {//以前的参与演说,成为主播的页面
                TrackingAgentUtils.onEvent(mContext, ConstantEventId.NEWvoice_mine_mine_banner3);
            } else if (index == 4) {//以前的演说听书页
            } else if (index == 5) {//TODO:跳转到webview页面
                String ldp = data.getLdp();
                if (ldp.contains("wechat")) {
                    ldp = ldp + "?listen=1";
                }
                WebViewActivity.to(mContext, ldp, data.getName());
            } else if (index == 6) {//跳转到其他人的用户中心
                String userId   = data.getUserId();
                String userName = data.getUserName();
                PersonCenterActivity.to(mContext, userId);
            } else if (index == 7) {//打开deepLink
                String ldp = data.getLdp();
                if (!TextUtils.isEmpty(ldp)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ldp));
                    mContext.startActivity(intent);
                }
            }

            //热云Banner点击行为埋点
            TrackingAgentUtils.setEvent("event_5");
        });
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mIv;
        public BannerViewHolder (@NonNull View itemView) {
            super(itemView);
            mIv = itemView.findViewById(R.id.iv_banner_item);
        }
    }
}
