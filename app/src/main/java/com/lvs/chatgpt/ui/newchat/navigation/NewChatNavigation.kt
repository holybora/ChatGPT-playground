package com.lvs.chatgpt.ui.newchat.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.lvs.chatgpt.ui.newchat.NewChatScreen
import com.lvs.chatgpt.ui.newchat.NewChatViewModel

const val newChatRoute = "new_chat_route"

fun NavController.navigateToNewChat(navOptions: NavOptions? = null) =
    this.navigate(newChatRoute, navOptions)

fun NavGraphBuilder.newChatScreen(
    openDrawer: () -> Unit = {},
    onNewChatCreated: (Long) -> Unit,
) =
    composable(newChatRoute) {
        NewChatScreen(
            viewModel = hiltViewModel<NewChatViewModel>(),
            onDrawerClick = openDrawer,
            onNewChatCreated = onNewChatCreated
        )
    }