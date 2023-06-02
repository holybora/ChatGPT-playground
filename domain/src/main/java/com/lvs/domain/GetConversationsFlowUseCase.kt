package com.lvs.domain

import com.lvs.data.remote.repositories.ConversationRepository
import javax.inject.Inject

class GetConversationsFlowUseCase @Inject constructor(private val conversationRepository: ConversationRepository) {

    operator fun invoke() = conversationRepository.getConversationsFlow()
}