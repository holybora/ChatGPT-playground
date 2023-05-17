package com.lvs.domain

import com.lvs.data.remote.repositories.ConversationRepository
import javax.inject.Inject

class CreateConversationUseCase @Inject constructor(private val repository: ConversationRepository) {
    operator fun invoke(params: String): Long {
        return repository.newConversation(params)
    }


}