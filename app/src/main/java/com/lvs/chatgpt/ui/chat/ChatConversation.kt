package com.lvs.chatgpt.ui.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lvs.chatgpt.ui.theme.Purple40
import com.lvs.data.remote.common.GPTRole
import com.lvs.data.remote.db.entities.MessageEntity

@Composable
fun ChatConversation(
    messages: List<MessageEntity>,
    onSendMessageListener: (String) -> Unit,
    showLoadingChatResponse: Boolean
) {
    Column(Modifier.fillMaxSize()) {
        ChatMessageList(messages = messages)

        AnimatedVisibility(visible = showLoadingChatResponse) { BotPrintingView() }

        ChatTextInput(onSendMessageListener)
    }
}


@Composable
fun BotPrintingView() {
    Box(
        contentAlignment = Alignment.CenterEnd, modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, end = 8.dp)
    ) {
        Text(
            text = "Processing...",
            fontSize = 14.sp,
            color = Purple40,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 28.dp, vertical = 12.dp)
        )
    }
}

@Preview
@Composable
fun BotPrintingViewPreview() {
    BotPrintingView()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColumnScope.ChatMessageList(
    messages: List<MessageEntity>
) {
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 16.dp)
    ) {
        LazyColumn(
            contentPadding =
            WindowInsets.statusBars.add(WindowInsets(top = 90.dp)).asPaddingValues(),
            modifier = Modifier.fillMaxSize(),
            reverseLayout = true,
            state = listState,
        ) {
            items(messages.size) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = if (messages[index].role != GPTRole.USER.value) Alignment.CenterStart else Alignment.CenterEnd
                ) {
                    ChatMessageCard(message = messages[index])
                }
            }
        }
    }
}
