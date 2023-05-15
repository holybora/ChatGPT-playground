package com.lvs.chatgpt.ui

import com.lvs.data.remote.db.entities.ConversationEntity
import com.lvs.data.remote.db.entities.MessageEntity

sealed class MainUiState(
    val isFetching: Boolean,
    val conversations: List<ConversationEntity>,
    val selectedConversation: Long
) {

    companion object {
        const val DEFAULT_CONVERSATION_ID = -1L
    }

    class Fetching(
        conversations: List<ConversationEntity> = emptyList(),
        selectedConversation: Long = DEFAULT_CONVERSATION_ID
    ) :
        MainUiState(isFetching = true, conversations, selectedConversation)

    class Success(
        conversations: List<ConversationEntity> = emptyList(),
        selectedConversation: Long = DEFAULT_CONVERSATION_ID
    ) : MainUiState(isFetching = false, conversations, selectedConversation)

}