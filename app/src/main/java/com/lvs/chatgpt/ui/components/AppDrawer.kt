    package com.lvs.chatgpt.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.AddComment
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.lvs.chatgpt.constant.urlToImageAppIcon
import com.lvs.chatgpt.ui.CGPTDestinations.CHATS_ROUTE
import com.lvs.chatgpt.ui.theme.ChatGPTTheme
import com.lvs.data.remote.db.entities.ConversationEntity

@Composable
fun AppDrawer(
    currentRoute: String,
    selectedConversation: Long,
    conversations: List<ConversationEntity>,
    onChatClicked: (Long) -> Unit,
    navigateToHome: () -> Unit,
    closeDrawer: () -> Unit,
    onNewChatClicked: () -> Unit,
    onIconClicked: () -> Unit = {}
) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        DrawerHeader(clickAction = onIconClicked)
        DividerItem()
        DrawerItemHeader("Chats")
        NavigationDrawerItem(
            label = { Text(text = "New Chat") },
            selected = false,
            onClick = { onNewChatClicked(); navigateToHome(); closeDrawer() },
            icon = { Icon(Icons.Outlined.AddComment, null) },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        HistoryConversations(
            onChatClicked = onChatClicked, conversations = conversations, selectedConversation = selectedConversation
        )
    }
}

@Composable
private fun DrawerHeader(
    clickAction: () -> Unit = {}
) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {

        val (iconGPT, title, subtitle, dayNightIcon) = createRefs()

        Image(
            painter = rememberAsyncImagePainter(urlToImageAppIcon),
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                .size(34.dp)
                .then(Modifier.clip(RoundedCornerShape(6.dp)))
                .constrainAs(iconGPT) {
                    top.linkTo(parent.top)
                },
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        Text(
            "ChatGPT Lite",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(iconGPT.top)
                    start.linkTo(iconGPT.end)
                }
                .padding(start = 16.dp, top = 16.dp),
        )
        Text(
            "Powered by OpenAI",
            fontSize = 11.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .constrainAs(subtitle) {
                    top.linkTo(title.bottom)
                    start.linkTo(title.start)
                }
                .padding(start = 16.dp)
        )

        IconButton(
            onClick = { clickAction.invoke() },
            content = {
                Icon(
                    Icons.Filled.WbSunny,
                    "backIcon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(26.dp)
                )
            },
            modifier = Modifier.constrainAs(dayNightIcon) {
                top.linkTo(parent.top, margin = 16.dp)
                end.linkTo(parent.end)
            }
        )

    }
}

@Composable
private fun ColumnScope.HistoryConversations(
    onChatClicked: (Long) -> Unit, conversations: List<ConversationEntity>, selectedConversation: Long
) {
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .weight(1f, false),
    ) {
        items(conversations.size) { index ->
            NavigationDrawerItem(
                label = { Text(text = conversations[index].title) },
                selected = conversations[index].id == selectedConversation,
                onClick = { onChatClicked(conversations[index].id) },
                icon = { Icon(Icons.Filled.Message, null) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}

@Composable
private fun DrawerItemHeader(text: String) {
    Box(
        modifier = Modifier
            .heightIn(min = 52.dp)
            .padding(horizontal = 28.dp), contentAlignment = CenterStart
    ) {
        Text(
            text = text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun DividerItem(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}

@Preview("Drawer contents")
@Preview("Drawer contents (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppDrawer() {
    ChatGPTTheme {
        AppDrawer(
            currentRoute = CHATS_ROUTE,
            onChatClicked = { },
            onNewChatClicked = { },
            onIconClicked = { },
            closeDrawer = { },
            selectedConversation = 1,
            navigateToHome = { },
            conversations = listOf(
                ConversationEntity(
                    ConversationEntity.DEFAULT_CONVERSATION_ID, "New Chat", ""
                ),
                ConversationEntity(
                    1, "Some title", ""
                ),
                ConversationEntity(
                    2, "Some title 2", ""
                ),
            )
        )
    }
}
