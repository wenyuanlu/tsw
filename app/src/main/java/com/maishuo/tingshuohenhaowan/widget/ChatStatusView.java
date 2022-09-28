package com.maishuo.tingshuohenhaowan.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.maishuo.tingshuohenhaowan.R;

/**
 * author ：yh
 * date : 2021/1/20 16:39
 * description : 聊天状态展示
 */
public class ChatStatusView extends RelativeLayout {

    private ImageView      mError;
    private RelativeLayout mUnRead;
    private ProgressBar    mLoding;

    public ChatStatusView (Context context) {
        this(context, null);
    }

    public ChatStatusView (Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatStatusView (Context context, AttributeSet attrs, int defStyleAttr) {
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
        /*TypedArray typedArray      = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ImageTextLayout, defStyleAttr, 0);
        int        imageResourceId = typedArray.getResourceId(R.styleable.ImageTextLayout_commonImage, 0);
        int        imageWidth      = (int) typedArray.getDimensionPixelSize(R.styleable.ImageTextLayout_commonImageWidth, 44);
        int        imageHeight     = (int) typedArray.getDimensionPixelSize(R.styleable.ImageTextLayout_commonImageHeight, 44);
        String     title           = typedArray.getString(R.styleable.ImageTextLayout_commonTitle);
        int        titleSize       = (int) typedArray.getDimensionPixelSize(R.styleable.ImageTextLayout_commonTitleSize, 15 * 3);
        int        titleMaginTop   = (int) typedArray.getDimensionPixelSize(R.styleable.ImageTextLayout_commonTitleMarginTop, 0);

        typedArray.recycle();*/

        //定义view
        View view = LayoutInflater.from(context).inflate(R.layout.chat_status_item, this);
        mError = (ImageView) view.findViewById(R.id.iv_chat_status_error);
        mUnRead = (RelativeLayout) view.findViewById(R.id.iv_chat_status_unread);
        mLoding = (ProgressBar) view.findViewById(R.id.iv_chat_status_loding);

    }

    /**
     * 展示上传失败
     */
    public void showError () {
        mError.setVisibility(VISIBLE);
        mLoding.setVisibility(GONE);
        mUnRead.setVisibility(GONE);
    }

    /**
     * 展示loding中
     */
    public void showLoding () {
        mError.setVisibility(GONE);
        mLoding.setVisibility(VISIBLE);
        mUnRead.setVisibility(GONE);
    }

    /**
     * 成功的展示
     */
    public void showSuccess () {
        mError.setVisibility(GONE);
        mLoding.setVisibility(GONE);
        mUnRead.setVisibility(GONE);
    }

    /**
     * 未读的展示
     */
    public void showUnRead () {
        mError.setVisibility(GONE);
        mLoding.setVisibility(GONE);
        mUnRead.setVisibility(VISIBLE);
    }

    /**
     * 已读的展示
     */
    public void showRead () {
        mError.setVisibility(GONE);
        mLoding.setVisibility(GONE);
        mUnRead.setVisibility(GONE);
    }

}
