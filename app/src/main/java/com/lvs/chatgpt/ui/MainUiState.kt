package com.lvs.chatgpt.ui

import com.lvs.data.remote.db.entities.ConversationEntity
import com.lvs.data.remote.db.entities.ConversationEntity.Companion.DEFAULT_CONVERSATION_ID
import com.lvs.data.remote.db.entities.MessageEntity

data class MainUiState(
    val isFetching: Boolean = false,
    val conversations: List<ConversationEntity> = emptyList(),
    val messages: List<MessageEntity> = emptyList(),
    val selectedConversation: Long = DEFAULT_CONVERSATION_ID
) {

}