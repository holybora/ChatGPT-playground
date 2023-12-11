package com.lvs.chatgpt.ui.main

import android.util.Log
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lvs.chatgpt.ui.chat.navigation.navigateToChat
import com.lvs.chatgpt.ui.components.AppDrawer
import com.lvs.chatgpt.ui.newchat.navigation.navigateToNewChat
import com.lvs.chatgpt.ui.stt.navigation.navigateToTranscription
import com.lvs.chatgpt.ui.theme.ChatGPTTheme
import kotlinx.coroutines.launch

private const val TAG = "ChatGPTApp"

@Composable
fun ChatGPTApp(
    windowSizeClass: WindowSizeClass,
    appState: CGPTAppState = rememberCGPTAppState(windowSizeClass = windowSizeClass)
) {

    val darkTheme = remember(key1 = "darkTheme") {
        mutableStateOf(true)
    }
    ChatGPTTheme(darkTheme.value) {

        val viewModel = hiltViewModel<HomeViewModel>()

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val sizeAwareDrawerState =
            rememberSizeAwareDrawerState(appState.windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded)

        LaunchedEffect(key1 = uiState.openConversation) {
            Log.d(TAG, "openConversation SideEfeect")
            val convId = uiState.openConversation?.conversationId
            val isNew = uiState.openConversation?.isNew ?: false
            when (convId) {
                null -> appState.navController.navigateToNewChat()
                else -> appState.navController.navigateToChat(conversationId = convId, isNew = isNew)
            }
        }

        ModalNavigationDrawer(
            drawerState = sizeAwareDrawerState,
            drawerContent = {
                ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.background) {
                    AppDrawer(
                        onChatClicked = {
                            appState.coroutineScope.launch { sizeAwareDrawerState.close() }
                            viewModel.setEvent(MainEvent.OnChatClicked(it))
                        },
                        onNewChatClicked = {
                            appState.coroutineScope.launch { sizeAwareDrawerState.close() }
                            viewModel.setEvent(MainEvent.OnNewChatClicked)
                        },
                        onNewTranscriptionClicked = {
                            appState.coroutineScope.launch { sizeAwareDrawerState.close() }
                            appState.navController.navigateToTranscription()
                        },
                        closeDrawer = { appState.coroutineScope.launch { sizeAwareDrawerState.close() } },
                        onDayNightClicked = { darkTheme.value = !darkTheme.value },
                        conversations = uiState.conversations
                    )
                }
            },
            content = {
                MainNavHost(
                    navController = appState.navController,
                    openDrawer = { appState.coroutineScope.launch { sizeAwareDrawerState.open() } },
                    onDeleteChat = { viewModel.setEvent(MainEvent.OnDeleteChatClicked(it)) },
                    onNewChatCreated = {
                        viewModel.setEvent(MainEvent.OnNewChatCreated(it))
                    }
                )
            }
        )

    }
}


/**
 * Determine the drawer state to pass to the modal drawer.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberSizeAwareDrawerState(isExpandedScreen: Boolean): DrawerState {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    return if (!isExpandedScreen) {
        // If we want to allow showing the drawer, we use a real, remembered drawer
        // state defined above
        drawerState
    } else {
        // If we don't want to allow the drawer to be shown, we provide a drawer state
        // that is locked closed. This is intentionally not remembered, because we
        // don't want to keep track of any changes and always keep it closed
        DrawerState(DrawerValue.Closed)
    }
}

@Preview
@Composable
fun SomePreview() {
    Text(text = "ASAP")
}