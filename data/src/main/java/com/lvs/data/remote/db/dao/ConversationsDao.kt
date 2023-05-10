package com.lvs.data.remote.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lvs.data.remote.db.entities.ConversationEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ConversationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = ConversationEntity::class)
    fun insert(conversation: ConversationEntity.InsertionPrototype)

    @Delete
    fun delete(conversation: ConversationEntity)

    @Query("SELECT * FROM conversations")
    fun getAll(): Flow<List<ConversationEntity>>

    @Query("SELECT * FROM conversations ORDER BY created_at DESC")
    fun getAllDesc(): Flow<List<ConversationEntity>>


}