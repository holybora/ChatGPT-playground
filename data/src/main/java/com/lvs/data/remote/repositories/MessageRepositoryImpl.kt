package com.lvs.data.remote.repositories

import com.lvs.data.remote.db.dao.MessagesDao
import com.lvs.data.remote.db.entities.MessageEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messagesDao: MessagesDao
) : MessageRepository {
    override fun getAll(): Flow<List<MessageEntity>> = messagesDao.getAll()

    override fun getMessagesFlow(conversationId: Long): Flow<List<MessageEntity>> =
        messagesDao.getAllByConversationIdFlow(conversationId)

    override fun getMessages(conversationId: Long): List<MessageEntity> =
        messagesDao.getAllByConversationId(conversationId)

    override fun insertMessage(message: MessageEntity.InsertionPrototype): Long {
        return messagesDao.insert(message)
    }

    override fun getMessageById(id: Long): MessageEntity? {
        return messagesDao.getMessageById(id)
    }

    override fun deleteMessage(message: MessageEntity) =
        messagesDao.deleteAll(message)

}