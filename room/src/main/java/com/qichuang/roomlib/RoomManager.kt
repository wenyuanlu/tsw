package com.qichuang.roomlib

import android.annotation.SuppressLint
import android.content.Context
import com.qichuang.roomlib.database.AppDatabase
import com.qichuang.roomlib.entity.AgentEntity
import com.qichuang.roomlib.entity.ChatEntity

/**
 * author ：Seven
 * date : 9/22/21
 * description :room数据库管理类
 */
@SuppressLint("StaticFieldLeak")
class RoomManager {

    companion object {
        private var instance: RoomManager? = null
        private var appDatabase: AppDatabase? = null
        fun getInstance(context: Context): RoomManager {
            if (instance == null) {
                instance = RoomManager()
                appDatabase = AppDatabase.getInstance(context)
            }
            return instance as RoomManager
        }
    }

    /**
     * 统计表-插入
     */
//    fun insertAgent(agentEntity: AgentEntity) {
//        appDatabase?.agentDao()?.insert(agentEntity)
//    }
//
//    /**
//     * 统计表-查询所有
//     */
//    fun loadAgent(agentKey: String): AgentEntity? {
//        return appDatabase?.agentDao()?.loadByKey(agentKey)
//    }
//
//    /**
//     * 统计表-查询所有
//     */
//    fun loadAllAgent(): List<AgentEntity>? {
//        return appDatabase?.agentDao()?.loadAll()
//    }

    /**
     * 聊天表-查询所有
     */
    fun loadAllChat(): List<ChatEntity>? {
        return appDatabase?.chatDao()?.loadAll()
    }

}