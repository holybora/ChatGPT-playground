package com.lvs.chatgpt.ui

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lvs.chatgpt.ui.components.AppDrawer
import com.lvs.chatgpt.ui.home.HomeEvent
import com.lvs.chatgpt.ui.home.HomeViewModel
import com.lvs.chatgpt.ui.theme.ChatGPTTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CGPTApp(widthSizeClass: WindowWidthSizeClass) {

    val darkTheme = remember(key1 = "darkTheme") {
        mutableStateOf(true)
    }
    ChatGPTTheme(darkTheme.value) {

        val viewModel = hiltViewModel<HomeViewModel>()

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        val navController = rememberNavController()

        val navigationActions = remember(navController) {
            CGPTNavigationActions(navController)
        }

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute =
            navBackStackEntry?.destination?.route ?: CGPTDestinations.CHATS_ROUTE


        val sizeAwareDrawerState = rememberSizeAwareDrawerState(widthSizeClass == WindowWidthSizeClass.Expanded)

        val coroutineScope = rememberCoroutineScope()

        val chatListState = rememberLazyListState()

        ModalNavigationDrawer(
            drawerState = sizeAwareDrawerState,
            drawerContent = {
                ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.background) {
                    AppDrawer(
                        currentRoute = currentRoute,
                        onChatClicked = {
                            coroutineScope.launch {
                                viewModel.setEvent(HomeEvent.OnChatClicked(it))
                                sizeAwareDrawerState.close()
                            }
                        },
                        onNewChatClicked = {
                            coroutineScope.launch {
                                viewModel.setEvent(HomeEvent.OnNewChatClicked)
                                sizeAwareDrawerState.close()
                            }
                        },
                        closeDrawer = { coroutineScope.launch { sizeAwareDrawerState.close() } },
                        navigateToHome = navigationActions.navigateToHome,
                        onIconClicked = { darkTheme.value = !darkTheme.value },
                        selectedConversation = uiState.selectedConversation,
                        conversations = uiState.conversations
                    )
                }
            },
            content = {
                CGPTNavGraph(
                    navController = navController,
                    openDrawer = { coroutineScope.launch { sizeAwareDrawerState.open() } },
                    onDeleteChat = {
                        viewModel.setEvent(HomeEvent.OnDeleteChatClicked(uiState.selectedConversation))
                    },
                    chatListState = chatListState
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