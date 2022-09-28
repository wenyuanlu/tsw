package com.maishuo.tingshuohenhaowan.gift.view

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.databinding.ViewSendGiftDialogLayoutBinding
import com.maishuo.tingshuohenhaowan.gift.giftbean.GiftBackBean
import com.maishuo.tingshuohenhaowan.gift.listener.OnGiftDialogListener
import com.maishuo.tingshuohenhaowan.gift.viewmodel.SendGiftViewModel
import com.maishuo.tingshuohenhaowan.api.response.GetGfitBean
import com.maishuo.tingshuohenhaowan.api.response.GiftBuyBean
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackListener
import com.maishuo.tingshuohenhaowan.utils.DialogUtils
import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils
import com.maishuo.tingshuohenhaowan.widget.GiftDialogItemView
import com.qichuang.commonlibs.basic.BaseDialogFragment
import com.qichuang.commonlibs.common.PreferencesKey
import com.qichuang.commonlibs.utils.PreferencesUtils

/**
 * author : xpSun
 * date : 10/12/21
 * description :
 */
class SendGiftDialog : BaseDialogFragment {

    constructor()

    constructor(activity: AppCompatActivity) : super(activity)

    private var binding: ViewSendGiftDialogLayoutBinding? = null
    private var viewModel: SendGiftViewModel? = null
    private var pageDatas: MutableList<GiftDialogItemView> = mutableListOf()

    //上一次选中的
    private var mLastSelectorPosition: Int = 0

    //上当前选中的礼物位置
    private var mSelectPosition: Int = 0
    private var mCurrentItem: Int = 0

    private var mDiamodAndroid: Long? = null

    //类型 1直播,2私聊,3密友,4留声,5听书,6演说
    var type: Int? = null

    //发送给谁
    var mToUserId: String? = null

    //留声Id
    var mPhonicId: String? = null

    var onGiftDialogListener: OnGiftDialogListener? = null

    override fun fetchRootView(): View? {
        binding = ViewSendGiftDialogLayoutBinding.inflate(LayoutInflater.from(activity))
        viewModel = SendGiftViewModel(appCompatActivity)
        viewModel?.mToUserId = mToUserId
        viewModel?.mPhonicId = mPhonicId
        binding?.viewmodel = viewModel
        return binding?.root
    }

    override fun initWidgets() {
        viewModel?.let {
            it.type = type
            it.fetchGiftDataLiveData.observe(this) { response ->
                mDiamodAndroid = response?.diamodAndroid
                showGiftViewPager(response?.gifts)
            }
            it.fetchGiftData()
            it.fetchGiftBuyLiveData.observe(this) { response ->
                mDiamodAndroid = response?.diamodAndroid
                sendGiftInfo(it.userSelectorSendGiftBean, response)
            }
            it.sendGiftErrorLiveData.observe(this) {
                initRecharge()
            }
            it.dismissDialogLiveData.observe(this) {
                dismiss()
            }
        }
    }

    private fun initRecharge() {
        DialogUtils.showCommonDialog(appCompatActivity, "余额不足",
                "当前余额不足，是否前去充值？", object : OnDialogBackListener {
            override fun onSure(content: String) {
                viewModel?.userClickRecharge()
            }

            override fun onCancel() {

            }
        })
    }

    private fun sendGiftInfo(bean: GetGfitBean.GiftsBean?, giftBuyBean: GiftBuyBean?) {
        binding?.sendGiftMoney?.text = giftBuyBean?.diamodAndroid.toString()

        val userName = PreferencesUtils.getString(PreferencesKey.USER_NAME, "")
        val useId = PreferencesUtils.getString(PreferencesKey.USER_ID, "")
        val giftBackBean = GiftBackBean()
        //id
        giftBackBean.id = bean?.id ?: 0
        giftBackBean.name = bean?.name //礼物名称
        giftBackBean.userName = userName //发礼物的用户名
        giftBackBean.userId = useId //发礼物的用户id
        giftBackBean.version = bean?.gift_version.toString()
        giftBackBean.isHaveSvga = bean?.effect == 1 //是否有特效
        giftBackBean.img = bean?.img //礼物图片
        giftBackBean.isLocalImg = false
        giftBackBean.efectSvga = bean?.effect_img
        giftBackBean.isLocalSvga = false //需要加一部判断是否有本地的svga文件

        onGiftDialogListener?.onGiftBack(giftBackBean)

        //全屏动效不能连击
        if (1 == bean?.effect) {
            dismiss()
        }

        //热云统计打赏
        if (type == 4) {
            TrackingAgentUtils.setEvent("event_2")
        } else if (type == 3) {
            TrackingAgentUtils.setEvent("event_3")
        }
    }

    private fun showGiftViewPager(gifts: MutableList<GetGfitBean.GiftsBean>?) {
        if (gifts.isNullOrEmpty()) {
            return
        }

        //把第一个默认设置为选中
        for (i in gifts.indices) {
            val giftsBean: GetGfitBean.GiftsBean = gifts[i]
            giftsBean.isSelect = i == 0
            gifts[i] = giftsBean
        }

        var pagerCount = 0
        val size: Int = gifts.size
        val contentSize = size / 8
        val yu = size % 8

        if (yu > 0) {
            pagerCount += 1
        } else {
            pagerCount = contentSize
        }

        if (!pageDatas.isNullOrEmpty()) {
            pageDatas.clear()
        }

        for (i in 0 until pagerCount) {
            val view = GiftDialogItemView(activity)
            view.updateData(gifts, i)
            view.setOnItemClickListener { position, pagePosition, _ ->
                if (position == mLastSelectorPosition) {
                    return@setOnItemClickListener
                }

                val lastBean: GetGfitBean.GiftsBean = gifts[mLastSelectorPosition]
                lastBean.isSelect = false
                gifts[mLastSelectorPosition] = lastBean

                val nowBean: GetGfitBean.GiftsBean = gifts[position]
                nowBean.isSelect = true
                gifts[position] = nowBean

                mLastSelectorPosition = position
                mSelectPosition = position

                if (!pageDatas.isNullOrEmpty() && pageDatas.size >= pagePosition) {
                    pageDatas[pagePosition].updateData(gifts, pagePosition)
                }
            }
            pageDatas.add(view)
        }

        binding?.let {
            it.sendGiftMoney.text = mDiamodAndroid?.toString()

            val adapter = GiftPageAdapter(pageDatas)
            it.sendGiftViewpager.adapter = adapter
            setIndicator(pagerCount, gifts)
        }
    }

    private fun setIndicator(count: Int = 0, gifts: MutableList<GetGfitBean.GiftsBean>?) {
        binding?.let {
            it.sendGiftIndicator.removeAllViews()
            for (i in 0 until count) {
                val view = View(activity)
                view.setBackgroundResource(R.drawable.select_indicator)
                view.isEnabled = false
                val layoutParams = LinearLayout.LayoutParams(20, 20)
                if (0 != i) {
                    layoutParams.leftMargin = 20
                }
                view.layoutParams = layoutParams
                it.sendGiftIndicator.addView(view)
            }

            if (0 != it.sendGiftIndicator.childCount) {
                it.sendGiftIndicator.getChildAt(0).isEnabled = true
            }

            it.sendGiftViewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                }

                override fun onPageSelected(position: Int) {
                    it.sendGiftIndicator.getChildAt(mCurrentItem).isEnabled = false
                    it.sendGiftIndicator.getChildAt(position).isEnabled = true
                    mCurrentItem = position
                    pageDatas[position].updateData(gifts, position)
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        initWidgetSize(Gravity.BOTTOM)
    }
}