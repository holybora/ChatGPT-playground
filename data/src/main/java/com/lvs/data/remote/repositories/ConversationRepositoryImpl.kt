package com.lvs.data.remote.repositories

import com.lvs.data.remote.db.dao.ConversationsDao
import com.lvs.data.remote.db.entities.ConversationEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ConversationRepositoryImpl @Inject constructor(
    private val conversationsDao: ConversationsDao,
) : ConversationRepository {

    private val selectedConversation: MutableStateFlow<ConversationEntity?> = MutableStateFlow(null)
    override fun setSelectedConversation(conversationEntity: ConversationEntity?) {
        selectedConversation.update { conversationEntity }
    }

    override fun getSelectedConversation(): Flow<ConversationEntity?> = selectedConversation

    override fun getConversationsFlow() = conversationsDao.getAllDesc()
    override fun getConversations(): List<ConversationEntity> = conversationsDao.getAll()
    override fun getConversationById(conversationId: Long): ConversationEntity? =
        conversationsDao.getConversationById(conversationId)

    override fun newConversation(title: String) = conversationsDao.insert(ConversationEntity.InsertionPrototype(title))
    override fun deleteConversation(conversation: ConversationEntity) = conversationsDao.delete(conversation)
    override fun updateConversation(conversation: ConversationEntity) = conversationsDao.upsert(conversation)

}