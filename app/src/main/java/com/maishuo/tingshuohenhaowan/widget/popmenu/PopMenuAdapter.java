package com.maishuo.tingshuohenhaowan.widget.popmenu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maishuo.tingshuohenhaowan.R;

import java.util.List;

/**
 * author: yh
 * date: 2021/1/26 09:58
 * description: TODO:
 */
public class PopMenuAdapter extends RecyclerView.Adapter<PopMenuAdapter.ViewHolder> {

    private List<ActionItem>       mActionList;
    private PopMenu.Builder        mBuilder;
    private OnPopItemClickListener mItemClickListener;

    public PopMenuAdapter (List<ActionItem> newsList) {
        this.mActionList = newsList;
    }

    public void setBuild (PopMenu.Builder builder) {
        this.mBuilder = builder;
    }

    public void setItemClick (OnPopItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
        View       view       = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_popmenu, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder (@NonNull ViewHolder viewHolder, int position) {
        ActionItem bean = mActionList.get(position);
        viewHolder.mTv.setText(bean.getText());
        if (mBuilder.getShowImage()) {
            if (bean.getResId() != 0) {
                viewHolder.mIv.setImageResource(bean.getResId());
                viewHolder.mIv.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mIv.setVisibility(View.GONE);
            }
        } else {
            viewHolder.mIv.setVisibility(View.GONE);
        }
        viewHolder.mLlPopMenu.setOnClickListener(view -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClickListener(bean, position);
            }
        });
    }

    @Override
    public int getItemCount () {
        return mActionList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView    mIv;
        TextView     mTv;
        LinearLayout mLlPopMenu;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);

            mLlPopMenu = itemView.findViewById(R.id.ll_pop_menu);
            mIv = itemView.findViewById(R.id.iv_pop_menu);
            mTv = itemView.findViewById(R.id.tv_pop_menu);
        }
    }

    public interface OnPopItemClickListener {
        void onItemClickListener (ActionItem item, int position);
    }
}
