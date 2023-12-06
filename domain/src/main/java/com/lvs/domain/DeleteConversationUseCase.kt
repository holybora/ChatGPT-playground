package com.lvs.domain

import com.lvs.data.remote.repositories.ConversationRepository
import javax.inject.Inject

class DeleteConversationUseCase @Inject constructor(private val conversationRepository: ConversationRepository) {

    operator fun invoke(conversationId: Long) {
        val conversationInDb = conversationRepository.getConversationById(conversationId)
            ?: throw ConversationNonExistException(conversationId)

        conversationRepository.deleteConversation(conversationInDb)
    }

    class ConversationNonExistException(conversationId: Long) :
        IllegalArgumentException("No conversation with id: $conversationId")
}


