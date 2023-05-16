package com.lvs.domain

import com.lvs.data.remote.db.entities.ConversationEntity
import com.lvs.data.remote.repositories.ConversationRepository
import javax.inject.Inject

class GetConversationUseCase @Inject constructor(private val repository: ConversationRepository) :
    CoroutineUseCase<Unit, List<ConversationEntity>>() {
    override suspend fun invoke(params: Unit) = repository.getConversations()

}
