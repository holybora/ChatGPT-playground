package com.lvs.chatgpt.ui.chat.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lvs.chatgpt.ui.chat.ChatScreen
import com.lvs.chatgpt.ui.chat.ChatViewModel
import com.lvs.data.remote.db.entities.ConversationEntity

internal const val conversationIdArg = "conversationId"
internal const val isNewArg = "isNew"

internal class ChatArgs(val conversationId: Long, val isNew: Boolean = false) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                conversationId = checkNotNull(savedStateHandle[conversationIdArg]),
                isNew = savedStateHandle[isNewArg] ?: false
            )
}

fun NavController.navigateToChat(conversationId: Long, isNew: Boolean) =
    navigate("chat/$conversationId/$isNew") {
    }

fun NavGraphBuilder.chatScreen(
    openDrawer: () -> Unit,
    onDeleteChat: (ConversationEntity?) -> Unit,
) =
    composable(
        "chat/{$conversationIdArg}/{$isNewArg}",
        arguments = listOf(
            //TODO: Replace on const
            navArgument(conversationIdArg) { type = NavType.LongType },
            //TODO: Replace on const
            navArgument(isNewArg) { type = NavType.BoolType; defaultValue = false }
        )
    ) {

        val viewModel = hiltViewModel<ChatViewModel>()

        ChatScreen(
            viewModel = viewModel,
            onDrawerClick = openDrawer,
            onDeleteChatClick = onDeleteChat
        )
    }