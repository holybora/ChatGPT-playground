package com.lvs.chatgpt.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lvs.chatgpt.ui.MainUiState.Companion.DEFAULT_CONVERSATION_ID
import com.lvs.data.remote.api.enities.MessageDto
import com.lvs.data.remote.common.GPTModel
import com.lvs.data.remote.common.GPTRole
import com.lvs.data.remote.db.entities.ConversationEntity
import com.lvs.data.remote.db.entities.MessageEntity
import com.lvs.data.remote.repositories.ConversationRepository
import com.lvs.data.remote.repositories.MessageRepository
import com.lvs.data.remote.repositories.OpenAIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

//TODO: Error handling
@HiltViewModel
class MainViewModel @Inject constructor(
    private val conversationRepo: ConversationRepository,
    private val messageRepo: MessageRepository,
    private val openAIRepository: OpenAIRepository
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
            val conversations = conversationRepo.fetchConversations().first()
            val selectedConversation = conversations.firstOrNull()?.id ?: DEFAULT_CONVERSATION_ID
            _uiState.value = MainUiState(
                conversations = conversations,
                selectedConversation = selectedConversation,
                messages = messageRepo.fetchMessages(selectedConversation).first()
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
                runCatching {
                    val insertedConversationId = conversationRepo.newConversation(message)
                    _uiState.value = _uiState.value.copy(conversations = conversationRepo.fetchConversations().first())
                    insertedConversationId
                }
                    .onFailure { handleException(it) }
                    .getOrThrow()
            } else conversationId

            //TODO: move to usecases
            messageRepo.createMessage(
                MessageEntity.InsertionPrototype(
                    actualConversationId,
                    message,
                    GPTRole.USER.value
                )
            )

            _uiState.value = _uiState.value.copy(
                isFetching = true,
                selectedConversation = actualConversationId
            )

            val messages = messageRepo.fetchMessages(actualConversationId).firstOrNull()?.asReversed()
            //TODO: replace on converters
            try {
                val response = openAIRepository.textCompletionsWithStream(GPTModel.GPT35TURBO, messages?.map {
                    MessageDto(
                        role = it.role,
                        content = it.text
                    )
                } ?: emptyList())
                //TODO: think how better choose response message
                val responseMessage = response.choices.first().message
                messageRepo.createMessage(
                    MessageEntity.InsertionPrototype(
                        conversationId = actualConversationId,
                        text = responseMessage.content,
                        role = responseMessage.role
                    )
                )

                _uiState.value = _uiState.value.copy(
                    isFetching = false,
                    messages = messageRepo.fetchMessages(actualConversationId).first()
                )
            } catch (ex: Exception) {
                handleException(ex)
            }

        }

    }

    private fun handleException(exception: Throwable) {
        Log.e(TAG, exception.toString())
    }

    fun onChatClicked(conversationId: Long) {
        //TODO: add error handling
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(
                selectedConversation = conversationId,
                messages = messageRepo.fetchMessages(conversationId).first()
            )
        }
    }

    fun onNewChatClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(
                selectedConversation = DEFAULT_CONVERSATION_ID,
                messages = messageRepo.fetchMessages(DEFAULT_CONVERSATION_ID).first()
            )
        }
    }

}
