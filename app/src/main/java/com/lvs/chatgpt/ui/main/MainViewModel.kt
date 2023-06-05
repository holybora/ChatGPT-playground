package com.lvs.chatgpt.ui.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.lvs.chatgpt.base.BaseViewModel
import com.lvs.data.remote.db.entities.ConversationEntity.Companion.DEFAULT_CONVERSATION_ID
import com.lvs.domain.CreateConversationUseCase
import com.lvs.domain.GetConversationUseCase
import com.lvs.domain.GetConversationsFlowUseCase
import com.lvs.domain.GetMessagesByConversationIdUseCase
import com.lvs.domain.SendMessageToChatGPTUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

//TODO: Error handling
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getConversations: GetConversationUseCase,
    private val createConversation: CreateConversationUseCase,
    private val getConversationsFlowUseCase: GetConversationsFlowUseCase,
    private val sendMessageToChatGPT: SendMessageToChatGPTUseCase,
    private val getMessagesByConversationId: GetMessagesByConversationIdUseCase
) : BaseViewModel<MainEvent, MainUiState, MainEffect>() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getConversationsFlowUseCase().collect {
                setState {
                    copy(conversations = it)
                }
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            val conversations = getConversations()
            val selectedConversation = conversations.firstOrNull()
            val selectedConversationId = selectedConversation?.id
                ?: runCatching { createConversation("New Chat") }
                    .onFailure { handleException(it) }
                    .getOrThrow()

            setState {
                copy(
                    conversations = conversations,
                    selectedConversationId = selectedConversationId,
                    messages = getMessagesByConversationId(selectedConversationId)
                )
            }
        }
    }

    override fun createInitialState(): MainUiState = MainUiState()
    override fun handleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.OnChatClicked -> {
                if(event.chatId == currentState.selectedConversationId) return

                viewModelScope.launch(Dispatchers.IO) {
                    setState {
                        copy(
                            selectedConversationId = event.chatId,
                            messages = getMessagesByConversationId(event.chatId)
                        )
                    }
                    setEffect { MainEffect.ScrollChatToZero }
                }
            }

            is MainEvent.OnNewChatClicked -> {
                setState {
                    copy(
                        selectedConversationId = DEFAULT_CONVERSATION_ID,
                        messages = emptyList()
                    )
                }
                setEffect { MainEffect.ScrollChatToZero }
            }

            is MainEvent.OnOpenDrawer -> setState {
                copy(drawerShouldBeOpen = true)
            }

            is MainEvent.OnResetOpenDrawerAction -> setState {
                copy(drawerShouldBeOpen = false)
            }

            is MainEvent.OnSendMessage -> {

                if (event.message.isBlank()) return

                setState { copy(isFetching = true) }

                viewModelScope.launch(Dispatchers.IO) {

                    val actualConversationId =
                        currentState.selectedConversationId
                            .takeIf { currentState.selectedConversationId != DEFAULT_CONVERSATION_ID }
                            ?: runCatching { createConversation(event.message) }
                                .onFailure { handleException(it) }
                                .getOrThrow()

                    val actualMessages = sendMessageToChatGPT(
                        SendMessageToChatGPTUseCase.Params(
                            textToSend = event.message,
                            messagesForContext = currentState.messages,
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
        val TAG = MainViewModel::class.simpleName
    }
}
