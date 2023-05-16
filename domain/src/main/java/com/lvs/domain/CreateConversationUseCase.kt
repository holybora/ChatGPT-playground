package com.lvs.domain

import com.lvs.data.remote.repositories.ConversationRepository
import javax.inject.Inject

class CreateConversationUseCase @Inject constructor(private val repository: ConversationRepository) :
    CoroutineUseCase<String, Long>() {
    override suspend fun invoke(params: String): Long {
        return repository.newConversation(params)
    }


}