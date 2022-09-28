package com.maishuo.tingshuohenhaowan.main.dialog

import android.app.Activity
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.databinding.ViewCustomRecorderSelectorDialogLayoutBinding
import com.maishuo.tingshuohenhaowan.main.activity.CustomRecorderActivity
import com.maishuo.tingshuohenhaowan.main.activity.MainActivity
import com.maishuo.tingshuohenhaowan.main.event.MainConfigEvent
import com.maishuo.tingshuohenhaowan.ui.activity.WebViewActivity
import com.maishuo.tingshuohenhaowan.utils.CustomPreferencesUtils
import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils
import com.maishuo.umeng.ConstantEventId
import com.qichuang.commonlibs.basic.BaseDialogFragment
import org.greenrobot.eventbus.EventBus

/**
 * author : xpSun
 * date : 2021/4/1
 *description :
 */
class CustomRecorderSelectorDialog : BaseDialogFragment {

    constructor() : super()

    constructor(appCompatActivity: AppCompatActivity) : super(appCompatActivity)

    private var binding: ViewCustomRecorderSelectorDialogLayoutBinding? = null
    private var clickMenu: Boolean = false
    var selectorTypes: ArrayList<Int>? = null
    var edit: String? = null
    var isCanCheckActivityTag: Int? = null

    override fun fetchRootView(): View? {
        binding = ViewCustomRecorderSelectorDialogLayoutBinding.inflate(LayoutInflater.from(context))
        return binding?.root
    }

    override fun initWidgets() {

    }

    override fun initWidgetsEvent() {
        binding?.let {
            it.linPopupRecorderUp.setOnClickListener {
                clickMenu = true
                TrackingAgentUtils.onEvent(context, ConstantEventId.NEWvoice_Record_recording)
                CustomRecorderActivity.start(
                        context,
                        0,
                        selectorTypes,
                        edit,
                        isCanCheckActivityTag)
            }
            it.linPopupFileUp.setOnClickListener {
                clickMenu = true
                TrackingAgentUtils.onEvent(context, ConstantEventId.NEWvoice_Record_phone)
                CustomRecorderActivity.start(
                        context,
                        1,
                        selectorTypes,
                        edit,
                        isCanCheckActivityTag)
            }
            it.linPopupPcUp.setOnClickListener {
                clickMenu = true
                TrackingAgentUtils.onEvent(context, ConstantEventId.NEWvoice_Record_pc)
                val url = CustomPreferencesUtils.fetchPcUploadIntroduce()
                val intent = Intent(context, WebViewActivity::class.java)
                intent.putExtra("url", url)
                intent.putExtra(WebViewActivity.TITLE_NAME, getString(R.string.pc_up_course))
                (context as Activity).startActivityForResult(intent, 0)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initWidgetSize(Gravity.BOTTOM)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val mActivity = activity
        if (mActivity is MainActivity) {
            EventBus.getDefault().post(MainConfigEvent().setType(2).setTabId(mActivity.mainFragmentItem).setPlay(true))
        }
    }
}