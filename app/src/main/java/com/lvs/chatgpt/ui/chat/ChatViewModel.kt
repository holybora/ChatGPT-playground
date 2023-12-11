package com.lvs.chatgpt.ui.chat

import androidx.lifecycle.SavedStateHandle
import com.lvs.chatgpt.base.BaseViewModel
import com.lvs.chatgpt.ui.chat.navigation.ChatArgs
import com.lvs.domain.CreateConversationUseCase
import com.lvs.domain.GetConversationsFlowUseCase
import com.lvs.domain.GetMessagesByConversationIdUseCase
import com.lvs.domain.InsertMessageUseCase
import com.lvs.domain.SendMessageToChatGPTUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import javax.inject.Inject

//TODO: Error handling
@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createConversation: CreateConversationUseCase,
    private val getConversationsFlowUseCase: GetConversationsFlowUseCase,
    private val sendMessageToChatGPT: SendMessageToChatGPTUseCase,
    private val insertMessageUseCase: InsertMessageUseCase,
    private val getMessagesByConversationId: GetMessagesByConversationIdUseCase,
) : BaseViewModel<ChatEvent, ChatUiState>() {

    private var onSendMessageJob: Job? = null
    private val chatArgs: ChatArgs = ChatArgs(savedStateHandle)

    init {
        // as collect{} is infinity suspend function from Room we should call it in standalone coroutine builder
        launchOnBackground {
            getConversationsFlowUseCase().collect {
                val convId: Long = checkNotNull(chatArgs.conversationId)
                val conversation = checkNotNull(it.find { it.id == convId })
                if (chatArgs.isNew) {
                    setState {
                        copy(
                            isFetching = true,
                            messages = getMessagesByConversationId(conversation.id)
                        )
                    }

                    val actualMessages = runCatching {
                        sendMessageToChatGPT(
                            SendMessageToChatGPTUseCase.Params(
                                messagesForContext = currentState.messages.take(1),
                                conversationId = convId
                            )
                        )
                    }
                        .onFailure { setState { copy(isFetching = false) } }
                        .getOrThrow()

                    setState {
                        copy(
                            isFetching = false,
                            messages = actualMessages,
                            selectedConversation = conversation
                        )
                    }
                } else {
                    setState {
                        copy(
                            messages = getMessagesByConversationId(conversation.id),
                            selectedConversation = conversation,
                            isFetching = false,
                        )
                    }
                }
            }
        }
    }

    override val tag: String
        get() = ChatViewModel::class.java.name

    override fun createInitialState(): ChatUiState = ChatUiState()
    override fun handleEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.OnSendMessage -> {

                //TODO: feedback to user
                if (event.message.isBlank()) return

                setState { copy(isFetching = true) }

                onSendMessageJob = launchOnBackground {

                    val actualConversation =
                        currentState.selectedConversation
                            ?: runCatching { createConversation(event.message) }
                                .onFailure { setState { copy(isFetching = false) } }
                                .getOrThrow()

                    if (onSendMessageJob?.isCancelled == true) throw CancellationException()

                    val insertedMessage = insertMessageUseCase(
                        InsertMessageUseCase.Params(
                            textToSend = event.message,
                            conversationId = actualConversation.id
                        )
                    )

                    setState { copy(messages = listOf(insertedMessage) + messages) }

                    if (onSendMessageJob?.isCancelled == true) throw CancellationException()

                    val actualMessages = runCatching {
                        sendMessageToChatGPT(
                            SendMessageToChatGPTUseCase.Params(
                                messagesForContext = currentState.messages.take(3),
                                conversationId = actualConversation.id
                            )
                        )
                    }
                        .onFailure { setState { copy(isFetching = false) } }
                        .getOrThrow()

                    if (onSendMessageJob?.isCancelled == true) throw CancellationException()

                    setState {
                        copy(
                            isFetching = false,
                            messages = actualMessages,
                            selectedConversation = actualConversation
                        )
                    }
                }
            }
        }
    }

    override fun onCleared() {
        if (onSendMessageJob?.isActive == true) onSendMessageJob?.cancel()
    }

}
