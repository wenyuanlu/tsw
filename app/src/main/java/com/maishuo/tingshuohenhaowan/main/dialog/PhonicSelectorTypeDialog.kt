package com.maishuo.tingshuohenhaowan.main.dialog

import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import com.maishuo.tingshuohenhaowan.databinding.ViewPhonicSelectorTypeLayoutBinding
import com.maishuo.tingshuohenhaowan.api.response.PhonicTagBean
import com.maishuo.tingshuohenhaowan.main.adapter.CustomPagerAdapter
import com.maishuo.tingshuohenhaowan.main.adapter.TagAdapter
import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils
import com.maishuo.umeng.ConstantEventId
import com.qichuang.commonlibs.basic.BaseDialog
import com.qichuang.commonlibs.utils.DeviceUtil
import com.qichuang.commonlibs.utils.ScreenUtils
import java.util.*
import kotlin.math.min

/**
 * 留声类型选择dialog
 */
class PhonicSelectorTypeDialog(appCompatActivity: AppCompatActivity?) :
        BaseDialog(appCompatActivity) {

    companion object {
        private const val GRID_NUMBER_QUANTITY: Int = 8
    }

    private var binding: ViewPhonicSelectorTypeLayoutBinding? = null
    private var adapter: CustomPagerAdapter? = null

    var onSelectorTypeListener: ((phonicTagBean: PhonicTagBean?) -> Unit)? = null

    override fun fetchRootView(): View? {
        binding = ViewPhonicSelectorTypeLayoutBinding.inflate(LayoutInflater.from(activity))
        return binding?.root
    }

    override fun initWidgets() {
        val height = (ScreenUtils.getRealyScreenHeight(activity) * 0.2).toInt()
        setGravity(Gravity.BOTTOM, ViewGroup.LayoutParams.MATCH_PARENT, height)

        adapter = CustomPagerAdapter()
        binding?.viewpager?.adapter = adapter
        binding?.pvGuideIndicator?.setViewPager(binding?.viewpager)

        setCanceledOnTouchOutside(true)
        setCancelable(true)
    }

    fun setPhonicTagBean(phonicTagBean: List<PhonicTagBean>?, bean: PhonicTagBean?) {
        if (bean == null && !phonicTagBean.isNullOrEmpty()) {
            phonicTagBean[0].isSelect = true
        } else {
            if (!phonicTagBean.isNullOrEmpty()) {
                for (beans in phonicTagBean) {
                    beans.isSelect = beans.id == bean!!.id
                }
            }
        }

        setPhonicTypeData(phonicTagBean)
    }

    private fun setPhonicTypeData(phonicTagBean: List<PhonicTagBean>?) {
        if (phonicTagBean == null || phonicTagBean.isEmpty()) {
            return
        }

        val lists = groupListByQuantity(phonicTagBean)
        val views = ArrayList<GridView>()

        if (lists.isNullOrEmpty()) {
            return
        }

        for (item in lists.iterator()) {
            val gridView = GridView(activity)
            gridView.numColumns = 4
            gridView.horizontalSpacing = DeviceUtil.dip2px(activity, 10f)
            gridView.verticalSpacing = DeviceUtil.dip2px(activity, 20f)

            val tagAdapter = TagAdapter(item, activity)
            gridView.adapter = tagAdapter
            gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                onSelectorTypeListener?.invoke(item[position])
                umengAgent(item[position])
                dismiss()
            }
            views.add(gridView)
        }
        adapter?.setViews(views)
    }

    private fun groupListByQuantity(list: List<PhonicTagBean>?): List<List<PhonicTagBean>>? {
        if (list.isNullOrEmpty()) {
            return null
        }

        val wrapList: MutableList<List<PhonicTagBean>> = ArrayList()
        var count = 0
        while (count < list.size) {
            wrapList.add(ArrayList(list.subList(count, min(count + GRID_NUMBER_QUANTITY, list.size))))
            count += GRID_NUMBER_QUANTITY
        }
        return wrapList
    }

    /**
     * 埋点
     */
    private fun umengAgent(bean: PhonicTagBean) {
        if (TextUtils.equals(bean.name, "全部")) {
            TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_sort_all)
        } else if (TextUtils.equals(bean.name, "吐槽")) {
            TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_sort_vent)
        } else if (TextUtils.equals(bean.name, "热点")) {
            TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_sort_hotspot)
        } else if (TextUtils.equals(bean.name, "生活")) {
            TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_sort_life)
        } else if (TextUtils.equals(bean.name, "搞笑")) {
            TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_sort_Funny)
        } else if (TextUtils.equals(bean.name, "自然")) {
            TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_sort_natural)
        } else if (TextUtils.equals(bean.name, "音乐")) {
            TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_sort_music)
        } else if (TextUtils.equals(bean.name, "万象")) {
            TrackingAgentUtils.onEvent(activity, ConstantEventId.NEWvoice_sort_whole)
        }
    }
}