package com.lvs.chatgpt.ui.components

import android.annotation.SuppressLint
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue.Closed
import androidx.compose.runtime.Composable
import com.lvs.data.remote.db.entities.ConversationEntity

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    drawerState: DrawerState = rememberDrawerState(initialValue = Closed),
    onChatClicked: (Long) -> Unit,
    onNewChatClicked: () -> Unit,
    onIconClicked: () -> Unit = {},
    selectedConversation: Long,
    conversations: List<ConversationEntity>,
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.background) {
                AppDrawer(
                    onChatClicked = onChatClicked,
                    onNewChatClicked = onNewChatClicked,
                    onIconClicked = onIconClicked,
                    selectedConversation = selectedConversation,
                    conversations = conversations
                )
            }
        },
        content = content
    )

}