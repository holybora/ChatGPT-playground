package com.lvs.data.repositories

import com.lvs.data.remote.db.entities.MessageEntity
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    fun getAll(): Flow<List<MessageEntity>>
    fun getMessagesFlow(conversationId: Long): Flow<List<MessageEntity>>
    fun getMessages(conversationId: Long): List<MessageEntity>
    fun insertMessage(message: MessageEntity.InsertionPrototype): Long
    fun getMessageById(id: Long) : MessageEntity?
    fun deleteMessage(message: MessageEntity)
}