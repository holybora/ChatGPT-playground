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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.lang.Exception
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

        if(message.isBlank()) return

        viewModelScope.launch(Dispatchers.IO) {
            val actualConversationId = if (conversationId == DEFAULT_CONVERSATION_ID) {
                runCatching { conversationRepo.newConversation(message) }
                    .onFailure { handleException(it) }
                    .onSuccess { _selectedConversation.value = it }
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
            updateMessages(conversationId)
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

                updateMessages(conversationId)
            } catch (ex: Exception) {
                handleException(ex)
            }

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
