package com.maishuo.sharelibrary

enum class SHARE_MEDIA {
    SINA,
    QZONE,
    QQ,
    WEIXIN,
    WEIXIN_CIRCLE,
    WEIXIN_FAVORITE;

    override fun toString(): String {
        return super.toString()
    }

    fun getName(): String {
        return if (this.toString() == "WEIXIN") {
            "wxsession"
        } else if (this.toString() == "WEIXIN_CIRCLE") {
            "wxtimeline"
        } else {
            if (this.toString() == "WEIXIN_FAVORITE") {
                "wxfavorite"
            } else {
                this.toString().toLowerCase()
            }
        }
    }
}