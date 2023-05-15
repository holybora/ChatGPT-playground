package com.lvs.data.remote.repositories

import com.lvs.data.remote.db.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    fun getAll(): Flow<List<MessageEntity>>
    fun fetchMessages(conversationId: Long): Flow<List<MessageEntity>>
    fun createMessage(message: MessageEntity.InsertionPrototype) :Long
    fun deleteMessage(message: MessageEntity)
}