package com.lvs.domain

import com.lvs.data.remote.db.entities.ConversationEntity
import com.lvs.data.remote.repositories.ConversationRepository
import javax.inject.Inject

class SetSelectedConversationUseCase @Inject constructor(private val repository: ConversationRepository) {
    operator fun invoke(param: ConversationEntity?) {
        return repository.setSelectedConversation(param)
    }
}