package com.lvs.domain

import com.lvs.data.repositories.ConversationRepository
import javax.inject.Inject

class SetSelectedNextConversationUseCase @Inject constructor(val conversationRepository: ConversationRepository) {
    suspend operator fun invoke() {
        val conversations = conversationRepository.getConversations()
    }
}