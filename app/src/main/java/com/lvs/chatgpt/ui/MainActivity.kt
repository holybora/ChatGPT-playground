package com.lvs.chatgpt.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lvs.chatgpt.ui.chat.ChatConversation
import com.lvs.chatgpt.ui.components.AppBar
import com.lvs.chatgpt.ui.components.AppScaffold
import com.lvs.chatgpt.ui.theme.ChatGPTTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val drawerOpen by viewModel.drawerShouldBeOpened.collectAsStateWithLifecycle()

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            if (drawerOpen) {
                // Open drawer and reset state in VM.
                LaunchedEffect(Unit) {
                    // wrap in try-finally to handle interruption whiles opening drawer
                    try {
                        drawerState.open()
                    } finally {
                        viewModel.resetOpenDrawerAction()
                    }
                }
            }

            // Intercepts back navigation when the drawer is open
            val scope = rememberCoroutineScope()
            val focusManager = LocalFocusManager.current

            BackHandler {
                if (drawerState.isOpen) {
                    scope.launch {
                        drawerState.close()
                    }
                } else {
                    focusManager.clearFocus()
                }
            }
            val darkTheme = remember(key1 = "darkTheme") {
                mutableStateOf(true)
            }
            ChatGPTTheme(darkTheme.value) {


                Surface(
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppScaffold(
                        drawerState = drawerState,
                        onChatClicked = {
                            scope.launch {
                                viewModel.onChatClicked(it)
                                drawerState.close()
                            }
                        },
                        onNewChatClicked = {
                            scope.launch {
                                viewModel.onNewChatClicked()
                                drawerState.close()
                            }
                        },
                        onIconClicked = {
                            darkTheme.value = !darkTheme.value
                        },
                        conversations = uiState.conversations,
                        selectedConversation = uiState.selectedConversation
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            AppBar(onClickMenu = {
                                scope.launch {
                                    drawerState.open()
                                }
                            })
                            Divider()

                            ChatConversation(
                                messages = uiState.messages,
                                onSendMessageListener = {
                                    viewModel.onSendMessage(
                                        uiState.selectedConversation,
                                        it
                                    )
                                },
                                showLoadingChatResponse = uiState.isFetching
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ChatGPTTheme {
        Greeting("Android")
    }
}