package com.maishuo.sharelibrary

interface OnShareRequestListener {

    fun onComplete(share_media: SHARE_MEDIA?)

    fun onError(share_media: SHARE_MEDIA?, throwable: Throwable?)

    fun onCancel(share_media: SHARE_MEDIA?)

    fun onShareMyCircle()

    fun onShareReport()

    fun onShareDownLoad()

    fun onShareCancel()

    fun onDeleteListener()

}