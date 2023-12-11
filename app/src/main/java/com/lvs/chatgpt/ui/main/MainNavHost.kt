package com.lvs.chatgpt.ui.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.lvs.chatgpt.ui.chat.navigation.chatScreen
import com.lvs.chatgpt.ui.newchat.navigation.newChatRoute
import com.lvs.chatgpt.ui.newchat.navigation.newChatScreen
import com.lvs.chatgpt.ui.stt.navigation.transcriptionScreen
import com.lvs.data.remote.db.entities.ConversationEntity

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    openDrawer: () -> Unit,
    onNewChatCreated: (Long) -> Unit,
    onDeleteChat: (ConversationEntity?) -> Unit,
    startDestination: String = newChatRoute
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {

        newChatScreen(
            openDrawer = openDrawer,
            onNewChatCreated = onNewChatCreated
        )
        chatScreen(
            openDrawer = openDrawer,
            onDeleteChat = onDeleteChat
        )
        transcriptionScreen(
            openDrawer = openDrawer
        )

    }
}
