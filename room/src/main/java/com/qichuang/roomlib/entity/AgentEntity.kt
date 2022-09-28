package com.qichuang.roomlib.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * author ：Seven
 * date : 9/18/21
 * description :埋点表
 */
@Entity(tableName = "agent")
class AgentEntity{

    @PrimaryKey(autoGenerate = true)
    var agentId:Int = 0

    @ColumnInfo(name = "agentKey")
    var agentKey: String? = null

    @ColumnInfo(name = "agentValue")
    var agentValue: String? = null

    @ColumnInfo(name = "timestamp")
    var timestamp: Long? = null

}