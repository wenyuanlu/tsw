package com.maishuo.tingshuohenhaowan.setting.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.alioss.AliOSSUploadFile.UploadFileCallBack
import com.maishuo.tingshuohenhaowan.alioss.AliOssUtil
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity
import com.maishuo.tingshuohenhaowan.databinding.ActivityHeadImageMakingBinding
import com.maishuo.tingshuohenhaowan.api.response.HeadImageMakingBean
import com.maishuo.tingshuohenhaowan.ui.activity.SelectPicsActivity
import com.qichuang.commonlibs.common.PreferencesKey
import com.qichuang.commonlibs.utils.GlideUtils
import com.qichuang.commonlibs.utils.Md5
import com.qichuang.commonlibs.utils.PreferencesUtils
import com.qichuang.retrofit.CommonObserver
import java.lang.ref.WeakReference
import java.util.*

/**
 * EXPLAIN: 头像制作
 */
class HeadImageMakingActivity : CustomBaseActivity<ActivityHeadImageMakingBinding>() {

    private var comicauator: String? = null
    private var acType = 1 //是否设置动漫头像
    private var mHandler: Handler? = null

    //设置重新选择
    private fun setReselect(type: Int) {
        vb?.let {
            when (type) {
                0 -> {
                    it.createdHeadTvBtnNormal.visibility = View.VISIBLE
                    it.createdHeadTvBtnComic.visibility = View.VISIBLE
                    it.createdHeadTvBtnUsed.visibility = View.GONE
                    it.createdHeadTvBtnSelect.visibility = View.GONE
                    it.createdHeadTvLoadingText.visibility = View.GONE
                }
                1 -> {
                    it.createdHeadTvBtnNormal.visibility = View.GONE
                    it.createdHeadTvBtnComic.visibility = View.GONE
                    it.createdHeadTvBtnUsed.visibility = View.VISIBLE
                    it.createdHeadTvBtnSelect.visibility = View.VISIBLE
                    it.createdHeadTvLoadingText.visibility = View.GONE
                }
                2 -> {
                    it.createdHeadTvBtnNormal.visibility = View.GONE
                    it.createdHeadTvBtnComic.visibility = View.GONE
                    it.createdHeadTvBtnUsed.visibility = View.GONE
                    it.createdHeadTvBtnSelect.visibility = View.GONE
                    it.createdHeadTvLoadingText.visibility = View.VISIBLE
                    if (acType == 1) {
                        it.createdHeadTvLoadingText.text = "正在处理中..."
                    }
                }
                else -> {
                }
            }
        }
    }

    @SuppressLint("HandlerLeak")
    inner class MyHandler internal constructor(activity: Activity) : Handler() {
        private var mActivityReference: WeakReference<Activity> = WeakReference(activity)
        override fun handleMessage(msg: Message) {
            val mActivity = mActivityReference.get()
            if (mActivity != null && !mActivity.isFinishing) {
                when (msg.what) {
                    1 -> {
                        imageUrlFromService()
                    }
                    2 -> {
                        vb?.let {
                            comicauator = msg.obj as String
                            if (!TextUtils.isEmpty(comicauator)) {
                                GlideUtils.initImageForHead(
                                        mActivity,
                                        comicauator,
                                        R.mipmap.home_head_pic_default,
                                        it.createdHeadCvPersonalHead
                                )
                            }
                            PreferencesUtils.putString(PreferencesKey.USER_TEMP_AVATOR, comicauator)
                            it.createdHeadAnimationMaking.pauseAnimation()
                            it.createdHeadAnimationMaking.visibility = View.GONE
                            it.createdHeadCvPersonalHead.visibility = View.VISIBLE
                            setReselect(1)
                        }
                    }
                }
            }
        }
    }

    override fun initView() {
        vb?.createdHeadTvTitle?.text = getString(R.string.head_portrait)
        mHandler = MyHandler(this)
        setReselect(0)
    }

    override fun initData() {
        vb?.let {
            it.root.setPadding(0, ImmersionBar.getStatusBarHeight(this), 0, 0)
            it.createdHeadAnimationHoneCenter.imageAssetsFolder = "images/"
            it.createdHeadAnimationHoneCenter.setAnimation("sstar.json")
            it.createdHeadAnimationHoneCenter.progress = 0f
            it.createdHeadAnimationHoneCenter.repeatCount = Int.MAX_VALUE
            it.createdHeadAnimationHoneCenter.playAnimation()
            it.createdHeadAnimationMaking.imageAssetsFolder = "images/"
            it.createdHeadAnimationMaking.setAnimation("qiehuan.json")
            it.createdHeadAnimationMaking.progress = 0f
            it.createdHeadAnimationMaking.repeatCount = Int.MAX_VALUE
            val auator = fetchAuator()
            if (!TextUtils.isEmpty(auator)) {
                GlideUtils.initImageForHead(
                        context,
                        auator,
                        R.mipmap.home_head_pic_default,
                        it.createdHeadCvPersonalHead
                )
            }
            it.createdHeadTvBtnComic.setOnClickListener(this)
            it.createdHeadTvBtnNormal.setOnClickListener(this)
            it.createdHeadIvBack.setOnClickListener(this)
            it.createdHeadTvBtnUsed.setOnClickListener(this)
            it.createdHeadTvBtnSelect.setOnClickListener(this)
        }
    }

    private fun fetchAuator(): String? {
        val auator = PreferencesUtils.getString(PreferencesKey.USER_AVATOR)
        val oldAuator = PreferencesUtils.getString(PreferencesKey.USER_TEMP_AVATOR)

        //优先判断临时数据
        if (!TextUtils.isEmpty(oldAuator)) {
            return oldAuator
        }
        return if (!TextUtils.isEmpty(auator)) {
            auator
        } else null
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.created_head_tv_btn_normal -> {
                acType = 1
                toSelectPics()
            }
            R.id.created_head_tv_btn_comic -> {
                acType = 2
                toSelectPics()
            }
            R.id.created_head_iv_back -> finish()
            R.id.created_head_tv_btn_select -> setReselect(0)
            R.id.created_head_tv_btn_used -> {
                val intent = Intent()
                intent.putExtra("auator", comicauator)
                setResult(RESULT_OK, intent)
                finish()
            }
            else -> {
            }
        }
    }

    override fun onResume() {
        super.onResume()

        vb?.let {
            if (!it.createdHeadAnimationHoneCenter.isAnimating) {
                it.createdHeadAnimationHoneCenter.playAnimation()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        vb?.let {
            if (it.createdHeadAnimationHoneCenter.isAnimating) {
                it.createdHeadAnimationHoneCenter.pauseAnimation()
            }
            if (it.createdHeadAnimationMaking.isAnimating) {
                it.createdHeadAnimationMaking.pauseAnimation()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        vb?.let {
            if (it.createdHeadAnimationHoneCenter.isAnimating) {
                it.createdHeadAnimationHoneCenter.cancelAnimation()
            }
            if (it.createdHeadAnimationMaking.isAnimating) {
                it.createdHeadAnimationMaking.cancelAnimation()
            }
        }
    }

    //去选择照片页面
    private fun toSelectPics() {
        val intent = Intent(this, SelectPicsActivity::class.java)
        intent.putExtra(SelectPicsActivity.SELECT_COUNT, 1) //选择数量
        intent.putExtra(SelectPicsActivity.SHOW_CAMERA, true) //是否有拍照,默认false
        intent.putExtra(SelectPicsActivity.ENABLE_CROP, true) //是否裁剪,默认false
        intent.putExtra(SelectPicsActivity.ENABLE_PREVIEW, true) //是否预览,默认false
        intent.putExtra(SelectPicsActivity.SINGLE_BACK, true) //是否单选直接返回,默认false
        intent.putExtra(SelectPicsActivity.NEED_THUMB, true) //是否需要缩略图,默认false
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK) {
            if (data != null) {
                val stringMap: Map<String, String> = data.getSerializableExtra(SelectPicsActivity.COMPRESS_SINGLE_PATHS) as Map<String, String>
                val path: String? = stringMap["path"]

                vb?.let {
                    if (!TextUtils.isEmpty(path)) {
                        GlideUtils.initImageForHead(
                                context,
                                path,
                                R.mipmap.home_head_pic_default,
                                it.createdHeadCvPersonalHead
                        )
                    }
                    
                    if (!it.createdHeadAnimationMaking.isAnimating) {
                        //只在制作动漫图时才显示动画
                        if (acType == 2) {
                            it.createdHeadAnimationMaking.playAnimation()
                            it.createdHeadAnimationMaking.visibility = View.VISIBLE
                            it.createdHeadCvPersonalHead.visibility = View.GONE
                        }
                    }
                    setReselect(2)
                    setUploadFileParams(path)
                }
            }
        }
    }

    private fun setUploadFileParams(imagePath: String?) {
        val userId = PreferencesUtils.getString(PreferencesKey.USER_ID)
        if (TextUtils.isEmpty(userId)) {
            return
        }
        val ca = Calendar.getInstance()
        val year = ca[Calendar.YEAR] //年份数值
        val nowYear = year.toString() + ""
        val times = (System.currentTimeMillis() * 1000).toString() + ""
        val names = Md5.getResult(userId + times) + ".jpg"
        val name = "users/$userId/images/$nowYear/$names"
        AliOssUtil.getInstance().uploadHeadPicture(this, userId, name, imagePath, acType.toString() + "",
                object : UploadFileCallBack {
                    override fun onSucceed() {
                        val message = mHandler!!.obtainMessage()
                        message.what = 1
                        mHandler?.sendMessageDelayed(message, 1500)
                    }

                    override fun onFail() {}
                })
    }

    private fun imageUrlFromService() {
        ApiService.instance
                .comicResult()
                .subscribe(object : CommonObserver<HeadImageMakingBean>() {
                    override fun onResponseSuccess(response: HeadImageMakingBean?) {
                        val status = response!!.status
                        if (status == 1) {
                            val auator = response.avatar
                            val message = mHandler!!.obtainMessage()
                            message.what = 2
                            message.obj = auator
                            mHandler!!.sendMessageDelayed(message, 1000)
                        } else {
                            val message = mHandler!!.obtainMessage()
                            message.what = 1
                            mHandler!!.sendMessageDelayed(message, 1000)
                        }
                    }
                })
    }
}