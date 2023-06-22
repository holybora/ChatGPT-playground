package com.lvs.chatgpt.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lvs.chatgpt.ui.components.AppBar

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    onDrawerClick: () -> Unit,
    chatListState: LazyListState,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.messages.size + uiState.isFetching.hashCode()) {
        chatListState.animateScrollToItem(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        AppBar(onClickMenu = { onDrawerClick.invoke() })
        Divider()

        ChatConversation(
            messages = uiState.messages,
            onSendMessageListener = {
                viewModel.handleEvent(ChatEvent.OnSendMessage(it))
            },
            showLoading = uiState.isFetching,
            listState = chatListState
        )
    }
}