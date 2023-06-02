package com.lvs.chatgpt.ui.main

import com.lvs.chatgpt.base.UiState
import com.lvs.data.remote.db.entities.ConversationEntity
import com.lvs.data.remote.db.entities.ConversationEntity.Companion.DEFAULT_CONVERSATION_ID
import com.lvs.data.remote.db.entities.MessageEntity

data class MainUiState(
    val isFetching: Boolean = false,
    val conversations: List<ConversationEntity> = emptyList(),
    val selectedConversationId: Long = DEFAULT_CONVERSATION_ID,
    val messages: List<MessageEntity> = emptyList(),
    val drawerShouldBeOpen: Boolean = false
) : UiState