package com.maishuo.sharelibrary

import android.graphics.Bitmap
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.maishuo.sharelibrary.databinding.DialogShareLayoutNewBinding
import com.qichuang.commonlibs.basic.BaseDialog
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * author : xpSun
 * date : 9/18/21
 * description :
 */
class CustomShareDialog constructor(activity: AppCompatActivity) : BaseDialog(activity) {

    private var binding: DialogShareLayoutNewBinding? = null
    private var customShareUtils: CustomShareUtils? = null

    var onShareRequestListener: OnShareRequestListener? = null
        set(value) {
            field = value
            onQQShareListener = OnQQShareListener(onShareRequestListener)
            onQZoneShareListener = OnQZoneShareListener(onShareRequestListener)

            customShareUtils?.onShareRequestListener = onShareRequestListener
        }

    var defaultText: String? = null
    var bitmap: Bitmap? = null

    var thumbImageUrl: String? = null
    var shareUrl: String? = null
    var title: String? = null
    var desc: String? = null
    var type: Int? = null

    var isShowDelete: Boolean = false
        set(value) {
            field = value
            binding?.delLayout?.visibility =  if (isShowDelete) View.VISIBLE else View.GONE
        }

    var backGroundColor: Int? = null
        set(value) {
            field = value
            if (null != backGroundColor && 0 != backGroundColor) {
                binding?.root?.setBackgroundColor(backGroundColor!!)
            }
        }

    var isShowCancel: Boolean = false
        set(value) {
            field = value
            binding?.divider?.visibility = if (isShowCancel) View.VISIBLE else View.GONE
            binding?.shareTvCancel?.visibility = if (isShowCancel) View.VISIBLE else View.GONE
        }

    private var onQQShareListener: OnQQShareListener? = null
    private var onQZoneShareListener: OnQZoneShareListener? = null

    override fun fetchRootView(): View? {
        binding = DialogShareLayoutNewBinding.inflate(LayoutInflater.from(activity))

        binding?.let {
            it.dialog = this

            EventBus.getDefault().register(this)
            customShareUtils = CustomShareUtils.getInstance()
        }
        return binding?.root
    }

    override fun initWidgets() {
        setGravity(Gravity.BOTTOM)
    }

    fun sendWechatMessage() {
        val mTargetScene = SendMessageToWX.Req.WXSceneSession
        customShareUtils?.sendWeChatMessage(
                shareUrl,
                title,
                desc,
                bitmap,
                mTargetScene
        )
    }

    fun sendWechatMomentsMessage() {
        val mTargetScene = SendMessageToWX.Req.WXSceneTimeline
        customShareUtils?.sendWeChatMessage(
                shareUrl,
                title,
                desc,
                bitmap,
                mTargetScene
        )
    }

    fun sendQQMessage() {
        customShareUtils?.sendQQMessage(
                activity,
                shareUrl,
                desc,
                thumbImageUrl,
                title,
                activity?.packageName ?: "",
                onQQShareListener
        )
    }

    fun sendQZoneMessage() {
        customShareUtils?.sendQQZoneMessage(
                activity,
                title,
                desc,
                shareUrl,
                thumbImageUrl,
                onQZoneShareListener
        )
    }

    fun sendWeiBoMessage() {
        customShareUtils?.sendWeiBoMessage(
                activity, title, desc, bitmap, shareUrl, defaultText
        ) {
            onShareRequestListener?.onError(SHARE_MEDIA.SINA, Throwable("2008"))
        }
    }

    fun onDelListener() {
        onShareRequestListener?.onDeleteListener()
    }

    fun myMoments() {
        onShareRequestListener?.onShareMyCircle()
    }

    fun onReport() {
        onShareRequestListener?.onShareReport()
    }

    fun onDownLoad() {
        onShareRequestListener?.onShareDownLoad()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEventMainThread(baseResp: BaseResp) {
        customShareUtils?.onWechatShareResponse(baseResp)
    }
}