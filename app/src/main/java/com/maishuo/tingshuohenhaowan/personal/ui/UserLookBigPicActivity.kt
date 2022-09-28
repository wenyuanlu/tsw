package com.maishuo.tingshuohenhaowan.personal.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity
import com.maishuo.tingshuohenhaowan.databinding.ActivityUserLookBigPicLayoutBinding
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackListener
import com.maishuo.tingshuohenhaowan.utils.DialogUtils
import com.maishuo.tingshuohenhaowan.utils.permission.PermissionSettingPage
import com.maishuo.tingshuohenhaowan.utils.permission.PermissionUtil
import com.maishuo.tingshuohenhaowan.utils.permission.PermissionUtil.PermissionCheckCallBack
import com.qichuang.commonlibs.utils.GlideUtils

/**
 * author : xpSun
 * date : 9/24/21
 * description :
 */
class UserLookBigPicActivity : CustomBaseActivity<ActivityUserLookBigPicLayoutBinding>() {

    companion object {
        private const val PIC_TITLE_TAG: String = "pic_title_tag"
        private const val PIC_URL_TAG: String = "pic_url_tag"

        fun start(context: Context, url: String?, title: String? = null) {
            val intent = Intent(context, UserLookBigPicActivity::class.java)
            intent.putExtra(PIC_TITLE_TAG, title)
            intent.putExtra(PIC_URL_TAG, url)
            context.startActivity(intent)
        }
    }

    private var url: String? = null

    override fun initView() {
        if (intent.hasExtra(PIC_TITLE_TAG)) {
            val title = intent.getStringExtra(PIC_TITLE_TAG)

            setTitle(if (!TextUtils.isEmpty(title)) {
                title
            } else {
                "查看大图"
            })
        }

        if (intent.hasExtra(PIC_URL_TAG)) {
            url = intent.getStringExtra(PIC_URL_TAG)

            if (!TextUtils.isEmpty(url)) {
                vb?.let {
                    GlideUtils.loadImage(this, url, it.userLookBigPicView)
                }
            }
        }

        vb?.userLookBigPicSave?.setOnClickListener {
            if (TextUtils.isEmpty(url)) {
                return@setOnClickListener
            }

            PermissionUtil.checkPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    object : PermissionCheckCallBack {
                        override fun onHasPermission() {
                            GlideUtils.saveNetworkFile(
                                    this@UserLookBigPicActivity,
                                    url,
                                    System.currentTimeMillis().toString()
                            )
                        }

                        override fun onUserHasAlreadyTurnedDown(vararg permission: String) {
                            userApplyRecorderPermission()
                        }

                        override fun onUserHasAlreadyTurnedDownAndDontAsk(vararg permission: String) {
                            jumpSettingActivity()
                        }
                    })
        }
    }

    override fun initData() {

    }

    private fun userApplyRecorderPermission() {
        DialogUtils.showCommonCustomDialog(
                this,
                getString(R.string.apply_read_writ_permission_tip_title),
                getString(R.string.apply_save_permission_tip_message),
                object : OnDialogBackListener {
                    override fun onSure(content: String) {
                        PermissionUtil.checkAndRequestMorePermissions(this@UserLookBigPicActivity,
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1) {

                        }
                    }

                    override fun onCancel() {}
                })
    }

    private fun jumpSettingActivity() {
        DialogUtils.showCommonCustomDialog(
                this,
                getString(R.string.not_apply_permission_tip_title),
                getString(R.string.not_apply_permission_tip_message),
                object : OnDialogBackListener {
                    override fun onSure(content: String) {
                        PermissionSettingPage.start(this@UserLookBigPicActivity)
                    }

                    override fun onCancel() {}
                }
        )
    }

}