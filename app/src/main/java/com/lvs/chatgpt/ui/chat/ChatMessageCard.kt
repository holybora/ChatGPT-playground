package com.lvs.chatgpt.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.CodeBlockStyle
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.material3.SetupMaterial3RichText
import com.lvs.chatgpt.ui.theme.BackGroundMessageGPT
import com.lvs.chatgpt.ui.theme.BackGroundMessageHuman
import com.lvs.chatgpt.ui.theme.ChatGPTTheme
import com.lvs.chatgpt.ui.theme.ColorTextGPT
import com.lvs.chatgpt.ui.theme.ColorTextHuman
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
                    if (!isBot) BackGroundMessageHuman else BackGroundMessageGPT,
                    shape = RoundedCornerShape(12.dp)
                ),
        ) {
            if (!isBot) {
                HumanMessageCard(message = message)
            } else {
                BotMessageCard(message = message)
            }
        }
    }
}

@Composable
fun HumanMessageCard(message: MessageEntity) {
    Text(
        text = message.text,
        fontSize = 14.sp,
        color = ColorTextHuman,
        modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
        textAlign = TextAlign.Justify,
    )
}

@Composable
fun BotMessageCard(message: MessageEntity) {
    ChatGPTTheme {
        SetupMaterial3RichText {
            RichText(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                style = RichTextStyle(
                    codeBlockStyle = CodeBlockStyle(
                        textStyle = TextStyle(
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            color = ColorTextGPT,
                        ),
                        wordWrap = true,
                        modifier = Modifier.background(
                            color = Color.Black,
                            shape = RoundedCornerShape(6.dp)
                        )
                    )
                )
            ) {
                Markdown(
                    message.text.trimIndent()
                )
            }
        }
    }
}