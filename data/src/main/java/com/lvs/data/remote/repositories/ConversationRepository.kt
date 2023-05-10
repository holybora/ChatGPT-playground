package com.lvs.data.remote.repositories

import com.lvs.data.remote.db.entities.ConversationEntity
import kotlinx.coroutines.flow.Flow


interface ConversationRepository {
    suspend fun fetchConversations(): Flow<List<ConversationEntity>>
    fun newConversation(title: String)
    fun deleteConversation(conversation: ConversationEntity)
}