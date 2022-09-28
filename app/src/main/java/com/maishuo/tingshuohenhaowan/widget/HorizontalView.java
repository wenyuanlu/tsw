package com.maishuo.tingshuohenhaowan.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maishuo.tingshuohenhaowan.R;

/**
 * author ：yh
 * date : 2021/1/20 16:39
 * description : 左侧标题,中间内容,右侧提示内容,右侧箭头
 */
public class HorizontalView extends RelativeLayout {

    private ImageView mIvArrow;
    private TextView  mTvTitle;
    private TextView  mTvHint;
    private TextView  mTvCenter;

    public HorizontalView (Context context) {
        this(context, null);
    }

    public HorizontalView (Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalView (Context context, AttributeSet attrs, int defStyleAttr) {
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
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HorizontalLayout, defStyleAttr, 0);
        String     title      = typedArray.getString(R.styleable.HorizontalLayout_horizontalTitle);
        int        titleSize  = (int) typedArray.getDimensionPixelSize(R.styleable.HorizontalLayout_horizontalTitleSize, (int) getResources().getDimension(R.dimen.sp_14));
        int        titleColor = (int) typedArray.getColor(R.styleable.HorizontalLayout_horizontalTitleColor, getResources().getColor(R.color.FFBFBFC6));

        String  hint        = typedArray.getString(R.styleable.HorizontalLayout_horizontalHint);
        int     hintSize    = (int) typedArray.getDimensionPixelSize(R.styleable.HorizontalLayout_horizontalHintSize, (int) getResources().getDimension(R.dimen.sp_12));
        int     hintColor   = (int) typedArray.getColor(R.styleable.HorizontalLayout_horizontalHintColor, getResources().getColor(R.color.FFD6D6DD));
        boolean isShowArrow = typedArray.getBoolean(R.styleable.HorizontalLayout_horizontalShowArrow, true);

        typedArray.recycle();

        //定义view
        View view = LayoutInflater.from(context).inflate(R.layout.horizontal_item, this);
        mTvTitle = view.findViewById(R.id.tv_horizontal_title);
        mTvHint = view.findViewById(R.id.tv_horizontal_hint);
        mTvCenter = view.findViewById(R.id.tv_horizontal_center);
        mIvArrow = view.findViewById(R.id.iv_horizontal_arrow);

        //取控件当前的布局参数
        /*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mIvArrow.getLayoutParams();
        params.width = imageWidth;//设置宽度值
        params.height = imageHeight;//设置高度值
        mIvArrow.setLayoutParams(params);*/

       /* LinearLayout.LayoutParams countParams = (LinearLayout.LayoutParams) mTvTitle.getLayoutParams();
        countParams.topMargin = titleMaginTop;
        mTvTitle.setLayoutParams(countParams);*/

        //上方图片资源加载
        /*if (imageResourceId > 0) {
            mIvArrow.setImageResource(imageResourceId);
        }*/

        //展示左侧标题
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

        //展示右侧提示内容
        if (!TextUtils.isEmpty(hint)) {
            mTvHint.setText(hint);
        }

        //设置提示内容的文字大小
        if (hintSize > 0) {
            mTvHint.setTextSize(TypedValue.COMPLEX_UNIT_PX, hintSize);
        }

        //设置提示内容的颜色
        if (hintColor != 0) {
            mTvHint.setTextColor(hintColor);
        }

        if (isShowArrow) {
            mIvArrow.setVisibility(VISIBLE);
        } else {
            mIvArrow.setVisibility(GONE);
        }
    }

    /**
     * 动态设置右侧提示内容
     */
    public void setHintText (String text) {
        if (!TextUtils.isEmpty(text)) {
            mTvHint.setText(text);
        }

    }
}
