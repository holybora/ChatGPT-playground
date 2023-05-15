package com.lvs.chatgpt.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lvs.chatgpt.ui.MainUiState.Companion.DEFAULT_CONVERSATION_ID
import com.lvs.data.remote.db.entities.ConversationEntity
import com.lvs.data.remote.db.entities.MessageEntity
import com.lvs.data.remote.models.TextCompletionsParam
import com.lvs.data.remote.repositories.ConversationRepository
import com.lvs.data.remote.repositories.MessageRepository
import com.lvs.data.remote.repositories.OpenAIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.handleCoroutineException
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.StringBuilder
import java.util.HashMap
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val conversationRepo: ConversationRepository,
    private val messageRepo: MessageRepository,
    private val openAIRepository: OpenAIRepository
) : ViewModel() {

    companion object {
        val TAG = MainViewModel::class.simpleName
    }

    private val _selectedConversation = MutableStateFlow(DEFAULT_CONVERSATION_ID)
    val selectedConversation: Flow<Long> = _selectedConversation

    private val _messages = MutableStateFlow(emptyList<MessageEntity>())
    val messages: Flow<List<MessageEntity>> = _messages

    val conversationsFlow: Flow<List<ConversationEntity>> = conversationRepo.fetchConversations()

    private val _drawerShouldBeOpened = MutableStateFlow(false)
    val drawerShouldBeOpened = _drawerShouldBeOpened.asStateFlow()

    fun openDrawer() {
        _drawerShouldBeOpened.value = true
    }

    fun resetOpenDrawerAction() {
        _drawerShouldBeOpened.value = false
    }

    private fun updateMessages(conversationId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _messages.value = messageRepo.fetchMessages(conversationId).firstOrNull() ?: emptyList()
        }
    }

    fun onSendMessage(conversationId: Long, message: String) {

        viewModelScope.launch(Dispatchers.IO) {
            val actualConversationId = if (conversationId == DEFAULT_CONVERSATION_ID) {
                runCatching { conversationRepo.newConversation(message) }
                    .onFailure { handleException(it) }
                    .onSuccess { _selectedConversation.value = it }
                    .getOrThrow()
            } else conversationId


            // Execute API OpenAI
            val flow: Flow<String> = flowOf("Bot", " response", " some", " message")

            var answerFromGPT: String = ""

            flow.collect {
                answerFromGPT += it
                Log.d(TAG, it)
            }

            messageRepo.createMessage(MessageEntity.InsertionPrototype(actualConversationId, message, false))
            messageRepo.createMessage(
                MessageEntity.InsertionPrototype(
                    actualConversationId,
                    "Bot response  some  message",
                    true
                )
            )
            updateMessages(conversationId)
        }

    }

    private fun handleException(exception: Throwable) {
        Log.e(TAG, exception.toString())
    }

    fun onChatClicked(conversationId: Long) {
        _selectedConversation.value = conversationId
        updateMessages(conversationId)
    }

    fun onNewChatClicked() {
        _selectedConversation.value = DEFAULT_CONVERSATION_ID
    }

}
