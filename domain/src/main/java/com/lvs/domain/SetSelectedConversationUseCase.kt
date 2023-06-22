package com.lvs.domain

import com.lvs.data.remote.repositories.ConversationRepository
import javax.inject.Inject

class SetSelectedConversationUseCase @Inject constructor(private val repository: ConversationRepository) {
    operator fun invoke(param: Long) {
        return repository.setSelectedConversation(repository.getConversationById(param))
    }
}