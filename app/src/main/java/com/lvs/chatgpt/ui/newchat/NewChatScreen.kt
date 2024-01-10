package com.lvs.chatgpt.ui.newchat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lvs.chatgpt.ui.chat.ChatConversation
import com.lvs.chatgpt.ui.components.AppBar

@Composable
fun NewChatScreen(
    viewModel: NewChatViewModel,
    onDrawerClick: () -> Unit,
    onNewChatCreated: (Long) -> Unit
) {


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        AppBar(
            onClickMenu = { onDrawerClick.invoke() },
            onDeleteClick = { },
            isDeleteAvailable = false
        )
        Divider()
        val focusManager = LocalFocusManager.current

        ChatConversation(
            messages = emptyList(),
            onSendMessageListener = {
                focusManager.clearFocus()
                viewModel.setEvent(NewChatEvent.OnSendMessage(it))
            },
            showLoading = false
        )

        val isVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
        LaunchedEffect(key1 = isVisible) {
            if (isVisible) {
                //hide fab button
            } else {
                if (uiState.conversationId != null) {
                    onNewChatCreated.invoke(uiState.conversationId as Long)
                }
            }
        }
    }
}