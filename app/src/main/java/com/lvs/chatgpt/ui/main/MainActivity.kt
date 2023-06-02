package com.lvs.chatgpt.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.lvs.chatgpt.ui.chat.ChatScreen
import com.lvs.chatgpt.ui.components.AppBar
import com.lvs.chatgpt.ui.components.AppDrawer
import com.lvs.chatgpt.ui.theme.ChatGPTTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.lvs.chatgpt.ui.main.MainEvent.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val navController = rememberNavController()

            if (uiState.drawerShouldBeOpen) {
                // Open drawer and reset state in VM.
                LaunchedEffect(Unit) {
                    // wrap in try-finally to handle interruption whiles opening drawer
                    try {
                        drawerState.open()
                    } finally {
                        viewModel.setEvent(OnResetOpenDrawerAction)
                    }
                }
            }

            // Intercepts back navigation when the drawer is open
            val scope = rememberCoroutineScope()

            BackHandler(enabled = drawerState.isOpen) {
                scope.launch {
                    drawerState.close()
                }
            }
            val darkTheme = remember(key1 = "darkTheme") {
                mutableStateOf(true)
            }
            ChatGPTTheme(darkTheme.value) {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                ) {
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.background) {
                                AppDrawer(
                                    onChatClicked = {
                                        scope.launch {
                                            viewModel.setEvent(OnChatClicked(it))
                                            drawerState.close()
                                        }
                                    },
                                    onNewChatClicked = {
                                        scope.launch {
                                            viewModel.setEvent(OnNewChatClicked)
                                            drawerState.close()
                                        }
                                    },
                                    onIconClicked = { darkTheme.value = !darkTheme.value },
                                    selectedConversation = uiState.selectedConversationId,
                                    conversations = uiState.conversations
                                )
                            }
                        },
                        content = {
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                AppBar(onClickMenu = { scope.launch { drawerState.open() } })
                                Divider()

                                ChatScreen(
                                    onSendMessage = { viewModel.handleEvent(OnSendMessage(it)) },
                                    uiState = uiState
                                )
                            }
                        }
                    )
                }

            }

        }
    }
}