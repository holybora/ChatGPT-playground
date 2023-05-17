package com.lvs.domain

import com.lvs.data.remote.db.entities.ConversationEntity
import com.lvs.data.remote.db.entities.MessageEntity
import com.lvs.data.remote.repositories.MessageRepository
import javax.inject.Inject

class GetMessagesByConversationIdUseCase @Inject constructor(private val messageRepository: MessageRepository) {
    operator fun invoke(params: Long): List<MessageEntity> =
        if (params == ConversationEntity.DEFAULT_CONVERSATION_ID) emptyList()
        else messageRepository.getMessages(params)

}