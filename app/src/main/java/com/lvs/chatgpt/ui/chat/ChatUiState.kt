package com.lvs.chatgpt.ui.chat

import com.lvs.chatgpt.base.UiState
import com.lvs.data.local.main.MessageUiEntity
import com.lvs.data.remote.db.entities.ConversationEntity
import com.lvs.data.remote.db.entities.ConversationEntity.Companion.DEFAULT_CONVERSATION_ID

data class ChatUiState(
    val isFetching: Boolean = false,
    val conversations: List<ConversationEntity> = emptyList(),
    val selectedConversationId: Long = DEFAULT_CONVERSATION_ID,
    val messages: List<MessageUiEntity> = emptyList()
) : UiState