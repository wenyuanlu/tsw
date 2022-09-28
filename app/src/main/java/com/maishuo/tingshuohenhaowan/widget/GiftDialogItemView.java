package com.maishuo.tingshuohenhaowan.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.gift.view.GiftItemAdapter;
import com.maishuo.tingshuohenhaowan.api.response.GetGfitBean;
import com.maishuo.tingshuohenhaowan.listener.OnViewPageItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：yh
 * date : 2021/1/20 16:39
 * description :礼物的的viewPage的子页面
 */
public class GiftDialogItemView extends RelativeLayout {
    private final List<GetGfitBean.GiftsBean> mData         = new ArrayList<>();
    private       OnViewPageItemClickListener mListener;
    private       int                         mPagePosition = 0;
    private       GiftItemAdapter             mAdapter;

    public GiftDialogItemView (Context context) {
        this(context, null);
    }

    public GiftDialogItemView (Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GiftDialogItemView (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void updateData (List<GetGfitBean.GiftsBean> data, int pagePosition) {
        mPagePosition = pagePosition;
        int startPosition = pagePosition * 8;
        int endPosition   = startPosition + 8;
        if (endPosition > data.size()) {
            endPosition = data.size();
        }
        List<GetGfitBean.GiftsBean> mRealyDate = data.subList(startPosition, endPosition);

        mData.clear();
        mData.addAll(mRealyDate);
        mAdapter.notifyDataSetChanged();
    }

    public void setOnItemClickListener (OnViewPageItemClickListener listener) {
        mListener = listener;
    }

    //初始化
    private void init (Context context) {
        View               view          = LayoutInflater.from(context).inflate(R.layout.view_dialog_gift_bottom_item_layout, this);
        CommonRecyclerView mRecyclerView = view.findViewById(R.id.rv_gift_item);

        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        mAdapter = new GiftItemAdapter();

        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
            GetGfitBean.GiftsBean item = mData.get(position);
            if (mListener != null) {
                int returnPosition = mPagePosition * 8 + position;
                mListener.onClick(returnPosition, mPagePosition, item);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setNewInstance(mData);
    }
}
