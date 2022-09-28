package com.maishuo.tingshuohenhaowan.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maishuo.tingshuohenhaowan.R;

/**
 * 上边图片,下方标题,带有消息
 */
public class MessageHeadView extends RelativeLayout {

    private RelativeLayout mItem;
    private ImageView      mIvHead;
    private TextView       mTvCount;
    private ImageView      mIvCount;
    private TextView       mTvTitle;

    public MessageHeadView (Context context) {
        this(context, null);
    }

    public MessageHeadView (Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageHeadView (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init (Context context, AttributeSet attrs, int defStyleAttr) {
        //获取属性
        TypedArray typedArray   = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MessageHeadLayout, defStyleAttr, 0);
        int        resourceId   = typedArray.getResourceId(R.styleable.MessageHeadLayout_topImage, 0);
        int        headWidth    = (int) typedArray.getDimensionPixelSize(R.styleable.MessageHeadLayout_headWidth, 44);
        int        headHeight   = (int) typedArray.getDimensionPixelSize(R.styleable.MessageHeadLayout_headHeight, 44);
        int        headMaginTop = (int) typedArray.getDimensionPixelSize(R.styleable.MessageHeadLayout_headMarginTop, 0);
        int        messageSize  = (int) typedArray.getDimensionPixelSize(R.styleable.MessageHeadLayout_messageSize, 15 * 3);
        String     title        = typedArray.getString(R.styleable.MessageHeadLayout_titleName);
        boolean    showTitle    = typedArray.getBoolean(R.styleable.MessageHeadLayout_showTitle, false);

        typedArray.recycle();
        //int i = DeviceUtil.dip2px(context, 44);
        //EasyLog.print("返回的尺寸" + headWidth + "固定尺寸" + i);
        //EasyLog.print("返回的尺寸" + headWidth + "固定尺寸" + i);
        //EasyLog.print("返回的尺寸" + headWidth + "固定尺寸" + i);

        //定义view
        View view = LayoutInflater.from(context).inflate(R.layout.message_head_item, this);
        mItem = view.findViewById(R.id.message_item);
        mIvHead = view.findViewById(R.id.message_item_head);
        mTvCount = view.findViewById(R.id.message_item_count);
        mIvCount = view.findViewById(R.id.message_item_count_image);
        mTvTitle = view.findViewById(R.id.message_item_title);

        //取控件当前的布局参数
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mIvHead.getLayoutParams();
        params.width = headWidth;//设置宽度值
        params.height = headHeight;//设置高度值
        mIvHead.setLayoutParams(params);

        RelativeLayout.LayoutParams itemParams = (RelativeLayout.LayoutParams) mItem.getLayoutParams();
        itemParams.width = headWidth + headMaginTop;//设置宽度值
        itemParams.height = headHeight + headMaginTop;//设置高度值
        mItem.setLayoutParams(itemParams);

        RelativeLayout.LayoutParams countParams = (RelativeLayout.LayoutParams) mTvCount.getLayoutParams();
        countParams.height = messageSize;
        countParams.width = messageSize;
        mTvCount.setLayoutParams(countParams);

        RelativeLayout.LayoutParams countIvParams = (RelativeLayout.LayoutParams) mIvCount.getLayoutParams();
        countIvParams.height = messageSize / 2;
        countIvParams.width = messageSize / 2;
        mIvCount.setLayoutParams(countParams);

        //上方图片资源加载
        if (resourceId > 0) {
            mIvHead.setImageResource(resourceId);
        }

        //展示下方标题
        if (!TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
        }

        if (showTitle) {
            mTvTitle.setVisibility(VISIBLE);
        } else {
            mTvTitle.setVisibility(GONE);
        }
    }

    /**
     * 设置消息数量
     */
    public void setCount (int count) {
        if (count > 0) {
            String content = String.valueOf(count);
            if (count > 99) {
                content = "";
                mIvCount.setVisibility(VISIBLE);
            } else {
                mIvCount.setVisibility(GONE);
            }
            mTvCount.setText(content);
            mTvCount.setVisibility(VISIBLE);
        } else {
            mTvCount.setVisibility(GONE);
            mIvCount.setVisibility(GONE);
        }
    }

    /**
     * 设置消息数量的显示
     */
    public void setCountShow (boolean isShow) {
        if (isShow) {
            mTvCount.setVisibility(VISIBLE);
            mIvCount.setVisibility(GONE);
        } else {
            mTvCount.setVisibility(GONE);
            mIvCount.setVisibility(GONE);
        }
    }


}
