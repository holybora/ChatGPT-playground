package com.lvs.data.remote.repositories

import com.lvs.data.remote.db.entities.ConversationEntity
import kotlinx.coroutines.flow.Flow


interface ConversationRepository {
    fun getConversationsFlow(): Flow<List<ConversationEntity>>
    fun getConversations(): List<ConversationEntity>
    fun newConversation(title: String): Long
    fun deleteConversation(conversation: ConversationEntity)
}