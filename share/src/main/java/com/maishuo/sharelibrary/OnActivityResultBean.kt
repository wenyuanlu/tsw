package com.maishuo.sharelibrary

import android.content.Intent

/**
 * @author Created by SXF on 2021/9/17 3:48 PM.
 * @description 分享回调类
 */
data class OnActivityResultBean(
        var requestCode: Int? = null,
        var resultCode: Int? = null,
        var data: Intent? = null
)