package com.lvs.data.repositories

import com.lvs.data.remote.db.dao.ConversationsDao
import com.lvs.data.remote.db.entities.ConversationEntity
import javax.inject.Inject

class ConversationRepositoryImpl @Inject constructor(
    private val conversationsDao: ConversationsDao,
) : ConversationRepository {

    private val TAG = ConversationRepositoryImpl::class.java.name

    override fun getConversationsFlow() = conversationsDao.getAllDesc()
    override fun getConversations(): List<ConversationEntity> = conversationsDao.getAll()
    override fun getConversationById(conversationId: Long): ConversationEntity? =
        conversationsDao.getConversationById(conversationId)

    override fun newConversation(title: String) = conversationsDao.insert(ConversationEntity.InsertionPrototype(title))
    override fun deleteConversation(conversation: ConversationEntity) = conversationsDao.delete(conversation)
    override fun updateConversation(conversation: ConversationEntity) = conversationsDao.upsert(conversation)

}