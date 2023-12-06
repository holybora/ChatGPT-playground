package com.lvs.domain

import com.lvs.data.remote.db.entities.ConversationEntity
import com.lvs.data.remote.repositories.ConversationRepository
import javax.inject.Inject

class CreateConversationUseCase @Inject constructor(private val repository: ConversationRepository) {
    operator fun invoke(params: String): ConversationEntity {
        val convId = repository.newConversation(params)
        repository.setSelectedConversation(repository.getConversationById(convId))
        return repository.getConversationById(convId)
            ?: throw IllegalStateException("Conversation wasn't created with id: $convId")
    }


}