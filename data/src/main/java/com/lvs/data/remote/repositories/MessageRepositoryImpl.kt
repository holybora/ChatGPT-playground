package com.lvs.data.remote.repositories

import com.lvs.data.remote.db.dao.MessagesDao
import com.lvs.data.remote.db.entities.MessageEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messagesDao: MessagesDao
) : MessageRepository {
    override fun getAll(): Flow<List<MessageEntity>> = messagesDao.getAll()

    override fun fetchMessages(conversationId: Long): Flow<List<MessageEntity>> =
        messagesDao.getAllByConversationId(conversationId)

    override fun createMessage(message: MessageEntity.InsertionPrototype): Long {
        return messagesDao.insert(message)
    }

    override fun deleteMessage(message: MessageEntity) =
        messagesDao.deleteAll(message)

}