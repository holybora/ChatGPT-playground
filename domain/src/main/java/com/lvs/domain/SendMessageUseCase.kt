package com.lvs.domain

import com.lvs.data.remote.api.enities.MessageDto
import com.lvs.data.remote.common.GPTModel
import com.lvs.data.remote.common.GPTRole
import com.lvs.data.remote.db.entities.MessageEntity
import com.lvs.data.remote.repositories.MessageRepository
import com.lvs.data.remote.repositories.OpenAIRepository
import javax.inject.Inject

class SendMessageToChatGPTUseCase @Inject constructor(
    private val messagesRepository: MessageRepository,
    private val openAIRepository: OpenAIRepository
) :
    CoroutineUseCase<SendMessageToChatGPTUseCase.Params, List<MessageEntity>>() {

    override suspend fun invoke(params: Params): List<MessageEntity> {

        val messageId = messagesRepository.insertMessage(
            MessageEntity.InsertionPrototype(
                params.conversationId,
                params.textToSend,
                GPTRole.USER.value
            )
        )

        val newMessage = messagesRepository.getMessageById(messageId)

        requireNotNull(newMessage) { "TODO: Handle DB exceptions" }

        //TODO: replace on converters
        val response =
            openAIRepository.textCompletionsWithStream(
                model = GPTModel.GPT35TURBO,
                messages = params.messagesForContext
                    .asReversed()
                    .toMutableList()
                    .also { it.add(newMessage) }
                    .map {
                        MessageDto(
                            role = it.role,
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

        return messagesRepository.getMessages(params.conversationId)

    }

    data class Params(val textToSend: String, val messagesForContext: List<MessageEntity>, val conversationId: Long)

}