package com.qichuang.roomlib.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * author ：Seven
 * date : 9/26/21
 * description :聊天记录表
 */
@Entity(tableName = "chat")
class ChatEntity{

    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
    @ColumnInfo(name = "MESSAGE_ID")
    var messageId: String? = null
    @ColumnInfo(name = "TIME")
    var time: Long? = null
    @ColumnInfo(name = "TEXT")
    var text: String? = null
    @ColumnInfo(name = "TYPE")
    var type: String? = null
    @ColumnInfo(name = "SUB_TYPE")
    var subType: String? = null
    @ColumnInfo(name = "IMAGE_PATH")
    var imagePath: String? = null
    @ColumnInfo(name = "VOICE_PATH")
    var voicePath: String? = null
    @ColumnInfo(name = "VOICE_DURATION")
    var voiceDuration: String? = null
    @ColumnInfo(name = "GIFT_NAME")
    var giftName: String? = null
    @ColumnInfo(name = "GIFT_ANIMATE")
    var giftAnimate: String? = null
    @ColumnInfo(name = "VERSIONS")
    var versions: String? = null
    @ColumnInfo(name = "UID")
    var uid: String? = null
    @ColumnInfo(name = "TO_UID")
    var toUid: String? = null
    @ColumnInfo(name = "IS_SELF")
    var self: String? = null
    @ColumnInfo(name = "MEDIA_ID")
    var mediaId: String? = null
    @ColumnInfo(name = "THUMB_IMAGE_PATH")
    var thumbImagePath: String? = null
    @ColumnInfo(name = "IMAGE_WIDTH")
    var imageWidth: String? = null
    @ColumnInfo(name = "IMAGE_HEIGHT")
    var imageHeight: String? = null
    @ColumnInfo(name = "IS_READ")
    var read: String? = null
    @ColumnInfo(name = "SEND_TIME")
    var sendTime: String? = null
    @ColumnInfo(name = "SEND_STATUS")
    var sendStatus: String? = null

}