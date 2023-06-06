package com.lvs.domain

import com.lvs.data.converters.MessagesDataToUiConverter
import com.lvs.data.local.main.MessageUiEntity
import com.lvs.data.remote.api.enities.MessageDto
import com.lvs.data.remote.common.GPTModel
import com.lvs.data.remote.common.GPTRole
import com.lvs.data.remote.db.entities.MessageEntity
import com.lvs.data.remote.repositories.ConversationRepository
import com.lvs.data.remote.repositories.MessageRepository
import com.lvs.data.remote.repositories.OpenAIRepository
import java.lang.IllegalStateException
import javax.inject.Inject

class SendMessageToChatGPTUseCase @Inject constructor(
    private val conversationsRepository: ConversationRepository,
    private val messagesRepository: MessageRepository,
    private val openAIRepository: OpenAIRepository,
    private val messagesDataToUiConverter: MessagesDataToUiConverter
) {

    suspend operator fun invoke(params: Params): List<MessageUiEntity> {

        if (params.messagesForContext.isEmpty()) {
            val conversation = conversationsRepository.getConversationById(params.conversationId)
                ?: throw IllegalStateException("Conversation with id = ${params.conversationId} not found")

            conversationsRepository.updateConversation(conversation.copy(title = params.messagesForContext.first().text))
        }

        //TODO: replace on converters
        val response =
            openAIRepository.textCompletionsWithStream(
                model = GPTModel.GPT35TURBO,
                messages = params.messagesForContext
                    .asReversed()
                    .toMutableList()
                    .map {
                        MessageDto(
                            role = it.role.value,
                            content = it.text
                        )
                    })
        //TODO: think how better choose response message
        val responseMessage = response.choices.first().message

        messagesRepository.insertMessage(
            MessageEntity.InsertionPrototype(
                conversationId = params.conversationId,
                text = responseMessage.content,
                role = responseMessage.role
            )
        )

        return messagesRepository
            .getMessages(params.conversationId)
            .map { messagesDataToUiConverter.convert(it) }

    }

    data class Params(
        val messagesForContext: List<MessageUiEntity>,
        val conversationId: Long
    )

}