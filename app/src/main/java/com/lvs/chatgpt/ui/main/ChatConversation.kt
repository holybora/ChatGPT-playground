package com.lvs.chatgpt.ui.main

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lvs.data.local.main.MessageUiEntity
import com.lvs.data.remote.common.GPTRole
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChatConversation(
    messages: List<MessageUiEntity>,
    onSendMessageListener: (String) -> Unit,
    showLoading: Boolean,
    listState: LazyListState = rememberLazyListState()
) {
    Column(Modifier.fillMaxSize()) {
        ChatMessageList(
            messages = messages,
            listState = listState,
            showLoading = showLoading
        )

        ChatTextInput(onSendMessageListener)
    }
}

@Composable
fun ColumnScope.ChatMessageList(
    messages: List<MessageUiEntity>,
    listState: LazyListState = rememberLazyListState(),
    showLoading: Boolean
) {
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
            item(showLoading) {
                if (showLoading) {
                    ChatMessageCard(
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        text = "Processing...",
                        showCursorAnimation = true
                    )
                }
            }
            items(messages.size, key = { messages[it].uid }) { index ->
                val message = messages[index]
                val isUser = message.role == GPTRole.USER
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
                ) {

                    val animateTextShowing = !isUser && index == 0
                    ChatMessageCard(
                        backgroundColor = if (isUser) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                        text = message.text,
                        showCursorAnimation = false,
                        animateTextShowing = animateTextShowing
                    )
                }
            }


        }
    }
}

@Composable
fun ChatMessageCard(
    backgroundColor: Color,
    text: String,
    showCursorAnimation: Boolean = false,
    animateTextShowing: Boolean = false
) {

    val scope = rememberCoroutineScope()

    val source = remember { mutableStateOf("") }
    if (animateTextShowing) {
        LaunchedEffect(key1 = text) {
            scope.launch {
                text.toCharArray()
                    .forEach {
                        delay(20)
                        source.value += it
                    }
            }
        }
    } else source.value = text

    Log.i("Animated", "recompose with string $: $source")

    Box(
        modifier = Modifier
            .widthIn(0.dp, 260.dp) //mention max width here
            .padding(vertical = 4.dp)
            .background(
                backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 4.dp),
    ) {

        if (showCursorAnimation) {
            val infiniteTransition = rememberInfiniteTransition(label = "cursor blinking")
            val alpha by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 300
                    },
                    repeatMode = RepeatMode.Reverse
                ), label = "cursor blinking"
            )

            val style = SpanStyle(
                background = Color.White.copy(alpha = alpha)
            )

            val message = AnnotatedString.Builder().run {
                append("${source.value}  ")
                addStyle(style, text.length, text.length + 2)
                toAnnotatedString()
            }

            Text(
                text = message,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(horizontal = 18.dp, vertical = 12.dp),
                textAlign = TextAlign.Justify,
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text(
                text = source.value,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(horizontal = 18.dp, vertical = 12.dp)
                    .animateContentSize(animationSpec = tween(200)),
                textAlign = TextAlign.Justify,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }


    }
}

@Composable
fun ChatTextInput(
    onSendMessageListener: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf(TextFieldValue("")) }

    Box(
        // Use navigationBarsPadding() imePadding() and , to move the input panel above both the
        // navigation bar, and on-screen keyboard (IME)
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding(),
    ) {
        Column {
            Divider(Modifier.height(0.2.dp))
            Box(
                Modifier
                    .padding(horizontal = 4.dp)
                    .padding(top = 6.dp, bottom = 10.dp)
            ) {
                Row {
                    TextField(
                        value = text,
                        onValueChange = {
                            text = it
                        },
                        label = null,
                        placeholder = { Text("Ask me anything", fontSize = 12.sp) },
                        shape = RoundedCornerShape(25.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp)
                            .weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedLabelColor = Color.White,
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = {
                            val textClone = text.text
                            text = TextFieldValue("")
                            onSendMessageListener(textClone)
                        })
                    )
                    IconButton(onClick = {
                        scope.launch {
                            val textClone = text.text
                            text = TextFieldValue("")
                            onSendMessageListener(textClone)
                        }
                    }) {
                        Icon(
                            Icons.Filled.Send,
                            "sendMessage",
                            modifier = Modifier.size(26.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }
    }
}
