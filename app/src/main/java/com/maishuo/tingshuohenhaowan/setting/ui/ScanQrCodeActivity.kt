package com.maishuo.tingshuohenhaowan.setting.ui

import android.Manifest
import android.os.Bundle
import android.os.PersistableBundle
import android.view.KeyEvent
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView.TorchListener
import com.maishuo.tingshuohenhaowan.R
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity
import com.maishuo.tingshuohenhaowan.databinding.ActivityScanQrCodeLayoutBinding
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackListener
import com.maishuo.tingshuohenhaowan.utils.DialogUtils
import com.maishuo.tingshuohenhaowan.utils.permission.PermissionSettingPage
import com.maishuo.tingshuohenhaowan.utils.permission.PermissionUtil


/**
 * author : xpSun
 * date : 9/14/21
 * description : 二维码扫码界面。
 */
class ScanQrCodeActivity : CustomBaseActivity<ActivityScanQrCodeLayoutBinding>(), TorchListener {

    companion object {
        private const val PERMISSION_REQUEST_CODE: Int = 0x1001
    }

    private var captureManager: CaptureManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vb?.let {
            //重要代码，初始化捕获
            captureManager = CaptureManager(this, it.decoratedBarcodeView)
            captureManager?.initializeFromIntent(intent, savedInstanceState)
            captureManager?.decode()
            it.decoratedBarcodeView.setTorchListener(this)
        }

        PermissionUtil.checkPermission(
                this,
                Manifest.permission.CAMERA,
                object : PermissionUtil.PermissionCheckCallBack {
                    override fun onHasPermission() {

                    }

                    override fun onUserHasAlreadyTurnedDown(vararg permission: String?) {
                        DialogUtils.showCommonCustomDialog(this@ScanQrCodeActivity,
                                getString(R.string.real_user_auth_tip_title),
                                getString(R.string.apply_camera_auth_tip_message),
                                object : OnDialogBackListener {
                                    override fun onSure(content: String) {
                                        PermissionUtil.requestPermission(
                                                this@ScanQrCodeActivity,
                                                Manifest.permission.CAMERA,
                                                PERMISSION_REQUEST_CODE)
                                    }

                                    override fun onCancel() {
                                        finish()
                                    }
                                })
                    }

                    override fun onUserHasAlreadyTurnedDownAndDontAsk(vararg permission: String?) {
                        DialogUtils.showCommonCustomDialog(
                                this@ScanQrCodeActivity,
                                getString(R.string.not_apply_permission_tip_title),
                                getString(R.string.not_apply_permission_tip_message),
                                object : OnDialogBackListener {
                                    override fun onSure(content: String) {
                                        PermissionSettingPage.start(this@ScanQrCodeActivity)
                                    }

                                    override fun onCancel() {
                                        finish()
                                    }
                                }
                        )
                    }
                })
    }

    override fun initView() {
        setTitle("扫描二维码")
    }

    override fun initData() {}

    override fun onPointerCaptureChanged(hasCapture: Boolean) {}

    override fun onTorchOn() {}

    override fun onTorchOff() {}

    override fun onPause() {
        super.onPause()
        captureManager?.onPause()
    }

    override fun onResume() {
        super.onResume()
        captureManager?.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        captureManager?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        captureManager?.onSaveInstanceState(outState)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return vb?.decoratedBarcodeView?.onKeyDown(keyCode, event) ?: false
                || super.onKeyDown(keyCode, event)
    }
}