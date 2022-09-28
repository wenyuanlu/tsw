package com.maishuo.tingshuohenhaowan.common

/**
 * author : xpSun
 * date : 8/3/21
 *description :
 */
enum class AutoClosePlayEnum(
        val type: Int,
        val value: Long,
        val desc: String
) {
    CLOSE(0, 0, "关闭定时"),
    AUTO_CLOSE_10_MINUTE(1, 10 * 60 * 1000, "10分钟"),
    AUTO_CLOSE_30_MINUTE(2, 30 * 60 * 1000, "30分钟"),
    AUTO_CLOSE_1_HOUR(3, 1 * 60 * 60 * 1000, "1小时"),
    AUTO_CLOSE_2_HOUR(4, 2 * 60 * 60 * 1000, "2小时"),
}