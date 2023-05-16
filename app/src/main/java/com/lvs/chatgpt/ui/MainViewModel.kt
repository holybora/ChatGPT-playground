package com.lvs.chatgpt.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lvs.data.remote.db.entities.ConversationEntity.Companion.DEFAULT_CONVERSATION_ID
import com.lvs.domain.CreateConversationUseCase
import com.lvs.domain.GetConversationUseCase
import com.lvs.domain.GetMessagesByConversationIdUseCase
import com.lvs.domain.SendMessageToChatGPTUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

//TODO: Error handling
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getConversations: GetConversationUseCase,
    private val sendMessageToChatGPT: SendMessageToChatGPTUseCase,
    private val createConversation: CreateConversationUseCase,
    private val getMessagesByConversationId: GetMessagesByConversationIdUseCase
) : ViewModel() {

    companion object {
        val TAG = MainViewModel::class.simpleName
    }

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    private val _drawerShouldBeOpened = MutableStateFlow(false)
    val drawerShouldBeOpened = _drawerShouldBeOpened.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val conversations = getConversations(Unit)
            val selectedConversation = conversations.firstOrNull()?.id ?: DEFAULT_CONVERSATION_ID
            _uiState.value = MainUiState(
                conversations = conversations,
                selectedConversation = selectedConversation,
                messages = getMessagesByConversationId(selectedConversation)
            )
        }
    }

    fun openDrawer() {
        _drawerShouldBeOpened.value = true
    }

    fun resetOpenDrawerAction() {
        _drawerShouldBeOpened.value = false
    }

    fun onSendMessage(conversationId: Long, message: String) {

        if (message.isBlank()) return

        viewModelScope.launch(Dispatchers.IO) {
            val actualConversationId = if (conversationId == DEFAULT_CONVERSATION_ID) {
                runCatching { createConversation(message) }
                    .onFailure { handleException(it) }
                    .getOrThrow()
            } else conversationId


            _uiState.value = _uiState.value.copy(
                isFetching = true,
                selectedConversation = actualConversationId
            )

            val actualMessages = sendMessageToChatGPT(
                SendMessageToChatGPTUseCase.Params(
                    textToSend = message,
                    messagesForContext = _uiState.value.messages,
                    conversationId = actualConversationId
                )
            )

            _uiState.value = _uiState.value.copy(
                isFetching = false,
                messages = actualMessages
            )
        }

    }

    private fun handleException(exception: Throwable) {
        Log.e(TAG, exception.toString())
    }

    fun onChatClicked(conversationId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(
                selectedConversation = conversationId,
                messages = getMessagesByConversationId(conversationId)
            )
        }
    }

    fun onNewChatClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(
                selectedConversation = DEFAULT_CONVERSATION_ID,
                messages = getMessagesByConversationId(DEFAULT_CONVERSATION_ID)
            )
        }
    }

}
