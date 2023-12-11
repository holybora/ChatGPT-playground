package com.lvs.domain

import com.lvs.data.converters.MessagesDataToUiConverter
import com.lvs.data.local.main.MessageUiEntity
import com.lvs.data.remote.common.GPTRole
import com.lvs.data.remote.db.entities.MessageEntity
import com.lvs.data.repositories.MessageRepository
import javax.inject.Inject

class InsertMessageUseCase @Inject constructor(
    private val messagesRepository: MessageRepository,
    private val messagesDataToUiConverter: MessagesDataToUiConverter
) {

    suspend operator fun invoke(params: Params): MessageUiEntity {

        val messageId = messagesRepository.insertMessage(
            MessageEntity.InsertionPrototype(
                params.conversationId,
                params.textToSend,
                GPTRole.USER.value
            )
        )

        val newMessage = messagesRepository.getMessageById(messageId)

        requireNotNull(newMessage) { "TODO: Handle DB exceptions" }

        return messagesDataToUiConverter.convert(newMessage)


    }

    data class Params(val textToSend: String, val conversationId: Long)

}