package com.maishuo.sharelibrary

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

/**
 * 垂直icon+msg
 */
class VerticalItemView(context: Context, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    //所有样式属性
    private var mIconWidth: Int? = null
    private var mIconHeight: Int? = null
    private var mIcon: Drawable? = null
    private var mInfoTextSize: Float? = null
    private var mInfoTextColor: Int? = null
    private var mInfoTextMarginTop: Int? = null
    private var mInfoText: String? = null

    /**
     * 构建自己的组合view
     */
    private fun createItemView(): View {
        val rootLayout = RelativeLayout(context)
        val mIconView = ImageView(context)
        mIconView.setImageDrawable(mIcon)
        mIconView.id = R.id.vertical_image_id

        val iconParams = LayoutParams(mIconWidth ?: 0, mIconHeight ?: 0)
        iconParams.addRule(CENTER_HORIZONTAL)
        rootLayout.addView(mIconView, iconParams)

        val mInfoView = TextView(context)
        mInfoView.id = R.id.vertical_text_id
        mInfoView.setTextColor(mInfoTextColor ?: 0)
        mInfoView.paint.textSize = mInfoTextSize ?: 0f
        mInfoView.text = mInfoText

        val textParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textParams.setMargins(0, mInfoTextMarginTop ?: 0, 0, 0)
        textParams.addRule(BELOW, R.id.vertical_image_id)
        textParams.addRule(CENTER_HORIZONTAL)
        rootLayout.addView(mInfoView, textParams)
        return rootLayout
    }

    init {
        @SuppressLint("CustomViewStyleable")
        val a = context.obtainStyledAttributes(attrs, R.styleable.VerticalItem)
        mIconWidth = a.getLayoutDimension(R.styleable.VerticalItem_iconWidth, 35)
        mIconHeight = a.getLayoutDimension(R.styleable.VerticalItem_iconHeight, 35)
        mIcon = a.getDrawable(R.styleable.VerticalItem_icon)
        mInfoTextSize = a.getDimension(R.styleable.VerticalItem_infoTextSize, 12f)
        mInfoTextColor = a.getColor(R.styleable.VerticalItem_infoTextColor, 0x333333)
        mInfoTextMarginTop = a.getLayoutDimension(R.styleable.VerticalItem_infoTextMarginTop, 10)
        mInfoText = a.getString(R.styleable.VerticalItem_infoText)
        a.recycle()

        //居中添加到布局中
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.addRule(CENTER_IN_PARENT)
        addView(createItemView(), params)
    }
}