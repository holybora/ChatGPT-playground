package com.lvs.data.remote.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lvs.data.remote.db.entities.MessageEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface MessagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = MessageEntity::class)
    fun insert(message: MessageEntity.InsertionPrototype)

    @Delete
    fun deleteAll(message: MessageEntity)

    @Query("SELECT * FROM messages")
    fun getAll(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE conversation_id == :conversationId ORDER BY created_at DESC")
    fun getAllByConversationId(conversationId: String): Flow<List<MessageEntity>>


}