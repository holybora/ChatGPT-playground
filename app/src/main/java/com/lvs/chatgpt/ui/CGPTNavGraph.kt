package com.lvs.chatgpt.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lvs.chatgpt.ui.CGPTDestinations.CHATS_ROUTE
import com.lvs.chatgpt.ui.CGPTDestinations.SETTINGS_ROUTE
import com.lvs.chatgpt.ui.chat.ChatScreen
import com.lvs.chatgpt.ui.chat.ChatViewModel
import com.lvs.chatgpt.ui.settings.SettingsScreen

@Composable
fun CGPTNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    openDrawer: () -> Unit = {},
    startDestination: String = CHATS_ROUTE,
    chatListState: LazyListState

) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(CHATS_ROUTE) {
            val viewModel = hiltViewModel<ChatViewModel>()

            ChatScreen(
                viewModel = viewModel,
                onDrawerClick = openDrawer,
                chatListState = chatListState
            )
        }
        composable(SETTINGS_ROUTE) {
            SettingsScreen()
        }
    }
}
