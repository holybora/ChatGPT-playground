package com.lvs.chatgpt.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lvs.data.remote.common.GPTRole
import com.lvs.data.remote.db.entities.MessageEntity

@Composable
fun ChatMessageCard(message: MessageEntity, isLast: Boolean = false) {
    val isBot = message.role == GPTRole.USER.value
    Column(
        horizontalAlignment = if (isBot) Alignment.End else Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .padding(top = if (isLast) 120.dp else 0.dp)
    ) {
        Box(
            modifier = Modifier
                .widthIn(0.dp, 260.dp) //mention max width here
                .background(
                    if (isBot) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                ),
        ) {
            Text(
                text = message.text,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                textAlign = TextAlign.Justify,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
