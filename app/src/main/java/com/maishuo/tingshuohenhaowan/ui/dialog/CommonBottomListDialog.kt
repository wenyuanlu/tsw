package com.maishuo.tingshuohenhaowan.ui.dialog

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.bean.DialogBottomMoreBean
import com.maishuo.tingshuohenhaowan.databinding.ViewBottomListLayoutBinding
import com.maishuo.tingshuohenhaowan.listener.OnDialogBottomMoreListener
import com.maishuo.tingshuohenhaowan.ui.adapter.DialogBottomMoreAdapter
import com.qichuang.commonlibs.basic.BaseDialog

/**
 * author : xpSun
 * date : 10/13/21
 * description :
 */
class CommonBottomListDialog(activity: AppCompatActivity?) :
        BaseDialog(activity, R.style.NoBackGroundDialog) {

    private var binding: ViewBottomListLayoutBinding? = null
    private var adapter: DialogBottomMoreAdapter? = null

    var onDialogBottomMoreListener: OnDialogBottomMoreListener? = null
    var bottomListData: MutableList<DialogBottomMoreBean>? = null
        set(value) {
            field = value
            adapter?.setNewInstance(value)
        }

    override fun fetchRootView(): View? {
        binding = ViewBottomListLayoutBinding.inflate(LayoutInflater.from(activity))
        return binding?.root
    }

    override fun initWidgets() {
        setGravity(Gravity.BOTTOM,resId = R.style.dialog_animation)

        binding?.let {
            it.commonBottomRecycler.layoutManager = LinearLayoutManager(activity)
            adapter = DialogBottomMoreAdapter()
            adapter?.setOnItemClickListener { _, view, position ->
                onDialogBottomMoreListener?.OnItemBack(view, position, dialog)
            }
            it.commonBottomRecycler.adapter = adapter

            it.commonBottomCancel.setOnClickListener {
                dismiss()
            }
        }
    }
}