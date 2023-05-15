package com.lvs.data.remote.repositories

import com.lvs.data.remote.db.dao.ConversationsDao
import com.lvs.data.remote.db.entities.ConversationEntity
import javax.inject.Inject

class ConversationRepositoryImpl @Inject constructor(
    private val conversationsDao: ConversationsDao,
) : ConversationRepository {

    override fun fetchConversations() = conversationsDao.getAllDesc()

    override fun newConversation(title: String) = conversationsDao.insert(ConversationEntity.InsertionPrototype(title))

    override fun deleteConversation(conversation: ConversationEntity) = conversationsDao.delete(conversation)

}