package com.lvs.data.repositories

import com.lvs.data.remote.db.entities.ConversationEntity
import kotlinx.coroutines.flow.Flow


interface ConversationRepository {
    fun getConversationsFlow(): Flow<List<ConversationEntity>>
    fun getConversations(): List<ConversationEntity>
    fun getConversationById(conversationId: Long): ConversationEntity?
    fun newConversation(title: String): Long
    fun deleteConversation(conversation: ConversationEntity)
    fun updateConversation(conversation: ConversationEntity): Long
}