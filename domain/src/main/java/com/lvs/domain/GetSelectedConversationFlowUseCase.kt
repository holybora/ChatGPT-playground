package com.lvs.domain

import com.lvs.data.remote.db.entities.ConversationEntity
import com.lvs.data.remote.repositories.ConversationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSelectedConversationFlowUseCase @Inject constructor(private val repository: ConversationRepository) {
    operator fun invoke(): Flow<ConversationEntity?> {
        return repository.getSelectedConversation()
    }
}