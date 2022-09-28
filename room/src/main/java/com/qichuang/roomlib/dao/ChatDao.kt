package com.qichuang.roomlib.dao

import android.database.Cursor
import androidx.room.*
import com.qichuang.roomlib.entity.AgentEntity
import com.qichuang.roomlib.entity.ChatEntity


/**
 * author ：Seven
 * date : 9/26/21
 * description :聊天表增删改查
 */
@Dao
interface ChatDao {

    @Insert
    fun insert(vararg entity: ChatEntity)

    @Delete
    fun delete(entity: ChatEntity)

    @Query("SELECT * FROM chat")
    fun loadAll(): List<ChatEntity>

}