package com.lvs.data.remote.repositories

import com.lvs.data.remote.db.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun fetchMessages(conversationId: String): Flow<List<MessageEntity>>
    fun createMessage(message: MessageEntity.InsertionPrototype)
    fun deleteMessage(message: MessageEntity)
}