package com.lvs.chatgpt.ui.chat

import com.lvs.chatgpt.base.UiState
import com.lvs.data.local.main.MessageUiEntity
import com.lvs.data.remote.db.entities.ConversationEntity

data class ChatUiState(
    val isFetching: Boolean = false,
    val conversations: List<ConversationEntity> = emptyList(),
    val selectedConversation: ConversationEntity? = null,
    val messages: List<MessageUiEntity> = emptyList(),
) : UiState