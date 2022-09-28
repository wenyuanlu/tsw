package com.maishuo.tingshuohenhaowan.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

/**
 * author : xpSun
 * date : 2021/4/10
 * description :
 */
class CommonRecyclerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) :
        RecyclerView(context, attrs, defStyleAttr) {

    //关闭默认局部刷新动画
    @JvmOverloads
    fun closeDefaultAnimator(mRecyclerView: RecyclerView? = this) {
        if (null == mRecyclerView) return
        mRecyclerView.itemAnimator?.addDuration = 0
        mRecyclerView.itemAnimator?.changeDuration = 0
        mRecyclerView.itemAnimator?.moveDuration = 0
        mRecyclerView.itemAnimator?.removeDuration = 0

        (mRecyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }
}