package com.lvs.chatgpt.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lvs.chatgpt.ui.components.AppBar
import com.lvs.data.remote.db.entities.ConversationEntity

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    onDrawerClick: () -> Unit,
    onDeleteChatClick: (ConversationEntity?) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val chatListState = rememberLazyListState()


    LaunchedEffect(uiState.messages.size + uiState.isFetching.hashCode()) {
        chatListState.animateScrollToItem(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        AppBar(
            onClickMenu = { onDrawerClick.invoke() },
            onDeleteClick = { onDeleteChatClick.invoke(uiState.selectedConversation) },
            isDeleteAvailable = true
        )
        Divider()

        ChatConversation(
            messages = uiState.messages,
            onSendMessageListener = {
                viewModel.setEvent(ChatEvent.OnSendMessage(it))
            },
            showLoading = uiState.isFetching,
            listState = chatListState
        )
    }
}