package com.lvs.chatgpt.ui.chat

import android.util.Log
import com.lvs.chatgpt.base.BaseViewModel
import com.lvs.data.remote.db.entities.ConversationEntity.Companion.DEFAULT_CONVERSATION_ID
import com.lvs.domain.CreateConversationUseCase
import com.lvs.domain.GetConversationsFlowUseCase
import com.lvs.domain.GetMessagesByConversationIdUseCase
import com.lvs.domain.GetSelectedConversationFlowUseCase
import com.lvs.domain.InsertMessageUseCase
import com.lvs.domain.SendMessageToChatGPTUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//TODO: Error handling
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val createConversation: CreateConversationUseCase,
    private val getConversationsFlowUseCase: GetConversationsFlowUseCase,
    private val sendMessageToChatGPT: SendMessageToChatGPTUseCase,
    private val insertMessageUseCase: InsertMessageUseCase,
    private val getMessagesByConversationId: GetMessagesByConversationIdUseCase,
    private val getSelectedConversationFlow: GetSelectedConversationFlowUseCase
) : BaseViewModel<ChatEvent, ChatUiState, ChatEffect>() {

    init {
        launchOnBackground {
            getConversationsFlowUseCase().collect {
                setState { copy(conversations = it) }
            }
        }
        launchOnBackground {
            getSelectedConversationFlow()
                .collect { conversation ->
                    setState {
                        if (conversation == null)
                            copy(
                                messages = emptyList(),
                                selectedConversationId = DEFAULT_CONVERSATION_ID
                            )
                        else copy(
                            messages = getMessagesByConversationId(conversation.id),
                            selectedConversationId = conversation.id
                        )
                    }
                }
        }
    }

    override fun createInitialState(): ChatUiState = ChatUiState()
    override fun handleEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.OnSendMessage -> {

                if (event.message.isBlank()) return

                setState { copy(isFetching = true) }

                launchOnBackground {

                    val actualConversationId =
                        currentState.selectedConversationId
                            .takeIf { currentState.selectedConversationId != DEFAULT_CONVERSATION_ID }
                            ?: runCatching { createConversation(event.message) }
                                .onFailure { handleException(it) }
                                .getOrThrow()


                    val insertedMessage = insertMessageUseCase(
                        InsertMessageUseCase.Params(
                            textToSend = event.message,
                            conversationId = actualConversationId
                        )
                    )

                    setState { copy(messages = listOf(insertedMessage) + messages) }

                    val actualMessages = sendMessageToChatGPT(
                        SendMessageToChatGPTUseCase.Params(
                            messagesForContext = currentState.messages.take(3),
                            conversationId = actualConversationId
                        )
                    )

                    setState {
                        copy(
                            isFetching = false,
                            messages = actualMessages,
                            selectedConversationId = actualConversationId
                        )
                    }
                }
            }
        }
    }

    private fun handleException(exception: Throwable) {
        Log.e(TAG, exception.toString())
    }


    companion object {
        val TAG = ChatViewModel::class.simpleName
    }
}
