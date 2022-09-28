package com.maishuo.tingshuohenhaowan.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maishuo.tingshuohenhaowan.R;

/**
 * author ：yh
 * date : 2021/1/20 16:39
 * description : 上边图片,下方文字
 */
public class ImageTextView extends RelativeLayout {

    private ImageView mIvHead;
    private TextView  mTvTitle;

    public ImageTextView (Context context) {
        this(context, null);
    }

    public ImageTextView (Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTextView (Context context, AttributeSet attrs, int defStyleAttr) {
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
        TypedArray typedArray      = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImageTextLayout, defStyleAttr, 0);
        int        imageResourceId = typedArray.getResourceId(R.styleable.ImageTextLayout_commonImage, 0);
        int        imageWidth      = (int) typedArray.getDimensionPixelSize(R.styleable.ImageTextLayout_commonImageWidth, 44);
        int        imageHeight     = (int) typedArray.getDimensionPixelSize(R.styleable.ImageTextLayout_commonImageHeight, 44);
        String     title           = typedArray.getString(R.styleable.ImageTextLayout_commonTitle);
        int        titleSize       = (int) typedArray.getDimensionPixelSize(R.styleable.ImageTextLayout_commonTitleSize, 15 * 3);
        int        titleMaginTop   = (int) typedArray.getDimensionPixelSize(R.styleable.ImageTextLayout_commonTitleMarginTop, 0);
        int        titleColor      = (int) typedArray.getColor(R.styleable.ImageTextLayout_commonTitleColor, Color.WHITE);

        typedArray.recycle();

        //定义view
        View view = LayoutInflater.from(context).inflate(R.layout.image_text_item, this);
        mIvHead = view.findViewById(R.id.iv_common_top);
        mTvTitle = view.findViewById(R.id.text_common_bottom);

        //取控件当前的布局参数
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mIvHead.getLayoutParams();
        params.width = imageWidth;//设置宽度值
        params.height = imageHeight;//设置高度值
        mIvHead.setLayoutParams(params);

        LinearLayout.LayoutParams countParams = (LinearLayout.LayoutParams) mTvTitle.getLayoutParams();
        countParams.topMargin = titleMaginTop;
        mTvTitle.setLayoutParams(countParams);

        //上方图片资源加载
        if (imageResourceId > 0) {
            mIvHead.setImageResource(imageResourceId);
        }

        //展示下方标题
        if (!TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
        }

        //设置标题的文字大小
        if (titleSize > 0) {
            mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        }

        //设置标题的颜色
        if (titleColor != 0) {
            mTvTitle.setTextColor(titleColor);
        }
    }

    /**
     * 设置title
     *
     * @param title
     */
    public void setTitle (String title) {
        if (!TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
        }
    }
}
