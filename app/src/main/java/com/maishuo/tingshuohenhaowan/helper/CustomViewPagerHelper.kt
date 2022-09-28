package com.maishuo.tingshuohenhaowan.helper

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils
import com.maishuo.tingshuohenhaowan.widget.indicator.HXLinePagerIndicator
import com.maishuo.umeng.ConstantEventId
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

/**
 * author : xpSun
 * date : 10/15/21
 * description :
 */
object CustomViewPagerHelper {

    fun bind(activity: Activity?,
             tabList: MutableList<String>?,
             magicIndicator: MagicIndicator?,
             viewPager: ViewPager?
    ) {
        val commonNavigator = CommonNavigator(activity)
        commonNavigator.isAdjustMode = false
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return tabList?.size ?: 0
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView? {
                if (null == activity) {
                    return null
                }

                val simplePagerTitleView = SimplePagerTitleView(context)
                simplePagerTitleView.text = tabList?.get(index)
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, activity.resources.getDimension(R.dimen.sp_14))
                simplePagerTitleView.normalColor = ContextCompat.getColor(activity, R.color.ff84848f)
                simplePagerTitleView.selectedColor = ContextCompat.getColor(activity, R.color.white)
                simplePagerTitleView.setOnClickListener {
                    TrackingAgentUtils.onEvent(activity, if (index == 0) ConstantEventId.NEWvoice_mine_send else ConstantEventId.NEWvoice_mine_like)
                    viewPager?.currentItem = index
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = HXLinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(context, 2.0).toFloat()
                indicator.lineWidth = UIUtil.dip2px(context, 28.0).toFloat()
                indicator.roundRadius = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                return indicator
            }
        }

        magicIndicator?.navigator = commonNavigator

        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                magicIndicator?.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                magicIndicator?.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                magicIndicator?.onPageScrollStateChanged(state)
            }
        })
    }
}