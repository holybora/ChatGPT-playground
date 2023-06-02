package com.lvs.chatgpt.ui.chat

import androidx.compose.runtime.Composable
import com.lvs.chatgpt.ui.main.MainUiState

@Composable
fun ChatScreen(
    onSendMessage: (String) -> Unit,
    uiState: MainUiState
) {

    ChatConversation(
        messages = uiState.messages,
        onSendMessageListener = { onSendMessage(it) },
        showLoadingChatResponse = uiState.isFetching
    )

}