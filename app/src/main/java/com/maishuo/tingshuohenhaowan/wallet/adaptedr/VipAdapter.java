package com.maishuo.tingshuohenhaowan.wallet.adaptedr;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.GetVipMoneyBean;
import com.maishuo.tingshuohenhaowan.listener.OnItemCommonClickListener;

import java.util.List;

/**
 * author ：yh
 * date : 2021/2/22 13:30
 * description :vip页面的金额展示
 */
public class VipAdapter extends RecyclerView.Adapter<VipAdapter.ViewHolder> {

    private final Context                   mContext;
    private       List<GetVipMoneyBean>     mData;
    private       OnItemCommonClickListener mListener;

    public VipAdapter (@NonNull Context context) {
        //super(context);
        mContext = context;
    }

    public void setData (List<GetVipMoneyBean> data) {
        mData = data;
    }

    public void setOnItemClickListener (OnItemCommonClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemCount () {
        return mData != null ? mData.size() : 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.vip_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull ViewHolder holder, int position) {
        GetVipMoneyBean getVipMoneyBean = mData.get(position);
        boolean         select          = getVipMoneyBean.isSelect();
        int             moneyShow       = getVipMoneyBean.getMoneyShow();
        int             moneyYuan       = getVipMoneyBean.getMoneyYuan();
        String          vipData         = getVipMoneyBean.getVipData();
        boolean         first           = getVipMoneyBean.isFirst();
        holder.mLlVipItem.setSelected(select);
        holder.mTvData.setText(vipData);
        holder.mTvMoneyShow.setText(moneyShow + "");
        holder.mTvMoneyYuan.setText(moneyYuan + "");
        if (position > 0) {//非第一个,只展示实际的价格
            if (moneyYuan == 0) {
                holder.mTvMoneyYuan.setVisibility(View.GONE);
            } else {
                holder.mTvMoneyYuan.setVisibility(View.VISIBLE);
            }
        }
        if (first) {
            holder.mIvFirst.setVisibility(View.VISIBLE);
            if (position == 0) {//首次,显示首次标识,显示折扣价格
                holder.mTvMoneyShow.setText(moneyShow + "");
                holder.mTvMoneyYuan.setText(moneyYuan + "");
                holder.mTvMoneyYuan.setVisibility(View.VISIBLE);
            }
        } else {
            holder.mIvFirst.setVisibility(View.INVISIBLE);
            if (position == 0) {//非首次,不显示首次标识,不显示显示折扣价格
                holder.mTvMoneyShow.setText(moneyYuan + "");
                holder.mTvMoneyYuan.setVisibility(View.GONE);
            }
        }

        holder.mLlVipItem.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onClick(position);
            }
        });

    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLlVipItem;
        private TextView     mTvMoneyShow;
        private TextView     mTvMoneyYuan;
        private TextView     mTvData;
        private ImageView    mIvFirst;


        public ViewHolder (View view) {
            super(view);
            mLlVipItem = view.findViewById(R.id.ll_vip_select);

            mTvMoneyYuan = view.findViewById(R.id.iv_vip_money_yuan);//原价
            mTvMoneyShow = view.findViewById(R.id.iv_vip_money);//显示价格
            mTvData = view.findViewById(R.id.iv_vip_time);//时间
            mIvFirst = view.findViewById(R.id.iv_vip_first);//是否第一次
            mTvMoneyYuan.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中划线

        }

    }
}

