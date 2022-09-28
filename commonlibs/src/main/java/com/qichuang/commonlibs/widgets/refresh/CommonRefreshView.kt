package com.qichuang.commonlibs.widgets.refresh

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorInt
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class CommonRefreshView constructor(context: Context?, attrs: AttributeSet)
    : SmartRefreshLayout(context, attrs) {

    private var refreshHeader: CommonClassicsHeader? = null
    private var refreshBottom: ClassicsFooter? = null

    init {
        refreshHeader = CommonClassicsHeader(context)
        refreshBottom = ClassicsFooter(context)

        setRefreshHeader(refreshHeader!!)
        setRefreshFooter(refreshBottom!!)
    }

    fun setHeaderAccentColor(@ColorInt accentColor: Int) {
        refreshHeader?.setAccentColor(accentColor)
    }

    fun setFooterAccentColor(@ColorInt accentColor: Int) {
        refreshBottom?.setAccentColor(accentColor)
    }

    fun setFooterBackgroundColor(@ColorInt accentColor: Int) {
        refreshBottom?.setBackgroundColor(accentColor)
    }
}