package com.qichuang.roomlib.dao

import android.database.Cursor
import androidx.room.*
import com.qichuang.roomlib.entity.AgentEntity


/**
 * author ：Seven
 * date : 9/18/21
 * description :埋点表增删改查
 */
@Dao
interface AgentDao {

    @Insert
    fun insert(vararg entity: AgentEntity)

    @Delete
    fun delete(entity: AgentEntity)

    @Query("SELECT * FROM agent")
    fun loadAll(): List<AgentEntity>

//    @Query("SELECT * FROM agent WHERE agentId IN (:agentIds)")
//    fun loadAllByIds(agentIds: IntArray): List<AgentEntity>

    @Query("SELECT * FROM agent WHERE agentKey LIKE :agentKey LIMIT 1 ")
    fun loadByKey(agentKey: String): AgentEntity

    @Query("SELECT * FROM agent WHERE agentKey LIKE :agentKey LIMIT 5")
    fun loadToCursor(agentKey: String): Cursor?
}