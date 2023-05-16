package com.lvs.chatgpt.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lvs.chatgpt.ui.theme.BackgroundColor
import com.lvs.chatgpt.ui.theme.ChatGPTTheme
import com.lvs.chatgpt.ui.theme.Purple40
import com.lvs.data.remote.db.entities.MessageEntity

@Composable
fun ChatConversation(
    messages: List<MessageEntity>,
    onSendMessageListener: (String) -> Unit,
    showLoadingChatResponse: Boolean
) {
    ChatGPTTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = BackgroundColor,
        ) {
            Box(Modifier.fillMaxSize()) {
                Column(Modifier.fillMaxSize()) {
                    ChatMessageList(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        messages = messages
                    )
                    if (showLoadingChatResponse) BotPrintingView()
                    ChatTextInput(onSendMessageListener)
                }
            }
        }
    }
}


const val ConversationTestTag = "ConversationTestTag"

@Composable
fun BotPrintingView() {

    Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.fillMaxWidth()) {
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

@Composable
fun ChatMessageList(
    modifier: Modifier = Modifier,
    messages: List<MessageEntity>
) {
    val listState = rememberLazyListState()

    Box(modifier = modifier) {
        LazyColumn(
            contentPadding =
            WindowInsets.statusBars.add(WindowInsets(top = 90.dp)).asPaddingValues(),
            modifier = Modifier
                .testTag(ConversationTestTag)
                .fillMaxSize(),
            reverseLayout = true,
            state = listState,
        ) {
            items(messages.size) { index ->
                Box(modifier = Modifier.padding(bottom = if (index == 0) 10.dp else 0.dp)) {
                    Column {
                        ChatMessageCard(message = messages[index])
                    }
                }
            }
        }
    }
}
